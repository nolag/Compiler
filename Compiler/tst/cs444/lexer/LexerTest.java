package cs444.lexer;

import org.junit.Test;

import cs444.TestHelper;
import cs444.lexer.Token;

public class LexerTest {
    @Test
    public void testValidTraditionalComment() throws Exception {
       TestHelper.assertTokenFor("/* some comment */", Token.Type.COMMENT);
    }

    @Test
    public void testDetectTraditionalCommentWithStars() throws Exception {
       TestHelper.assertTokenFor("/* some * comment with /* */", Token.Type.COMMENT);
    }

    @Test
    public void testDetectTraditionalCommentEndingStars() throws Exception {
       TestHelper.assertTokenFor("/* some comment ***/", Token.Type.COMMENT);
    }

    @Test
    public void testValidEndOfLineComment() throws Exception {
        TestHelper.assertTokenFor("// some comment", Token.Type.END_LINE_COMMENT);
    }

    @Test
    public void testValidIdentifiers() throws Exception {
        TestHelper.assertTokenFor("String", Token.Type.ID);
        TestHelper.assertTokenFor("i3", Token.Type.ID);
        TestHelper.assertTokenFor("MAX_VALUE", Token.Type.ID);
        TestHelper.assertTokenFor("isLetterOrDigit", Token.Type.ID);
    }

    @Test
    public void testValidKeywords() throws Exception {
        TestHelper.assertTokenFor("if", Token.Type.IF);
        TestHelper.assertTokenFor("implements", Token.Type.IMPLEMENTS);
    }

    @Test
    public void testValidIntLiterals() throws Exception {
        TestHelper.assertTokenFor("0", Token.Type.DECIMAL_INTEGER_LITERAL);
        TestHelper.assertTokenFor("8", Token.Type.DECIMAL_INTEGER_LITERAL);
        TestHelper.assertTokenFor("999990", Token.Type.DECIMAL_INTEGER_LITERAL);
    }
}
