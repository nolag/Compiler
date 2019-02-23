package cs444.generator.lexer.nfa.transition;

import cs444.generator.lexer.nfa.NFAState;

public abstract class NFATransition {

    private final NFAState nextState;
    
    protected NFATransition(NFAState nextState) {
        this.nextState = nextState;
    }
    
    public NFAState getNextState() {
        return nextState;
    }
    
    public abstract boolean isEpsilon();
    public abstract boolean shouldFollow(int ch);
    
    @Override
    public abstract String toString();
}
