package cs444.parser;

import cs444.parser.factory.ISymbolFactory;

public interface IParserRule {
    ISymbolFactory getStartSymbol();
}
