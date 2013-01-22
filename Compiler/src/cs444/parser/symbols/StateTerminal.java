package cs444.parser.symbols;

import cs444.parser.symbols.factories.ISymbolFactory;

public class StateTerminal {

    public final ISymbolFactory factory;
    public final int nextState;

    public StateTerminal(ISymbolFactory factory, int nextState){
        this.factory = factory;
        this.nextState = nextState;
    }
}
