package cs444.parser.symbols.factories;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;

public class NonTerminalFactory{

    public final String name;

    public NonTerminalFactory(String name){
        this.name = name;
    }

    public NonTerminal create(ISymbol [] children) {
        return new NonTerminal(name, children);
    }

    public String getType() {
        return name;
    }
}
