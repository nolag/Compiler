package cs444.parser.symbols.ast.factories.expressions;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.LinkedList;
import java.util.List;

// Take care of:
//ClassInstanceCreationExpression:
//new ClassType lparen [ArgumentList] rparen
//
//ArrayCreationExpression:
//new PrimitiveType DimExpr
//new Name DimExpr
public class CreationExprFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException,
            UnsupportedException, IllegalModifierException {
        if (!from.getName().equalsIgnoreCase("ClassInstanceCreationExpression")
                && !from.getName().equalsIgnoreCase("ArrayCreationExpression")) {
            return from;
        }

        ANonTerminal expr = (ANonTerminal) from;

        boolean isArray = haveDimExpression(expr);

        List<ISymbol> args;

        if (isArray) {
            args = new LinkedList<ISymbol>();
            args.add(((ANonTerminal) expr.children.get(2)).children.get(0));
        } else {
            ANonTerminal argumentList = (ANonTerminal) expr.firstOrDefault(JoosNonTerminal.ARGUMENT_LIST);
            args = getArguments(argumentList);
        }

        TypeSymbol type = new TypeSymbol(getTypeValue(expr), isArray, false);
        return new CreationExpression(type, args);
    }

    private String getTypeValue(ANonTerminal expr) {
        ISymbol symbolType = expr.children.get(1);
        ATerminal typeChild;
        if (symbolType.getName().equalsIgnoreCase("ClassType")) {
            ANonTerminal classType = (ANonTerminal) symbolType;
            typeChild = (ATerminal) classType.children.get(0);
        } else {
            typeChild = (ATerminal) symbolType;
        }

        return typeChild.value;
    }

    private List<ISymbol> getArguments(ANonTerminal argumentList) {
        List<ISymbol> args = new LinkedList<ISymbol>();

        if (argumentList != null) {
            for (ISymbol argument : argumentList.children) {
                args.add(argument);
            }
        }
        return args;
    }

    private boolean haveDimExpression(ANonTerminal expr) {
        return expr.children.size() > 2 && expr.children.get(2).getName().equalsIgnoreCase(JoosNonTerminal.DIM_EXPR);
    }
}
