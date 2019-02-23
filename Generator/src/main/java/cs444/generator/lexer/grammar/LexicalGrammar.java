package cs444.generator.lexer.grammar;

import java.util.Collection;
import java.util.HashMap;

import cs444.generator.lexer.dfa.DFA;
import cs444.generator.lexer.dfa.DFAState;
import cs444.generator.lexer.grammar.TokenMetadata.Type;
import cs444.generator.lexer.nfa.NFA;
import cs444.generator.lexer.nfa.NFAState;

public class LexicalGrammar {

    private final HashMap<TokenMetadata, NFA> patterns;
    private int nextPriority;
    private DFA dfa;

    public LexicalGrammar() {
        patterns = new HashMap<TokenMetadata, NFA>();
    }

    public void addPattern(String name, NFA nfa, Type type) {
        TokenMetadata data = new TokenMetadata(name, nextPriority, type);
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
            NFA nfa = NFA.union(false, nfas); 

            System.out.println(nfa.toString());

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
            if (data.priority == priority)
                return data.name;
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
