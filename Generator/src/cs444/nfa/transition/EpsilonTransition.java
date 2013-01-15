package cs444.nfa.transition;

import cs444.nfa.NFAState;

public class EpsilonTransition extends NFATransition {

    public EpsilonTransition(NFAState nextState) {
        super(nextState);
    }

    @Override
    public boolean isEpsilon() {
        return true;
    }

    @Override
    public boolean shouldFollow(int ch) {
        return true;
    }

    @Override
    public String toString() {
        return "eps->" + getNextState().getId();
    }

}
