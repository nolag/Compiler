package cs444.cfgrulesgenerator;

import java.util.ArrayList;
import java.util.List;

import cs444.cfgrulesgenerator.Rule;
import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.generator.lexer.nfa.transition.Range;

public class RuleExpander{
    public List<Rule> expand(Rule rule) throws BNFParseException{
    	Range bNFExpr = rule.getFirstBNFExpression();

        Token bNFToken = rule.getRightHandSideToken(bNFExpr.from);
        switch (bNFToken.type) {
        case LBRACKET:
            return expandZeroOrOneBNFExpr(rule, bNFExpr);
        case LBRACE:
            return expandZeroOrMoreBNFExpr(rule, bNFExpr);
        case LPAREN:
            return expandOrBNFExpr(rule, bNFExpr);
        default:
            throw new BNFParseException("Unexpected first token on BNF Expression. " +
                                        "Token: " + bNFToken + " in Rule: " + rule +
                                        ".");
        }
    }

    // if N -> A [B] C then
    // first rule: N -> A C
    // second rule: N -> A B C
    private List<Rule> expandZeroOrOneBNFExpr(Rule rule, Range bNFExpr) {
        List<Rule> ret = new ArrayList<Rule>();

        // first rule: N -> A C
        List<Token> firstRuleRHS = new ArrayList<Token>();
        for(int i = 0; i < rule.rightHandSideSize(); i++){
            if(bNFExpr.includes(i)) continue; // skip optional symbols

            firstRuleRHS.add(rule.getRightHandSideToken(i));
        }
        ret.add(new Rule(rule.leftHandSide, firstRuleRHS));

        // second rule: N -> A B C
        List<Token> secondRuleRHS = new ArrayList<Token>();
        for(int i = 0; i < rule.rightHandSideSize(); i++){
            if(bNFExpr.to == i ||
               bNFExpr.from == i) continue; // skip [ ] of BNF Expression

            secondRuleRHS.add(rule.getRightHandSideToken(i));
        }
        ret.add(new Rule(rule.leftHandSide, secondRuleRHS));

        return ret;
    }

    // if N -> A {B} C then
    // 1: N -> A N_B1 C
    // 2: N_B1 -> N_B1 B
    // 3: N_B1 -> epsilon
    private List<Rule> expandZeroOrMoreBNFExpr(Rule rule, Range bNFExpr) {
        List<Rule> ret = new ArrayList<Rule>();

        // Create new Non Terminal rule (N_B1)
        Token newNonTerm = getNewNonTermFor(rule, bNFExpr);

        // first rule: N -> A N_B1 C
        List<Token> firstRuleRHS = new ArrayList<Token>();
        addSymbolsBeforeExpr(rule, bNFExpr, firstRuleRHS); // add A
        firstRuleRHS.add(newNonTerm); // add N_B1
        addSymbolsAfterExpr(rule, bNFExpr, firstRuleRHS);
        ret.add(new Rule(rule.leftHandSide, firstRuleRHS));

        // second rule: N_B1 -> N_B1 B
        List<Token> secondRuleRHS = new ArrayList<Token>();
        secondRuleRHS.add(newNonTerm);                      // add N_B1
        for(int i = bNFExpr.from + 1; i < bNFExpr.to; i++){ // add B
            secondRuleRHS.add(rule.getRightHandSideToken(i));
        }
        ret.add(new Rule(newNonTerm, secondRuleRHS));

        // 3: N_B1 -> epsilon
        List<Token> thirdRuleRHS = new ArrayList<Token>();
        thirdRuleRHS.add(new Token(Token.Type.EPSILON, " "));
        ret.add(new Rule(newNonTerm, thirdRuleRHS));

        return ret;
    }

    private int counter = 0;
    private Token getNewNonTermFor(Rule rule, Range bNFExpr){
        StringBuffer newNonTerm = new StringBuffer("N");

        for (int i = bNFExpr.from + 1; i < bNFExpr.to; i++){
            newNonTerm.append("_" + rule.getRightHandSideToken(i).lexeme);
        }
        newNonTerm.append(++counter);

        return new Token(Token.Type.NON_TERMINAL, newNonTerm.toString());
    }

    // N -> a (b | c | ... | d) e
    // 1: N -> a b e
    // 2: N -> a c e
    // ...
    private List<Rule> expandOrBNFExpr(Rule rule, Range bNFExpr) {
        List<Rule> ret = new ArrayList<Rule>();

        int numPipes = countNumOfPipes(rule, bNFExpr);

        // create numPipes + 1 rules
        for(int i = 0, currSubExprIndex = bNFExpr.from + 1;
            i <= numPipes; i++){
            List<Token> rightHandSide = new ArrayList<Token>();
            addSymbolsBeforeExpr(rule, bNFExpr, rightHandSide);

            Token token;
            for(; (token = rule.getRightHandSideToken(currSubExprIndex)).type !=
                    Token.Type.PIPE &&
                    currSubExprIndex < bNFExpr.to; currSubExprIndex++){
                rightHandSide.add(token);
            }
            currSubExprIndex++; // point to next token after '|'

            addSymbolsAfterExpr(rule, bNFExpr, rightHandSide);

            ret.add(new Rule(rule.leftHandSide, rightHandSide));
        }

        return ret;
    }

    private int countNumOfPipes(Rule rule, Range bNFExpr){
        int counter = 0;
        for(int i = bNFExpr.from + 1; i < bNFExpr.to; i++){
            if(rule.getRightHandSideToken(i).type ==
               Token.Type.PIPE) counter++;
        }
        return counter;
    }

    private void addSymbolsBeforeExpr(Rule rule, Range bNFExpr,
                                      List<Token> ruleRHS) {
        for(int i = 0; i < bNFExpr.from; i++){
            ruleRHS.add(rule.getRightHandSideToken(i));
        }
    }

    private void addSymbolsAfterExpr(Rule rule, Range bNFExpr,
                                     List<Token> ruleRHS) {
        for(int i = bNFExpr.to + 1; i < rule.rightHandSideSize(); i++){
            ruleRHS.add(rule.getRightHandSideToken(i));
        }
    }
}
