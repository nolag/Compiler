public class Token {
    public interface Factory {
        public Token create(String lexeme);
    }
    public enum Id {
        Plus,
        Minus,
        WhiteSpace,
        Number,
        Equals,
        Declaration,
        Identifier,
    }
    public final Id id;
    public final String lexeme;
    public Token(Id id, String lexeme) {
        this.id = id;
        this.lexeme = lexeme;
    }
}
