package cs444.cfgrulesgenerator.exceptions;

import cs444.cfgrulesgenerator.lexer.Token;

import java.util.Set;

public class UnexpectedTokenException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnexpectedTokenException(Token t, Set<String> expectedTokens) {
        super("Unexpected " + t.type + " token with value " + t.lexeme + " expected token types " + tokensAsString(expectedTokens));
    }

    private static String tokensAsString(Set<String> expectedTokens) {
        StringBuilder sb = new StringBuilder();

        for (String expected : expectedTokens) {
            sb.append(expected).append(", ");
        }

        return sb.toString();
    }
}
