package cs444.cfgrulesgenerator.lexer;

import java.io.IOException;

public interface ILexer {
    Token getNextToken() throws LexerException, IOException;
}
