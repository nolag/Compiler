package cs444.cfgrulesgenerator;

import java.util.ArrayList;
import java.util.List;

import cs444.cfgrulesgenerator.Rule;
import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.generator.lexer.nfa.transition.Range;

public class RuleExpander{
    public static List<Rule> expand(Rule rule) throws BNFParseException{
    	Range bNFExpr = rule.getFirstBNFExpression();

        Token bNFToken = rule.getRightHandSideToken(bNFExpr.from);
        switch (bNFToken.type) {
        case LBRACKET:
            return expandZeroOrOneBNFExpr(rule, bNFExpr);
        case LBRACE:
            return expandZeroOrMoreBNFExpr(rule, bNFExpr);
        default:
            break;
        }
        return null;
    }

    // if N -> A [B] C then
    // first rule: N -> A C
    // second rule: N -> A B C
    private static List<Rule> expandZeroOrOneBNFExpr(Rule rule, Range bNFExpr) {
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
    // 3: N_B1 -> N_B1 B
    // 4: N_B1 -> epsilon
    private static List<Rule> expandZeroOrMoreBNFExpr(Rule rule, Range bNFExpr) {
		return null;
        
    }
}
