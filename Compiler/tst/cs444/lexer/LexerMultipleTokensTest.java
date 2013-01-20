package cs444.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

import cs444.TestHelper;

public class LexerMultipleTokensTest {

	@Test
	public void testDetectTokensAfterTraditionalComment() throws Exception {
		String string = "/* some comment */\n" +
				"if";

		Lexer scanner = TestHelper.getScannerFor(string);

		TestHelper.assertToken(Token.Type.COMMENT, "/* some comment */", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHITESPACE, "\n", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.IF, "if", scanner.getNextToken());
		assertNull(scanner.getNextToken());
	}

	@Test
	public void testDetectTwoTraditionalComment() throws Exception {
		String string = "/* some comment */\n" +
				"if /* some other comment */";

		Lexer scanner = TestHelper.getScannerFor(string);

		TestHelper.assertToken(Token.Type.COMMENT, "/* some comment */", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHITESPACE, "\n", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.IF, "if", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHITESPACE, " ", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.COMMENT, "/* some other comment */", scanner.getNextToken());
		assertNull(scanner.getNextToken());
	}

	@Test
	public void testDetectTokenAfterEndOfLineComment() throws Exception {
		String string = "// some comment\n" +
				"while";

		Lexer scanner = TestHelper.getScannerFor(string);

		TestHelper.assertToken(Token.Type.END_LINE_COMMENT, "// some comment\n", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHILE, "while", scanner.getNextToken());
		assertNull(scanner.getNextToken());
	}

	@Test
	public void testDetectTokenAfterStringLiteral() throws Exception {
 		String string = "\"Hello world\" while";

		Lexer scanner = TestHelper.getScannerFor(string);

		TestHelper.assertToken(Token.Type.STR_LITERAL, "\"Hello world\"", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHITESPACE, " ", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHILE, "while", scanner.getNextToken());
		assertNull(scanner.getNextToken());
	}
}
