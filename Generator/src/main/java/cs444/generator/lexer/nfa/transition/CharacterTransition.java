package cs444.generator.lexer.nfa.transition;

import cs444.generator.lexer.nfa.NFAState;

public class CharacterTransition extends NFATransition {

    private final int ch;
    
    public CharacterTransition(int ch, NFAState nextState) {
        super(nextState);
        
        this.ch = ch;
    }

    @Override
    public boolean isEpsilon() {
        return false;
    }

    @Override
    public boolean shouldFollow(int ch) {
        return this.ch == ch;
    }

    @Override
    public String toString() {
        return "'" + (char)ch + "'->" + getNextState().getId();
    }
}
