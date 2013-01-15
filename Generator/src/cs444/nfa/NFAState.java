package cs444.nfa;

import java.util.ArrayList;

import cs444.nfa.transition.NFATransition;

public class NFAState {

    private final boolean accepting;
    private final ArrayList<NFATransition> transitions;
    private NFA parent;
    
    public NFAState(boolean accepting) {
        
        this.accepting = accepting;
        
        transitions = new ArrayList<NFATransition>();
    }
    
    public NFAState() {
        this(false);
    }
    
    public int getId() {
        return parent.getId(this);
    }
    
    public void setParent(NFA parent) {
        this.parent = parent;
    }
    
    public boolean isAccepting() {
        return accepting;
    }
    
    public void addTransition(NFATransition transition) {
        transitions.add(transition);
    }
    
    public Iterable<NFATransition> getTransitions() {
        return transitions;
    }
    
    public NFAStateSet getClosure() {
        
        NFAStateSet result = new NFAStateSet();
        result.addState(this);
        
        return result.getClosure();
    }
    
    @Override
    public String toString() {
        return "State(" + getId() + ", " + isAccepting() + ")";
    }
}
