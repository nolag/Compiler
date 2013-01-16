public class Token {
    public interface Factory {
        public Token create(String lexeme);
    }
    public enum Id {
        GE,
        LT,
        STAR,
        RBRACE,
        LBRACE,
        BECOMES,
        AMPERSAND,
        WHITESPACE,
        MINUS,
        ID,
        SEMI,
        LPAREN,
        LBRACKET,
        RPAREN,
        SLASH,
        EXCLAMATION,
        COMMA,
        GT,
        DAMPERSAND,
        PIPE,
        PLUS,
        PCT,
        RBRACKET,
        EQ,
        DOT,
        DPIPE,
        LE,
        NE,
    }
    public final Id id;
    public final String lexeme;
    public Token(Id id, String lexeme) {
        this.id = id;
        this.lexeme = lexeme;
    }
}
