package cs444.parser;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.exceptions.InvalidFileNameException;

public interface IASTBuilder {
    public abstract ISymbol build(String fileName, ANonTerminal start) throws OutOfRangeException,
    	UnsupportedException, IllegalModifierException, InvalidFileNameException;
}
