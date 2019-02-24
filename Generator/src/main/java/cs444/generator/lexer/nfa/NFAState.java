package cs444.generator.lexer.nfa;

import cs444.generator.lexer.grammar.TokenMetadata;
import cs444.generator.lexer.nfa.transition.NFATransition;

import java.util.ArrayList;

public class NFAState {

    private final ArrayList<NFATransition> transitions;
    private boolean accepting;
    private TokenMetadata data;
    private NFA parent;

    public NFAState(boolean accepting) {
        this.accepting = accepting;
        transitions = new ArrayList<>();
    }

    public NFAState() {
        this(false);
    }

    public int getId() {
        return parent.getId(this);
    }

    public TokenMetadata getTokenMetadata() {
        return data;
    }

    public void setTokenMetadata(TokenMetadata data) {
        this.data = data;
    }

    public int getPriority() {
        TokenMetadata data = getTokenMetadata();
        return null != data ? data.priority : -1;
    }

    public void setParent(NFA parent) {
        this.parent = parent;
    }

    public boolean isAccepting() {
        return accepting;
    }

    public void setAccepting(boolean accepting) {
        this.accepting = accepting;
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
