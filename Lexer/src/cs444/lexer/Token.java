package src.cs444.lexer;

public final class Token {
    public enum Type {
        SWITCH,
        TRY,
        AMPERSAND,
        IF,
        DAMPERSAND,
        IMPLEMENTS,
        IMPORT,
        CHAR_LITERAL,
        COMMA,
        END_LINE_COMMENT,
        LBRACE,
        EXTENDS,
        WHITESPACE,
        DOT,
        FOR,
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
        DEFAULT,
        PLUS,
        CONST,
        ABSTRACT,
        VOLATILE,
        THROW,
        BREAK,
        BOOLEAN,
        DOUBLE,
        RPAREN,
        RBRACE,
        ID,
        COMMENT,
        PROTECTED,
        TRUE,
        BYTE,
        STRICTFP,
        NEW,
        CATCH,
        LONG,
        CONTINUE,
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


