package cs444.static_analysis;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.BaseExprSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ConstantExprReducer extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException,
            UnsupportedException, IllegalModifierException {
        if (!(from instanceof BaseExprSymbol)
            && !(from.getName().equals(JoosNonTerminal.BRACKET_PRIMARY))) return from;

        if (from.getName().equals(JoosNonTerminal.BRACKET_PRIMARY)){
            return ((JoosNonTerminal)from).children.get(0);
        }

        ISymbol reduced = ((BaseExprSymbol) from).reduce();
        if (reduced == null){
            return from;
        }else{
            return reduced;
        }
    }
}
