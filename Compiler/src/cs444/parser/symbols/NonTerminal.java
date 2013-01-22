package cs444.parser.symbols;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.lexer.Token;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class NonTerminal implements ISymbol{

    private final List<ISymbol> calledChildren = new LinkedList<ISymbol>();
    private final Map<Integer, String> acceptingStates;

    protected final Map<Integer, Map<Token.Type, StateTerminal>> rules;
    protected final String name;
    protected ISymbol currentChild = null;
    protected int state = 0;

    public NonTerminal(Map<Integer, Map<Token.Type, StateTerminal>> rules, Map<Integer, String> acceptingStates, String name){
    	this.rules = rules;
    	this.name = name;
        this.acceptingStates = acceptingStates;
    }

    public boolean giveToken(Token token) throws UnexpectedTokenException {
    	if(currentChild == null){
    		if(setChild(token)) return true;
    		calledChildren.add(currentChild);
    	}

        boolean reducedChild = currentChild.giveToken(token);

        if(reducedChild){
            currentChild = null;
            return giveToken(token);
        }

        return false;
    }

	public String rule() {
	    String display = acceptingStates.get(state);
	    if(display == null) display = name;
	    StringBuilder rule = new StringBuilder();
	    if(display != "")rule.append(display).append(" -> ");

		for(ISymbol child : calledChildren){
		    String childName = child.getName();
		    if(childName == "") continue;
			rule.append(childName).append(" ");
		}

		for(ISymbol child : calledChildren){
			String childRule = child.rule();
			if(childRule.length() != 0){
				rule.append("\n").append(childRule);
			}
		}

		return rule.toString();
	}

	public String getName() {
		return name;
	}

	protected void throwHeper(Token token, Map<Token.Type, StateTerminal> nextStates) throws UnexpectedTokenException{
        StateTerminal [] expected = new StateTerminal[nextStates.size()];
        expected = nextStates.values().toArray(expected);
        throw new UnexpectedTokenException(token, expected);
    }

    protected boolean setChild(Token token) throws UnexpectedTokenException{

        Map<Token.Type, StateTerminal> nextStates = rules.get(state);

        StateTerminal stateTerm = null;

        //reduce or illegal state
        if(!nextStates.containsKey(token.type)){
            if(acceptingStates.containsKey(state)) return true;
            throwHeper(token, nextStates);
        }else{
            stateTerm = nextStates.get(token.type);
        }

        state = stateTerm.nextState;
        //stateTerm.factory is null if it is an intermediate symbol and it is still stuck between 2+ symbols
        if(null != stateTerm.factory) currentChild = stateTerm.factory.create();
        return false;
    }
}
