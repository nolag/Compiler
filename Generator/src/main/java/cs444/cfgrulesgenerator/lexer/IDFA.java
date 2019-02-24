package cs444.cfgrulesgenerator.lexer;

public interface IDFA {
    LexerState getInitialState();

    LexerState getState(int stateId);
}
