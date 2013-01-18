package cs444.lexer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Test;

import cs444.lexer.Token.Type;

public class LexerTest {
    @Test
    public void testValidTraditionalComment() throws Exception {
       assertTokenFor("/* some comment */", Token.Type.COMMENT);
    }

    @Test
    public void testValidEndOfLineComment() throws Exception {
        assertTokenFor("// some comment", Token.Type.END_LINE_COMMENT);
    }

    @Test
    public void testValidIdentifiers() throws Exception {
        assertTokenFor("String", Token.Type.ID);
        assertTokenFor("i3", Token.Type.ID);
        assertTokenFor("MAX_VALUE", Token.Type.ID);
        assertTokenFor("isLetterOrDigit", Token.Type.ID);
    }

    @Test
    public void testValidKeywords() throws Exception {
        assertTokenFor("if", Token.Type.IF);
        assertTokenFor("implements", Token.Type.IMPLEMENTS);
    }

    @Test
    public void testValidIntLiterals() throws Exception {
        assertTokenFor("0", Token.Type.DECIMAL_INTEGER_LITERAL);
        assertTokenFor("8", Token.Type.DECIMAL_INTEGER_LITERAL);
        assertTokenFor("999990", Token.Type.DECIMAL_INTEGER_LITERAL);
    }

    @After
    public void runAfterEveryTest() throws IOException {
        Lexer.reset();
    }

    private void assertTokenFor(String string, Type tokenType) throws Exception {
        Lexer scanner = getScannerFor(string);

        Token token = scanner.getNextToken();
        assertToken(tokenType, string, token);
        assertNull(scanner.getNextToken());
        Lexer.reset();
    }

    private Lexer getScannerFor(String input) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(input));

        Lexer.load(reader);
        return Lexer.getInstance();
    }

    private void assertToken(Token.Type type, String lexeme, Token token){
        String failMessage = "Unexpected token: " + token;
        assertEquals(failMessage, type, token.getType());
        assertEquals(failMessage, lexeme, token.getLexeme());
    }

    // useful for debugging
    @SuppressWarnings("unused")
    private void printAllTokensFor(String string) throws Exception{
        Lexer scanner = getScannerFor(string);
        Token token;
        while (null != (token = scanner.getNextToken())) {
            System.out.println(token.toString());
        }
    }
}
