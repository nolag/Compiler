package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ArrayAccessFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase(JoosNonTerminal.ARRAY_ACCESS)) return from;
        ANonTerminal nonTerm = (ANonTerminal) from;
        return new ArrayAccessExprSymbol(nonTerm.children.get(0), nonTerm.children.get(1));
    }

}
