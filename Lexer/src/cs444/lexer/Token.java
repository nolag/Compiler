package cs444.lexer;

public final class Token {
    public enum Type {
        SWITCH,
        IF,
        TRY,
        AMPERSAND,
        DAMPERSAND,
        IMPLEMENTS,
        IMPORT,
        CHAR_LITERAL,
        COMMA,
        LBRACE,
        END_LINE_COMMENT,
        EXTENDS,
        WHITESPACE,
        FOR,
        DOT,
        TRANSIENT,
        STATIC,
        DPIPE,
        PRIVATE,
        ENUM,
        MINUS,
        SUPER,
        PUBLIC,
        GE,
        PCT,
        INT,
        FINAL,
        CHAR,
        LBRACKET,
        ABSTRACT,
        DEFAULT,
        PLUS,
        CONST,
        THROW,
        BREAK,
        VOLATILE,
        BOOLEAN,
        DOUBLE,
        COMMENT,
        CONTINUE,
        RPAREN,
        RBRACE,
        ID,
        PROTECTED,
        TRUE,
        BYTE,
        STRICTFP,
        NEW,
        CATCH,
        LONG,
        SHORT,
        CASE,
        NATIVE,
        BECOMES,
        THIS,
        STR_LITERAL,
        THROWS,
        NE,
        EQ,
        GOTO,
        FALSE,
        NULL,
        ASSERT,
        FLOAT,
        WHILE,
        ELSE,
        DO,
        PACKAGE,
        EXCLAMATION,
        DECIMAL_INTEGER_LITERAL,
        INSTANCEOF,
        GT,
        SEMI,
        RETURN,
        LE,
        PIPE,
        SYNCHRONIZED,
        FINALLY,
        RBRACKET,
        LPAREN,
        VOID,
        CLASS,
        SLASH,
        LT,
        INTERFACE,
        STAR
    }
    private final Type type;
    private final String lexeme;
    public Token(Type type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }
    public Type getType() {
        return type;
    }
    public String getLexeme() {
        return lexeme;
    }
    @Override
    public String toString() {
        return "<" + type.toString() + ", " + lexeme.trim() + ">";
    }
}
