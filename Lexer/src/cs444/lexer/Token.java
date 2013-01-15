package cs444.lexer;

public class Token {

    public enum Type {
        Number,
        Declaration,
        Identifier,
        Equals,
        Plus,
        Minus,
        WhiteSpace,
        EndOfFile
    }
    
    private final Type id;
    private final String lexeme;
    
	public Token(Type id, String lexeme) {
	    
	    this.id = id;
	    this.lexeme = lexeme;
	}

	public Token(Type id) {
	    this(id, null);
	}
	
    public Type getId() {
        return id;
    }

    public String getLexeme() {
        return lexeme;
    }
    
    @Override
    public String toString() {
        
        return "<" + getLexeme() + ">";
    }
}
