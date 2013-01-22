package cs444.lexer;

import org.junit.Test;

import cs444.TestHelper;

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

    @Test(expected = LexerException.class)
    public void testTraditionalCommentsWithoutClosing() throws Exception {
    	TestHelper.scanString("/* some comment that never ends");
    }

    @Test
    public void testValidEndOfLineComment() throws Exception {
        TestHelper.assertTokenFor("// some comment", Token.Type.END_LINE_COMMENT);
    }

    @Test
    public void testValidIdentifiers() throws Exception {
        TestHelper.assertTokenFor("String", Token.Type.ID);
        TestHelper.assertTokenFor("ints", Token.Type.ID);
        TestHelper.assertTokenFor("i3", Token.Type.ID);
        TestHelper.assertTokenFor("a", Token.Type.ID);
        TestHelper.assertTokenFor("MAX_VALUE", Token.Type.ID);
        TestHelper.assertTokenFor("isLetterOrDigit", Token.Type.ID);
        TestHelper.assertTokenFor("$isValidAsWell", Token.Type.ID);
        TestHelper.assertTokenFor("$", Token.Type.ID);
        TestHelper.assertTokenFor("_", Token.Type.ID);
    }

    @Test
    public void testIlegalIdentifier() throws Exception {
		Lexer scanner = TestHelper.getScannerFor("1a"); // id must not start with number

		TestHelper.assertToken(Token.Type.DECIMAL_INTEGER_LITERAL, "1", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.ID, "a", scanner.getNextToken());
    }

    @Test
    public void testValidIntLiterals() throws Exception {
    	TestHelper.assertTokenFor("0", Token.Type.DECIMAL_INTEGER_LITERAL);
    	TestHelper.assertTokenFor("8", Token.Type.DECIMAL_INTEGER_LITERAL);
    	TestHelper.assertTokenFor("999990", Token.Type.DECIMAL_INTEGER_LITERAL);
    	TestHelper.assertTokenFor("1234567890", Token.Type.DECIMAL_INTEGER_LITERAL);
    }

    @Test
    public void testIlegalIntLiterals() throws Exception {
    	Lexer scanner = TestHelper.getScannerFor("01"); // literal must not start with 0

		TestHelper.assertToken(Token.Type.DECIMAL_INTEGER_LITERAL, "0", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.DECIMAL_INTEGER_LITERAL, "1", scanner.getNextToken());
    }

    @Test
    public void testValidBooleanLiterals() throws Exception {
    	TestHelper.assertTokenFor("true", Token.Type.TRUE);
    	TestHelper.assertTokenFor("false", Token.Type.FALSE);
    }

    @Test
    public void testValidCharLiterals() throws Exception {
    	TestHelper.assertTokenFor("'a'", Token.Type.CHAR_LITERAL);
    	TestHelper.assertTokenFor("'%'", Token.Type.CHAR_LITERAL);
    	TestHelper.assertTokenFor("'\\t'", Token.Type.CHAR_LITERAL);
    	TestHelper.assertTokenFor("'\\'", Token.Type.CHAR_LITERAL);
    	TestHelper.assertTokenFor("'\\''", Token.Type.CHAR_LITERAL);
    	TestHelper.assertTokenFor("'\\177'", Token.Type.CHAR_LITERAL);
    }

    @Test(expected = LexerException.class)
    public void testIlegalEmptyCharLiteral() throws Exception {
    	TestHelper.scanString("\'\'");
    }

    @Test(expected = LexerException.class)
    public void testCharLiteralWithoutClosingQuote() throws Exception {
    	TestHelper.scanString("\'");
    }

    @Test
    public void testValidStringLiterals() throws Exception {
        TestHelper.assertTokenFor("\"\"", Token.Type.STR_LITERAL);   // empty string
        TestHelper.assertTokenFor("\"This is a string\"", Token.Type.STR_LITERAL); // normal string
        TestHelper.assertTokenFor("\"\\t\"", Token.Type.STR_LITERAL); // escape "\t"
        TestHelper.assertTokenFor("\"\\\"\"", Token.Type.STR_LITERAL); // a string containing " alone
    }

    @Test(expected = LexerException.class)
    public void testStringLiteralWithoutClosingQuote() throws Exception {
    	TestHelper.scanString("\"");
    }

    @Test
    public void testNullLiteral() throws Exception{
        TestHelper.assertTokenFor("null", Token.Type.NULL);
    }

    @Test
    public void testSeparators() throws Exception{
        TestHelper.assertTokenFor("(", Token.Type.LPAREN);
        TestHelper.assertTokenFor(")", Token.Type.RPAREN);
        TestHelper.assertTokenFor("{", Token.Type.LBRACE);
        TestHelper.assertTokenFor("}", Token.Type.RBRACE);
        TestHelper.assertTokenFor("[", Token.Type.LBRACKET);
        TestHelper.assertTokenFor("]", Token.Type.RBRACKET);
        TestHelper.assertTokenFor(";", Token.Type.SEMI);
        TestHelper.assertTokenFor(",", Token.Type.COMMA);
        TestHelper.assertTokenFor(".", Token.Type.DOT);
    }

    @Test
    public void testOperators() throws Exception{
        //Operators
        TestHelper.assertTokenFor("=", Token.Type.BECOMES);
        //arithmetic operators
        TestHelper.assertTokenFor("+", Token.Type.PLUS);
        TestHelper.assertTokenFor("-", Token.Type.MINUS);
        TestHelper.assertTokenFor("*", Token.Type.STAR);
        TestHelper.assertTokenFor("/", Token.Type.SLASH);
        TestHelper.assertTokenFor("%", Token.Type.PCT);

        //comparison operators
        TestHelper.assertTokenFor("<", Token.Type.LT);
        TestHelper.assertTokenFor(">", Token.Type.GT);
        TestHelper.assertTokenFor("<=", Token.Type.LE);
        TestHelper.assertTokenFor(">=", Token.Type.GE);
        TestHelper.assertTokenFor("==", Token.Type.EQ);
        TestHelper.assertTokenFor("!=", Token.Type.NE);

        //eager boolean operators
        TestHelper.assertTokenFor("&", Token.Type.AMPERSAND);
        TestHelper.assertTokenFor("|", Token.Type.PIPE);
        TestHelper.assertTokenFor("!", Token.Type.EXCLAMATION);

        //lazy boolean operators
        TestHelper.assertTokenFor("&&", Token.Type.DAMPERSAND);
        TestHelper.assertTokenFor("||", Token.Type.DPIPE);
    }

    @Test
    public void testValidKeywords() throws Exception {
        TestHelper.assertTokenFor("abstract", Token.Type.ABSTRACT);
        TestHelper.assertTokenFor("boolean", Token.Type.BOOLEAN);
        TestHelper.assertTokenFor("break", Token.Type.BREAK);
        TestHelper.assertTokenFor("byte", Token.Type.BYTE);
        TestHelper.assertTokenFor("case", Token.Type.CASE);
        TestHelper.assertTokenFor("catch", Token.Type.CATCH);
        TestHelper.assertTokenFor("char", Token.Type.CHAR);
        TestHelper.assertTokenFor("class", Token.Type.CLASS);
        TestHelper.assertTokenFor("const", Token.Type.CONST);
        TestHelper.assertTokenFor("continue", Token.Type.CONTINUE);
        TestHelper.assertTokenFor("default", Token.Type.DEFAULT);
        TestHelper.assertTokenFor("do", Token.Type.DO);
        TestHelper.assertTokenFor("double", Token.Type.DOUBLE);
        TestHelper.assertTokenFor("else", Token.Type.ELSE);
        TestHelper.assertTokenFor("extends", Token.Type.EXTENDS);
        TestHelper.assertTokenFor("final", Token.Type.FINAL);
        TestHelper.assertTokenFor("finally", Token.Type.FINALLY);
        TestHelper.assertTokenFor("float", Token.Type.FLOAT);
        TestHelper.assertTokenFor("for", Token.Type.FOR);
        TestHelper.assertTokenFor("goto", Token.Type.GOTO);
        TestHelper.assertTokenFor("if", Token.Type.IF);
        TestHelper.assertTokenFor("implements", Token.Type.IMPLEMENTS);
        TestHelper.assertTokenFor("import", Token.Type.IMPORT);
        TestHelper.assertTokenFor("instanceof", Token.Type.INSTANCEOF);
        TestHelper.assertTokenFor("int", Token.Type.INT);
        TestHelper.assertTokenFor("interface", Token.Type.INTERFACE);
        TestHelper.assertTokenFor("long", Token.Type.LONG);
        TestHelper.assertTokenFor("native", Token.Type.NATIVE);
        TestHelper.assertTokenFor("new", Token.Type.NEW);
        TestHelper.assertTokenFor("package", Token.Type.PACKAGE);
        TestHelper.assertTokenFor("private", Token.Type.PRIVATE);
        TestHelper.assertTokenFor("protected", Token.Type.PROTECTED);
        TestHelper.assertTokenFor("public", Token.Type.PUBLIC);
        TestHelper.assertTokenFor("return", Token.Type.RETURN);
        TestHelper.assertTokenFor("short", Token.Type.SHORT);
        TestHelper.assertTokenFor("static", Token.Type.STATIC);
        TestHelper.assertTokenFor("strictfp", Token.Type.STRICTFP);
        TestHelper.assertTokenFor("super", Token.Type.SUPER);
        TestHelper.assertTokenFor("switch", Token.Type.SWITCH);
        TestHelper.assertTokenFor("synchronized", Token.Type.SYNCHRONIZED);
        TestHelper.assertTokenFor("this", Token.Type.THIS);
        TestHelper.assertTokenFor("throw", Token.Type.THROW);
        TestHelper.assertTokenFor("throws", Token.Type.THROWS);
        TestHelper.assertTokenFor("transient", Token.Type.TRANSIENT);
        TestHelper.assertTokenFor("try", Token.Type.TRY);
        TestHelper.assertTokenFor("void", Token.Type.VOID);
        TestHelper.assertTokenFor("volatile", Token.Type.VOLATILE);
        TestHelper.assertTokenFor("while", Token.Type.WHILE);
    }
}
