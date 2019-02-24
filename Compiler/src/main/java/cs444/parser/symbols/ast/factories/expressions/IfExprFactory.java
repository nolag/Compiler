package cs444.parser.symbols.ast.factories.expressions;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class IfExprFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if (!JoosNonTerminal.ifs.contains(from.getName())) {
            return from;
        }

        ANonTerminal nonTerms = (ANonTerminal) from;

        ISymbol condition;
        ISymbol body1;

        condition = nonTerms.children.get(1);
        body1 = nonTerms.children.get(2);

        if (from.getName().equals(JoosNonTerminal.IF_THEN_STATEMENT)) {
            return new IfExprSymbol(condition, body1);
        } else {
            return new IfExprSymbol(condition, body1, nonTerms.children.get(4));
        }
    }
}
