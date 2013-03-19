package cs444.parser;

import cs444.CompilerException;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public interface IASTBuilder {
    public abstract ISymbol build(ANonTerminal start) throws CompilerException;
}
