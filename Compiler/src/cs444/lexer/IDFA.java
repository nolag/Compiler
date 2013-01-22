package cs444.lexer;

public interface IDFA {

	public LexerState getInitialState();

	public LexerState getState(int stateId);

}
