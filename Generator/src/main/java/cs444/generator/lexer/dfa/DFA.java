package cs444.generator.lexer.dfa;

import cs444.generator.lexer.nfa.NFA;
import cs444.generator.lexer.nfa.NFAState;
import cs444.generator.lexer.nfa.NFAStateSet;
import cs444.generator.lexer.nfa.transition.NFATransition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DFA {
    private final ArrayList<DFAState> states;
    private final Queue<DFAState> statesToResolve;

    public DFA(NFA source) {
        states = new ArrayList<>();
        statesToResolve = new LinkedList<>();
        createState(source.getStartState().getClosure());
        while (!statesToResolve.isEmpty()) {
            resolveState(statesToResolve.poll());
        }
    }

    public int getSize() {
        return states.size();
    }

    public DFAState getState(int id) {
        return states.get(id);
    }

    private DFAState createState(NFAStateSet nfaStates) {
        DFAState state = new DFAState(nfaStates, states.size());
        states.add(state);
        statesToResolve.add(state);
        return state;
    }

    private void resolveState(DFAState state) {
        // for all ascii characters from 0-127 inclusive        
        NFAStateSet[] transitions = new NFAStateSet[128];
        for (int ch = 0; ch < transitions.length; ch++) {

            // find all NFA transitions that include the current character
            for (NFAState nfaState : state.getNFAStates()) {
                for (NFATransition transition : nfaState.getTransitions()) {
                    if (!transition.isEpsilon() && transition.shouldFollow(ch)) {

                        // add the destination state to the list of states this character takes us to
                        if (null == transitions[ch]) {
                            transitions[ch] = new NFAStateSet();
                        }

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
            }
        }
    }

    private DFAState findOrCreateState(NFAStateSet nfaStates) {
        for (DFAState state : states) {
            if (state.matches(nfaStates)) {
                return state;
            }
        }

        return createState(nfaStates);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        // accept table
        result.append("{ ");

        for (DFAState state : states) {
            result.append(state.isAccepting());

            if (state.getId() != states.size() - 1) {
                result.append(", ");
            }
        }

        result.append(" };\n\n\"{ \"");

        // state table
        for (int i = 0; i < states.size(); i++) {

            result.append("  { ");

            int[] row = states.get(i).getTransitionRow();
            for (int j = 0; j < row.length; j++) {
                result.append(row[j]);
                if (j < row.length - 1) {
                    result.append(", ");
                }
            }

            result.append(" }");

            if (i < states.size() - 1) {
                result.append(",\n");
            }
        }

        result.append(" };\n");
        return result.toString();
    }
}
