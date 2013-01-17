package cs444.grammar;

import java.util.Collection;
import java.util.HashMap;

import cs444.dfa.DFA;
import cs444.dfa.DFAState;
import cs444.nfa.NFA;
import cs444.nfa.NFAState;

public class LexicalGrammar {

    private final HashMap<TokenMetadata, NFA> patterns;
    private int nextPriority;
    private DFA dfa;
    
    public LexicalGrammar() {
        patterns = new HashMap<TokenMetadata, NFA>();
    }
    
    public void addPattern(String name, NFA nfa) {
        TokenMetadata data = new TokenMetadata(name, nextPriority);
        for (NFAState state : nfa.getStates()) {
            state.setTokenMetadata(data);
        }
        patterns.put(data, nfa);
        ++nextPriority;
    }
    
    private DFA getDFA() {
        
        if (null == dfa) {
            
            Collection<NFA> storedNfas = patterns.values();
            NFA[] nfas = new NFA[storedNfas.size()];
            storedNfas.toArray(nfas);
            NFA nfa = NFA.union(nfas);
            
            dfa = new DFA(nfa);
        }
        
        return dfa;
    }

    public TokenMetadata[] getTokenMetadata() {
        TokenMetadata[] result = new TokenMetadata[patterns.size()];
        return patterns.keySet().toArray(result);
    }
    
    public String getTokenName(int priority) {
        for (TokenMetadata data : getTokenMetadata()) {
            if (data.getPriority() == priority)
                return data.getName();
        }
        return null;
    }
    
    public int[] getAcceptTable() {
        
        DFA dfa = getDFA();
        int[] acceptTable = new int[dfa.getSize()];
        
        for (int i = 0; i < dfa.getSize(); i++) {
            DFAState state = dfa.getState(i);
            acceptTable[i] = state.getPriority();
        }
        
        return acceptTable;
    }
    
    public int[][] getStateTable() {
        
        DFA dfa = getDFA();
        int[][] stateTable = new int[dfa.getSize()][];
        
        for (int i = 0; i < dfa.getSize(); i++) {
            DFAState state = dfa.getState(i);
            stateTable[i] = state.getTransitionRow();
        }
        
        return stateTable;
    }
}
