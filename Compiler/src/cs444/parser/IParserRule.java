package cs444.parser;

import cs444.parser.symbols.factories.ISymbolFactory;

public interface IParserRule {
    ISymbolFactory getStartSymbol();
}
