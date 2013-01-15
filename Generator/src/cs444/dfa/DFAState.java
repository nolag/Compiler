package cs444.dfa;

import cs444.nfa.NFAState;
import cs444.nfa.NFAStateSet;

public class DFAState {

    private final int id;
    private final NFAStateSet states;
    //private final DFATransitionDefinition transitions;
    private final DFAState[] transitions;
    
    public DFAState(NFAStateSet states, int id) {
        
        this.id = id;
        this.states = states;
        
        //transitions = new DFATransitionDefinition();
        transitions = new DFAState[128];
    }
    
    public int getId() {
        return id;
    }

    public boolean matches(NFAStateSet states) {
        return this.states.equals(states);
    }
    
    public boolean isAccepting() {
        
        for (NFAState state : states.getStates()) {
            if (state.isAccepting())
                return true;
        }
        
        return false;
    }
    
    public Iterable<NFAState> getNFAStates() {
        return states.getStates();
    }
    
    public void addTransition(int ch, DFAState state) {
        if (null == transitions[ch])
            transitions[ch] = state;
    }
    
    public int[] getTransitionRow() {
        
        int[] result = new int[transitions.length];
        
        for (int i = 0; i < transitions.length; i++) {
            if (null != transitions[i])
                result[i] = transitions[i].getId();
            else
                result[i] = -1;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        
        String result = "(" + getId() + " { ";
        
        for (NFAState state : getNFAStates()) {
            result += state.getId() + " ";
        }
        
        result += "} )";
        return result;
    }
}
