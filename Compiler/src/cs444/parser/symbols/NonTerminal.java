package cs444.parser.symbols;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs444.lexer.Token;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class NonTerminal implements ISymbol{

    private final Map<Integer, Map<Token.Type, StateTerminal>> rules;
    private final List<ISymbol> calledChildren = new LinkedList<ISymbol>();
    private final Set<Integer> acceptingStates;

    private int state = 0;
    private ISymbol currentChild = null;

    public NonTerminal(Map<Integer, Map<Token.Type, StateTerminal>> rules, Set<Integer> acceptingStates){
        this.rules = rules;
        this.acceptingStates = acceptingStates;
    }

    private void throwHeper(Token token, Map<Token.Type, StateTerminal> nextStates) throws UnexpectedTokenException{
        StateTerminal [] expected = new StateTerminal[nextStates.size()];
        expected = nextStates.values().toArray(expected);
        throw new UnexpectedTokenException(token, expected);
    }

    public boolean giveToken(Token token) throws UnexpectedTokenException {
        if(currentChild == null){

            Map<Token.Type, StateTerminal> nextStates = rules.get(state);

            StateTerminal stateTerm = null;

            //reduce or illegal state
            if(!nextStates.containsKey(token.getType())){
                if(acceptingStates.contains(state)) {
                    return true;
                }
                //For empty
                if(nextStates.containsKey(Token.Type.EMPTY)){
                    stateTerm = nextStates.get(Token.Type.EMPTY);
                    state = stateTerm.nextState;
                    return giveToken(token);
                }else{
                    throwHeper(token, nextStates);
                }
            }else{
                stateTerm = nextStates.get(token.getType());
            }

            state = stateTerm.nextState;
            currentChild = stateTerm.factory.create();
            calledChildren.add(currentChild);
        }

        boolean reducedChild = currentChild.giveToken(token);

        if(reducedChild){
            currentChild = null;
            return giveToken(token);
        }

        return false;
    }
}
