package cs444.cfgrulesgenerator;

import java.util.ArrayList;
import java.util.List;

import cs444.cfgrulesgenerator.lexer.Token;

public class TestHelper {
    protected Rule ruleFor(Token... tokens){
        Token LHS = tokens[0];
    	List<Token> RHS = new ArrayList<Token>();

    	for (int i = 1; i < tokens.length; i++) {
    		RHS.add(tokens[i]);
		}

    	return new Rule(LHS, RHS);
    }
}
