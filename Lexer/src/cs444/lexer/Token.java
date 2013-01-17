package cs444.lexer;

public final class Token {
    public enum Type {
        SWITCH,
        TRY,
        DAMPERSAND,
        AMPERSAND,
        IMPORT,
        IMPLEMENTS,
        COMMA,
        TRANSIENT,
        EXTENDS,
        GT,
        END_LINE_COMMENT,
        CHAR_LITERAL,
        WHITESPACE,
        DPIPE,
        FOR,
        DOT,
        LBRACE,
        PRIVATE,
        ENUM,
        STATIC,
        GE,
        SUPER,
        PUBLIC,
        PCT,
        CHAR,
        FINAL,
        INT,
        MINUS,
        LBRACKET,
        PLUS,
        ABSTRACT,
        DEFAULT,
        CONST,
        THROW,
        DOUBLE,
        BREAK,
        VOLATILE,
        BOOLEAN,
        RPAREN,
        COMMENT,
        CONTINUE,
        RBRACE,
        CATCH,
        ID,
        PROTECTED,
        TRUE,
        NEW,
        LONG,
        BYTE,
        THROWS,
        STRICTFP,
        CASE,
        SHORT,
        NATIVE,
        BECOMES,
        THIS,
        STR_LITERAL,
        NE,
        EQ,
        FALSE,
        ASSERT,
        NULL,
        GOTO,
        FLOAT,
        DO,
        WHILE,
        ELSE,
        PACKAGE,
        EXCLAMATION,
        DECIMAL_INTEGER_LITERAL,
        SEMI,
        RETURN,
        INSTANCEOF,
        PIPE,
        LE,
        FINALLY,
        SYNCHRONIZED,
        RBRACKET,
        LPAREN,
        CLASS,
        SLASH,
        INTERFACE,
        STAR,
        VOID,
        LT
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

