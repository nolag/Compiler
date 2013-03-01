package cs444.lexer;

import cs444.CompilerException;

public class LexerException extends CompilerException {
	private static final long serialVersionUID = -2043204283971930316L;
	
    public LexerException(String message) {
    	super(message);
	}
}
