package cs444.static_analysis;

import cs444.parser.IASTBuilder;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.exceptions.InvalidFileNameException;

public class ConstantExprBuilder implements IASTBuilder {

    @Override
    public ISymbol build(ANonTerminal start) throws OutOfRangeException, UnsupportedException,
            IllegalModifierException, InvalidFileNameException {

        ASTSymbolFactory astReducer = new ConstantExprReducer();

        return astReducer.convertBottomUp(start);
    }
}
