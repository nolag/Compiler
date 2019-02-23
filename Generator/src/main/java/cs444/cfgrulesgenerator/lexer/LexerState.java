package cs444.cfgrulesgenerator.lexer;

public class LexerState {
    private final int[] transitions;
    private final boolean accepting;
    private Token.Type type;
    public LexerState(int[] transitions, boolean accepting) {
        this(transitions, accepting, null);
    }
    public LexerState(int[] transitions, boolean accepting, Token.Type type) {
        this.transitions = transitions;
        this.accepting = accepting;
        this.type = type;
    }
    public final int getNextState(int ch) {
        return transitions[ch];
    }
    public final boolean isAccepting() {
        return accepting;
    }
    public final Token createToken(String lexeme) {
    return new Token(type, lexeme);
    }
}