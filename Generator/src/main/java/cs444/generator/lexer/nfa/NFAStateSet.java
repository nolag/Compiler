package cs444.generator.lexer.nfa;

import cs444.generator.lexer.nfa.transition.NFATransition;

import java.util.ArrayList;
import java.util.HashSet;

public class NFAStateSet {

    private final HashSet<NFAState> states;

    public NFAStateSet() {
        states = new HashSet<>();
    }

    public NFAStateSet(NFAStateSet states) {
        this();
        for (NFAState state : states.getStates()) {
            addState(state);
        }
    }

    public Iterable<NFAState> getStates() {
        return states;
    }

    public void addState(NFAState state) {
        states.add(state);
    }

    public boolean contains(NFAState state) {
        return states.contains(state);
    }

    public NFAStateSet getClosure() {
        NFAStateSet result = new NFAStateSet();

        int size = 0;
        int previousSize = 0;

        for (NFAState state : states) {
            result.addState(state);
            size++;
        }

        while (previousSize != size) {
            previousSize = size;
            ArrayList<NFAState> statesToAdd = new ArrayList<NFAState>();

            for (NFAState state : result.getStates()) {
                for (NFATransition transition : state.getTransitions()) {
                    if (transition.isEpsilon()) {
                        NFAState next = transition.getNextState();
                        statesToAdd.add(next);
                    }
                }
            }

            for (NFAState state : statesToAdd) {
                if (!result.contains(state)) {
                    result.addState(state);
                    size++;
                }
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((states == null) ? 0 : states.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        NFAStateSet other = (NFAStateSet) obj;
        if (states == null) {
            return other.states == null;
        } else return states.equals(other.states);
    }
}
