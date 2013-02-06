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
        for(int i = 0; i < bNFExpr.from; i++){ // add A
            firstRuleRHS.add(rule.getRightHandSideToken(i));
        }
        firstRuleRHS.add(newNonTerm); // add N_B1
        for(int i = bNFExpr.to + 1; i < rule.rightHandSideSize(); i++){
            firstRuleRHS.add(rule.getRightHandSideToken(i));
        }
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

    private List<Rule> expandOrBNFExpr(Rule rule, Range bNFExpr) {
		// TODO Auto-generated method stub
		return null;
	}

}
