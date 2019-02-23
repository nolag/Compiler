package cs444.parser.symbols.exceptions;

import java.util.Set;

import cs444.CompilerException;
import cs444.lexer.Token;

public class UnexpectedTokenException extends CompilerException{
    private static final long serialVersionUID = 1L;

    private static String tokensAsString(Set<String> expectedTokens){
        StringBuilder sb = new StringBuilder();

        for(String expected : expectedTokens) sb.append(expected).append(", ");

        return sb.toString();
    }

    public UnexpectedTokenException(Token t, Set<String> expectedTokens){
        super("Unexpected " + t.type + " token with value " + t.lexeme + " expected token types " + tokensAsString(expectedTokens));
    }
}
