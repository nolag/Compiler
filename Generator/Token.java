public class Token {
    public interface Factory {
        public Token create(String lexeme);
    }
    public enum Id {
        PACKAGE,
        PROTECTED,
        LT,
        CLASS,
        STAR,
        WHILE,
        CONST,
        LBRACE,
        CASE,
        NEW,
        CHAR,
        DO,
        FOR,
        FLOAT,
        ABSTRACT,
        ID,
        BREAK,
        LPAREN,
        LBRACKET,
        RPAREN,
        FINAL,
        IMPORT,
        SLASH,
        SYNCHRONIZED,
        BOOLEAN,
        EXCLAMATION,
        IMPLEMENTS,
        CONTINUE,
        COMMA,
        TRANSIENT,
        THIS,
        RETURN,
        DOUBLE,
        DAMPERSAND,
        PIPE,
        PLUS,
        VOID,
        SUPER,
        EQ,
        RBRACKET,
        GOTO,
        DOT,
        NE,
        GE,
        BYTE,
        RBRACE,
        VOLATILE,
        PRIVATE,
        STATIC,
        SWITCH,
        NULL,
        ELSE,
        DEFAULT,
        STRICTFP,
        BECOMES,
        AMPERSAND,
        NATIVE,
        WHITESPACE,
        THROWS,
        SHORT,
        INT,
        INSTANCEOF,
        MINUS,
        SEMI,
        TRUE,
        ASSERT,
        TRY,
        ENUM,
        FINALLY,
        GT,
        INTERFACE,
        PCT,
        CATCH,
        FALSE,
        LONG,
        PUBLIC,
        EXTENDS,
        DPIPE,
        LE,
        THROW,
    }
    public final Id id;
    public final String lexeme;
    public Token(Id id, String lexeme) {
        this.id = id;
        this.lexeme = lexeme;
    }
}
