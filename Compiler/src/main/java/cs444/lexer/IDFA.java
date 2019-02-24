package cs444.lexer;

public interface IDFA {

    LexerState getInitialState();

    LexerState getState(int stateId);
}
