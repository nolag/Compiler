package cs444.generator.lexer.nfa.transition;

import cs444.generator.lexer.nfa.NFAState;

public class RangeTransition extends NFATransition {

    private final Range range;
    
    public RangeTransition(int from, int to, NFAState nextState) {
        super(nextState);
        
        range = new Range(from, to);
    }

    @Override
    public boolean isEpsilon() {
        return false;
    }

    @Override
    public boolean shouldFollow(int ch) {
        return range.includes(ch);
    }
    
    @Override
    public String toString() {
        return "[" + (char)range.from + "-" + (char)range.to + "]->" + getNextState().getId();
    }
}
