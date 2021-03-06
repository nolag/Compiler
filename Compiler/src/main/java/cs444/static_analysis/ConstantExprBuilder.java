package cs444.static_analysis;

import cs444.CompilerException;
import cs444.parser.IASTBuilder;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public class ConstantExprBuilder implements IASTBuilder {

    @Override
    public ISymbol build(ANonTerminal start) throws CompilerException {

        ASTSymbolFactory astReducer = new ConstantExprReducer();

        return astReducer.convertBottomUp(start);
    }
}
