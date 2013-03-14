package cs444.lexer;

import static org.junit.Assert.*;

import java.io.IOException;

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
		TestHelper.assertEndOfInput(scanner);
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
		TestHelper.assertEndOfInput(scanner);
	}

	@Test
	public void testDetectTokenAfterEndOfLineComment() throws Exception {
		String string = "// some comment\n" +
				"while";

		Lexer scanner = TestHelper.getScannerFor(string);

		TestHelper.assertToken(Token.Type.END_LINE_COMMENT, "// some comment\n", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHILE, "while", scanner.getNextToken());
		TestHelper.assertEndOfInput(scanner);
	}

	@Test
	public void testDetectTokenAfterStringLiteral() throws Exception {
 		String string = "\"Hello world\" while";

		Lexer scanner = TestHelper.getScannerFor(string);

		TestHelper.assertToken(Token.Type.STR_LITERAL, "\"Hello world\"", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHITESPACE, " ", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHILE, "while", scanner.getNextToken());
		TestHelper.assertEndOfInput(scanner);
	}
	
	@Test
	public void testEndOfFileLineComment() throws IOException, LexerException {
		String string =
				"public\n" +
			    "// eof comment";
			
		Lexer scanner = TestHelper.getScannerFor(string);
		TestHelper.assertToken(Token.Type.PUBLIC, "public", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHITESPACE, "\n", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.END_LINE_COMMENT, "// eof comment\n", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.EOF, "", scanner.getNextToken());
		assertNull(scanner.getNextToken());
	}
	
	@Test
	public void testEndOfFileTraditionalComment() throws IOException, LexerException {
		String string =
			"public\n" +
		    "/* eof comment */";
		
		Lexer scanner = TestHelper.getScannerFor(string);
		TestHelper.assertToken(Token.Type.PUBLIC, "public", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.WHITESPACE, "\n", scanner.getNextToken());
		TestHelper.assertToken(Token.Type.COMMENT, "/* eof comment */", scanner.getNextToken());
		TestHelper.assertEndOfInput(scanner);
	}
}
