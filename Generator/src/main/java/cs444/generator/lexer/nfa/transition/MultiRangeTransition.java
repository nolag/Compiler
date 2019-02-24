package cs444.generator.lexer.nfa.transition;

import cs444.generator.lexer.nfa.NFAState;

import java.util.Arrays;

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
        return Arrays.stream(ranges).anyMatch(r -> r.includes(ch));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (Range range : ranges) {
            result.append((char) range.from).append("-").append((char) range.to);
        }

        return result.append("]->").append(getNextState().getId()).toString();
    }
}
