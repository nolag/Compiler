package cs444.lexer;

import java.io.IOException;

public interface ILexer {
    Token getNextToken() throws LexerException, IOException;
}
