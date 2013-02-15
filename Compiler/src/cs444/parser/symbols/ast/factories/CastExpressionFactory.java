package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class CastExpressionFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase("CastExpression")){
            return from;
        }
        ANonTerminal castExpr = (ANonTerminal) from;

        String firstChild = castExpr.children.get(0).getName();
        if (!firstChild.equalsIgnoreCase("char") &&
            !firstChild.equalsIgnoreCase("byte") &&
            !firstChild.equalsIgnoreCase("short") &&
            !firstChild.equalsIgnoreCase("int") &&
            !firstChild.equalsIgnoreCase("boolean") &&
            !firstChild.equalsIgnoreCase("Name")){
            throw new UnsupportedException("Something other than Name or primitive type in Cast Expression.");
        }
        return from;
    }
}
