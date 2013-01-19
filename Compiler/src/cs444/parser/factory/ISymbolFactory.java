package cs444.parser.factory;

import cs444.parser.symbols.ISymbol;

public interface ISymbolFactory {
    String getType();
    ISymbol create();
}
