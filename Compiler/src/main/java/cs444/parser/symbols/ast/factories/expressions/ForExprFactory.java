package cs444.parser.symbols.ast.factories.expressions;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ForExprFactory extends ASTSymbolFactory {
    private ISymbol forInit;
    private ISymbol forCondition;
    private ISymbol forUpdate;

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if (!JoosNonTerminal.fors.contains(from.getName())) {
            return from;
        }

        ANonTerminal nonTerms = (ANonTerminal) from;
        nonTerms.children.remove(0);  // remove word "for"

        int lastIndex = nonTerms.children.size() - 1;
        ISymbol body = nonTerms.children.get(lastIndex);
        nonTerms.children.remove(lastIndex);

        forInit = forUpdate = forCondition = null;
        for (ISymbol child : nonTerms.children) {
            extractChild(child);
        }

        if (forInit == null) {
            forInit = new EmptyStatementSymbol();
        }
        if (forUpdate == null) {
            forUpdate = new EmptyStatementSymbol();
        }
        if (forCondition == null) {
            forCondition = new BooleanLiteralSymbol(JoosNonTerminal.TRUE_VALUE);
        }

        return new ForExprSymbol(forInit, forCondition, forUpdate, body);
    }

    private void extractChild(ISymbol child) {
        if (child.getName().equals(JoosNonTerminal.FOR_INIT)) {
            forInit = ((JoosNonTerminal) child).children.get(0);
        } else if (child.getName().equals(JoosNonTerminal.FOR_UPDATE)) {
            forUpdate = ((JoosNonTerminal) child).children.get(0);
        } else {
            forCondition = child;
        }
    }
}
