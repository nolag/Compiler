package cs444.parser.symbols.ast.factories;

import java.util.HashSet;
import java.util.Set;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.CastExpressionSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class CastExpressionFactory extends ASTSymbolFactory{

    private static final String CAST_EXPRESSION = "CastExpression";

    private static final Set<String> validCastTypes= new HashSet<String>();

    static{
        validCastTypes.add("CHAR");
        validCastTypes.add("BYTE");
        validCastTypes.add("SHORT");
        validCastTypes.add("INT");
        validCastTypes.add("BOOLEAN");
        validCastTypes.add("Name");
    }

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase(CAST_EXPRESSION)){
            return from;
        }
        ANonTerminal castExpr = (ANonTerminal) from;

        if (castExpr.children.size() != 2)
            throw new UnsupportedException("Cast expression doesn't have two operands");

        ISymbol castType = castExpr.children.get(0);

        String firstChild = castType.getName();
        if (!validCastTypes.contains(firstChild)){
            throw new UnsupportedException("Something other than Name or primitive type in Cast Expression.");
        }

        ISymbol operandExpression = castExpr.children.get(1);

        return new CastExpressionSymbol(CAST_EXPRESSION, castType, operandExpression);
    }
}
