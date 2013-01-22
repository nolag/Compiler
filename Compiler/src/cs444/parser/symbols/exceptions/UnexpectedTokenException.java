package cs444.parser.symbols.exceptions;

import cs444.lexer.Token;
import cs444.parser.symbols.StateTerminal;

public class UnexpectedTokenException extends Exception{
    private static final long serialVersionUID = 1L;

    private static String tokensAsString(StateTerminal [] expectedTokens){
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for(; i < expectedTokens.length - 1; i ++) sb.append(expectedTokens[i].factory.getType()).append(", ");
        if(expectedTokens.length > 0) sb.append(expectedTokens[i].factory.getType());

        return sb.toString();
    }

    public UnexpectedTokenException(Token t, StateTerminal [] expectedTokens){
        super("Unexpected " + t.type + " token with value " + t.type + " expected token types " + tokensAsString(expectedTokens));
    }
}
