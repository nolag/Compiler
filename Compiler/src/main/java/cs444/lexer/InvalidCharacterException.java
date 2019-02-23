package cs444.lexer;

public class InvalidCharacterException extends LexerException {
	private static final long serialVersionUID = 5040721398032936371L;

	public InvalidCharacterException(int ch) {
		super("Lexer encountered invalid character " + ch);
	}
}
