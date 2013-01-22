package cs444.parser.symbols.factories;

import java.util.Map;

import cs444.lexer.Token;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.StateTerminal;

public class NonTerminalFactory implements ISymbolFactory{
    private final Map<Integer, Map<Token.Type, StateTerminal>> rules;
    private final Map<Integer, String> acceptingStates;

    public final String name;

    public NonTerminalFactory(Map<Integer, Map<Token.Type, StateTerminal>> rules, Map<Integer, String> acceptingStates, String name){
        this.rules = rules;
        this.acceptingStates = acceptingStates;
        this.name = name;
    }

    public NonTerminal create() {
        return new NonTerminal(rules, acceptingStates, name);
    }

    public String getType() {
        return name;
    }
}
