package cs444.parser.factory;

import java.util.Map;
import java.util.Set;

import cs444.lexer.Token;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.StateTerminal;

public class NonTerminalFactory implements ISymbolFactory{
    private final Map<Integer, Map<Token.Type, StateTerminal>> rules;
    private final Set<Integer> acceptingStates;

    public final String name;

    public NonTerminalFactory(Map<Integer, Map<Token.Type, StateTerminal>> rules, Set<Integer> acceptingStates, String name){
        this.rules = rules;
        this.acceptingStates = acceptingStates;
        this.name = name;
    }

    public NonTerminal create() {
        return new NonTerminal(rules, acceptingStates);
    }

    public String getType() {
        return name;
    }
}
