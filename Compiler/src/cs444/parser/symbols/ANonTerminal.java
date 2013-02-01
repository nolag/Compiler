package cs444.parser.symbols;

import java.util.LinkedList;
import java.util.List;

public abstract class ANonTerminal implements ISymbol{
    public final List<ISymbol> children = new LinkedList<ISymbol>();
}
