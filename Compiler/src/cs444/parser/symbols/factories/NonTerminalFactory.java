package cs444.parser.symbols.factories;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;

public abstract class NonTerminalFactory{

    public final String name;

    public NonTerminalFactory(String name){
        this.name = name;
    }

    public abstract NonTerminal create(ISymbol [] children);

    public String getType() {
        return name;
    }
}
