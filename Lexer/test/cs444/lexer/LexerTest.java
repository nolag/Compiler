package cs444.lexer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Test;

import cs444.lexer.Token.Type;

public class LexerTest {
    BufferedReader reader;

    @Test
    public void testValidTraditionalComment() throws Exception {
       assertTokenFor("/* some comment */", Token.Type.COMMENT);
    }

    @Test
    public void testValidEndOfLineComment() throws Exception {
        assertTokenFor("// some comment", Token.Type.END_LINE_COMMENT);
    }

    @After
    public void runAfterEveryTest() throws IOException {
        if (null != reader && reader.markSupported())
            reader.reset();
    }

    private void assertTokenFor(String string, Type tokenType) throws Exception {
        Lexer scanner = getScannerFor(string);

        Token token = scanner.getNextToken();
        assertToken(tokenType, string, token);
        assertNull(scanner.getNextToken());
    }

    private Lexer getScannerFor(String input) throws IOException {
        reader = new BufferedReader(new StringReader(input));
        reader.mark(input.length());

        Lexer.load(reader);
        return Lexer.getInstance();
    }

    private void assertToken(Token.Type type, String lexeme, Token token){
        System.out.println(lexeme);
        String failMessage = "Unexpected token: " + token;
        assertEquals(failMessage, type, token.getType());
        assertEquals(failMessage, lexeme, token.getLexeme());
    }
}
