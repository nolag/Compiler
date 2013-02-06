package cs444.cfgrulesgenerator.lexer;

public interface IDFA {

	public LexerState getInitialState();

	public LexerState getState(int stateId);

}
