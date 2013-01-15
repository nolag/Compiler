package cs444.grammar;

import java.util.Collection;
import java.util.HashMap;

import cs444.dfa.DFA;
import cs444.nfa.NFA;

public class LexicalGrammar {

    private final HashMap<String, NFA> patterns;
    private DFA dfa;
    
    public LexicalGrammar() {
        patterns = new HashMap<String, NFA>();
    }
    
    public void addPattern(String name, NFA nfa) {
        patterns.put(name, nfa);
    }
    
    public DFA getDFA() {
        
        if (null == dfa) {
            
            Collection<NFA> storedNfas = patterns.values();
            NFA[] nfas = new NFA[storedNfas.size()];
            storedNfas.toArray(nfas);
            NFA nfa = NFA.union(nfas);
            
            dfa = new DFA(nfa);
        }
        
        return dfa;
    }

    public String[] getTokenNames() {
        String[] result = new String[ patterns.size() ];
        return patterns.keySet().toArray(result);
    }
    
    public int getAcceptingState(String name) {
        return 0;
    }
}
