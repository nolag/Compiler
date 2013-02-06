package cs444.cfgrulesgenerator;

import java.util.List;

import cs444.cfgrulesgenerator.Rule;
import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.generator.lexer.nfa.transition.Range;

public class RuleExpander{
    public static List<Rule> expand(Rule rule) throws BNFParseException{
    	Range BNFExpr = rule.getFirstBNFExpression();

        return null;
    }
}
