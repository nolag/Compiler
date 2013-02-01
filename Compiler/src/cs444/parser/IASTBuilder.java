package cs444.parser;

import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public interface IASTBuilder {
    Iterable<ASTSymbolFactory> getSimplifcations();
}
