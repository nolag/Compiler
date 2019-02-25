package cs444.lexer;

import cs444.lexer.Token.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestHelper {
    public static void assertTokenFor(String string, Type tokenType) throws Exception {
        Lexer scanner = getScannerFor(string);

        Token token = scanner.getNextToken();
        assertToken(tokenType, string, token);
        token = scanner.getNextToken();
        assertToken(Token.Type.EOF, "", token);
        assertNull(scanner.getNextToken());
    }

    // Used mainly to test the scanner throws exceptions for lexically invalid strings
    public static void scanString(String string) throws Exception {
        Lexer scanner = getScannerFor(string);
        while (null != scanner.getNextToken()) {
        }
    }

    public static Lexer getScannerFor(String input) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(input));

        return new Lexer(reader);
    }

    public static void assertToken(Token.Type type, String lexeme, Token token) {
        String failMessage = "Unexpected token: " + token;
        assertEquals(failMessage, type, token.type);
        assertEquals(failMessage, lexeme, token.lexeme);
    }

    // useful for debugging
    public static void printAllTokensFor(String string) throws Exception {
        Lexer scanner = getScannerFor(string);
        Token token;
        while (null != (token = scanner.getNextToken())) {
            System.out.println(token);
        }
    }
}