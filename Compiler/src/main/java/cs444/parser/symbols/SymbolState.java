package cs444.parser.symbols;

import cs444.parser.symbols.factories.NonTerminalFactory;

public class SymbolState {

    public final NonTerminalFactory factory;
    public final int to;
    public final int numRed;

    private SymbolState(NonTerminalFactory factory, int to, int numRed) {
        this.factory = factory;
        this.to = to;
        this.numRed = numRed;
    }

    public SymbolState(NonTerminalFactory factory, int numRed) {
        this(factory, 0, numRed);
    }

    public SymbolState(int to) {
        this(null, to, 0);
    }

    public boolean shouldReduce() {
        return factory != null;
    }
}
