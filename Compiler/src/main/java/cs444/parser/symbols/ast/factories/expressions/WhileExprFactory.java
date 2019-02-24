package cs444.parser.symbols.ast.factories.expressions;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class WhileExprFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if (!JoosNonTerminal.whiles.contains(from.getName())) {
            return from;
        }

        ANonTerminal nonTerms = (ANonTerminal) from;
        nonTerms.children.remove(0);  // remove word "while"
        return new WhileExprSymbol(nonTerms.children);
    }
}
