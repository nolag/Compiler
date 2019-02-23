package cs444.parser.symbols.ast.factories.expressions;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ReturnExprFractory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase(JoosNonTerminal.RETURN)) return from;
        ANonTerminal nonTerm = (ANonTerminal) from;
        ISymbol retExpr = nonTerm.children.isEmpty() ? null : nonTerm.children.get(0);
        return new ReturnExprSymbol(retExpr);
    }

}
