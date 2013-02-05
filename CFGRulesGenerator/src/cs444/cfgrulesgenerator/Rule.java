package cs444.cfgrulesgenerator;

import java.util.List;

import cs444.cfgrulesgenerator.lexer.Token;

public class Rule{
    public final Token leftHandSide;
    public List<Token> rightHandSide;

    public Rule(Token leftHandSide, List<Token> righHandSide){
        this.leftHandSide = leftHandSide;
        this.rightHandSide = righHandSide;
    }

    public String toString(){
        StringBuffer result = new StringBuffer();

        // TODO: remove ':' and wrap rules inside "rules.add("...");"
        result.append(leftHandSide.lexeme);

        for (Token symbol : rightHandSide) {
            result.append(" " + symbol.lexeme);
        }

        return result.toString();
    }
}
