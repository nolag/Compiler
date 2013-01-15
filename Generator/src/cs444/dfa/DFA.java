package cs444.dfa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cs444.nfa.NFA;
import cs444.nfa.NFAState;
import cs444.nfa.NFAStateSet;
import cs444.nfa.transition.NFATransition;

public class DFA {

    private final ArrayList<DFAState> states;
    private final Queue<DFAState> statesToResolve;
    
    public DFA(NFA source) {
        
        System.out.println("Building DFA");
        
        states = new ArrayList<DFAState>();
        statesToResolve = new LinkedList<DFAState>();
        
        createState(source.getStartState().getClosure());
        
        while (!statesToResolve.isEmpty())
            resolveState(statesToResolve.poll());
    }
    
    private DFAState createState(NFAStateSet nfaStates) {
        DFAState state = new DFAState(nfaStates, states.size());
        states.add(state);
        statesToResolve.add(state);
        
        System.out.println("Creating DFA state " + state.toString());
        return state;
    }
    
    private void resolveState(DFAState state) {
        
        System.out.println("Resolving state " + state.toString());

        // for all ascii characters from 0-127 inclusive        
        NFAStateSet[] transitions = new NFAStateSet[128];
        for (int ch = 0; ch < transitions.length; ch++) {
            
            // find all NFA transitions that include the current character
            for (NFAState nfaState : state.getNFAStates()) {
                for (NFATransition transition : nfaState.getTransitions()) {
                    if (!transition.isEpsilon() && transition.shouldFollow(ch)) {
                        
                        // add the destination state to the list of states this character takes us to
                        if (null == transitions[ch])
                            transitions[ch] = new NFAStateSet();
                        
                        transitions[ch].addState(transition.getNextState());
                    }
                }
            }
            
            // if this character takes us anywhere, compute the closure
            // find or make a DFAState corresponding to the computed set of NFA states
            // tell the current state to transition to this new state when character ch is encountered
            if (null != transitions[ch]) {
                
                transitions[ch] = transitions[ch].getClosure();
                DFAState stateToConnect = findOrCreateState(transitions[ch]);
                state.addTransition(ch, stateToConnect);
                
                System.out.println("This state has outgoing edges to " + stateToConnect.toString());
            }
        }
    }
    
    private DFAState findOrCreateState(NFAStateSet nfaStates) {
        
        for (DFAState state : states) {
            if (state.matches(nfaStates))
                return state;
        }
        
        return createState(nfaStates);
    }
    
    @Override
    public String toString() {
        
        String result = "";
        
        // accept table
        
        result += "{ ";
        
        for (DFAState state : states) {
            result += state.isAccepting();
            
            if (state.getId() != states.size() - 1)
                result += ", ";
        }
        
        result += " };\n\n";
        
        // state table
        
        result += "{ ";
        
        for (int i = 0; i < states.size(); i++) {
            
            result += "  { ";
            
            int[] row = states.get(i).getTransitionRow();
            for (int j = 0; j < row.length; j++) {
                
                result += row[j];
                
                if (j < row.length - 1)
                    result += ", ";
            }
            
            result += " }";
            
            if (i < states.size() - 1)
                result += ",\n";
        }
        
        result += " };\n";
        
        return result;
    }
}
