package cs444.generator.lexer.nfa.transition;

import cs444.generator.lexer.nfa.NFAState;

public class MultiRangeTransition extends NFATransition {

    private final Range[] ranges;
    
    public MultiRangeTransition(NFAState nextState, Range... ranges) {
        super(nextState);

        this.ranges = ranges;
    }

    @Override
    public boolean isEpsilon() {
        return false;
    }

    @Override
    public boolean shouldFollow(int ch) {
        
        for (Range range : ranges) {
            if (range.includes(ch))
                return true;
        }
        
        return false;
    }

    @Override
    public String toString() {
        
        String result = "[";
        
        for (Range range : ranges) {
            result += (char)range.from + "-" + (char)range.to;
        }
        
        result += "]->" + getNextState().getId();
        return result;
    }
}
