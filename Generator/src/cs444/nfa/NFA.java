package cs444.nfa;

import java.util.ArrayList;

import cs444.nfa.transition.CharacterTransition;
import cs444.nfa.transition.EpsilonTransition;
import cs444.nfa.transition.MultiRangeTransition;
import cs444.nfa.transition.NFATransition;
import cs444.nfa.transition.Range;
import cs444.nfa.transition.RangeTransition;

public class NFA {

    public static NFA union(NFA... nfas) {
        
        NFA result = new NFA();
        
        NFAState start = result.getStartState();
        NFAState accepting = result.getAcceptingState();
        
        for (NFA nfa : nfas) {
            result.addAllStates(nfa);
            start.addTransition(new EpsilonTransition(nfa.getStartState()));
            nfa.getAcceptingState().addTransition(new EpsilonTransition(accepting));
        }
        
        return result;
    }
    
    public static NFA concatenate(NFA... nfas) {
        
        NFA result = new NFA();
        
        if (nfas.length > 0) {
        
            result.addAllStates(nfas[0]);
            NFAState start = result.getStartState();
            start.addTransition(new EpsilonTransition(nfas[0].getStartState()));
        
            NFA previousNFA = nfas[0];
            for (int i = 1; i < nfas.length; i++) {
                
                NFA currentNFA = nfas[i];
                result.addAllStates(currentNFA);
                previousNFA.getAcceptingState().addTransition(new EpsilonTransition(currentNFA.getStartState()));
                previousNFA = currentNFA;
            }
            
            previousNFA.getAcceptingState().addTransition(new EpsilonTransition(result.getAcceptingState()));
        }
        
        return result;
    }
    
    public static NFA oneOrMore(NFA nfa) {
        
        NFA result = new NFA();
        result.addAllStates(nfa);
        
        NFAState start = result.getStartState();
        start.addTransition(new EpsilonTransition(nfa.getStartState()));
        
        NFAState childAccepting = nfa.getAcceptingState();
        NFAState resultAccepting = result.getAcceptingState();
        childAccepting.addTransition(new EpsilonTransition(resultAccepting));
        resultAccepting.addTransition(new EpsilonTransition(start));
        
        return result;
    }
    
    public static NFA zeroOrMore(NFA nfa) {
        
        NFA result = new NFA();
        result.addAllStates(nfa);
        
        NFAState start = result.getStartState();
        start.addTransition(new EpsilonTransition(nfa.getStartState()));
        start.addTransition(new EpsilonTransition(result.getAcceptingState()));
        
        NFAState childAccepting = nfa.getAcceptingState();
        childAccepting.addTransition(new EpsilonTransition(start));
        childAccepting.addTransition(new EpsilonTransition(result.getAcceptingState()));
        
        return result;
    }
    
    public static NFA digit() {
        
        NFA result = new NFA();
        NFAState start = result.getStartState();
        NFAState accepting = result.getAcceptingState();
        start.addTransition(new RangeTransition('0', '9', accepting));
        
        return result;
    }
    
    public static NFA letter() {
        
        NFA result = new NFA();
        NFAState start = result.getStartState();
        NFAState accepting = result.getAcceptingState();
        start.addTransition(new MultiRangeTransition(accepting,
                new Range('A', 'Z'), new Range('a', 'z')));

        // '_' and '$' are also considered letters in Java
        // (http://docs.oracle.com/javase/specs/jls/se5.0/html/lexical.html#3.8)
        start.addTransition(new CharacterTransition('_', accepting));
        start.addTransition(new CharacterTransition('$', accepting));
        
        return result;
    }
    
    public static NFA literal(String literal) {
        
        NFA result = new NFA();
        
        NFAState currentState = result.getStartState();
        for (int i = 0; i < literal.length(); i++) {
            
            NFAState nextState = result.createState();
            currentState.addTransition(new CharacterTransition(literal.charAt(i), nextState));
            currentState = nextState;
        }
        
        NFAState acceptingState = result.getAcceptingState();
        currentState.addTransition(new EpsilonTransition(acceptingState));
        
        return result;
    }
    
    private final ArrayList<NFAState> states;
    
    private NFA() {
        states = new ArrayList<NFAState>();
        createState();
        createAcceptingState();
    }
    
    private void createAcceptingState() {
        NFAState state = new NFAState(true);
        addState(state);
    }
    
    public NFAState getStartState() {
        return getState(0);
    }

    public NFAState getAcceptingState() {
        return getState(1);
    }
    
    public NFAState getState(int id) {
        return states.get(id);
    }
    
    private Iterable<NFAState> getStates() {
        return states;
    }
    
    public NFAState createState() {
        NFAState state = new NFAState();
        addState(state);
        return state;
    }
    
    private void addState(NFAState state) {
        state.setParent(this);
        states.add(state);
    }
    
    private void addAllStates(NFA nfa) {
        for (NFAState state : nfa.getStates()) {
            addState(state);
        }
    }
    
    public int getId(NFAState state) {
        return states.indexOf(state);
    }
    
    @Override
    public String toString() {
        
        String result = "";
        
        for (NFAState state : states) {
            
            result += state.toString() + "\n";
            
            for (NFATransition transition : state.getTransitions()) {
                result += "\t" + transition.toString() + "\n";
            }
        }
        
        return result;
    }
}
