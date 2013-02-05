package cs444.parser;

import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public interface IASTBuilder {
    public abstract Iterable<ASTSymbolFactory> getSimplifcations();
}