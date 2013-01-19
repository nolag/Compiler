package cs444.lexer;

import java.io.IOException;

public interface ILexer {
    public Token getNextToken() throws LexerException, IOException;
}
