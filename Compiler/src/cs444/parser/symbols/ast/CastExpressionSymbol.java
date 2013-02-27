package cs444.parser.symbols.ast;

import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public class CastExpressionSymbol extends ANonTerminal{
    public final ISymbol castExprType;
    public final ISymbol operandExpression;

    public CastExpressionSymbol(String value, ISymbol castExprType,
                                ISymbol operandExpression) {
        super("CastExpression");
        this.castExprType = castExprType;
        this.operandExpression = operandExpression;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
        this.castExprType.accept(visitor);
        this.operandExpression.accept(visitor);
    }
}
