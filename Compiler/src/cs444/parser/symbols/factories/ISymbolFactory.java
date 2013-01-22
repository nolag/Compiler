package cs444.parser.symbols.factories;

import cs444.parser.symbols.ISymbol;

public interface ISymbolFactory {
    String getType();
    ISymbol create();
}
