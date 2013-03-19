package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class CastExpressionSymbol extends BaseExprSymbol{

    public CastExpressionSymbol(TypeSymbol castExprType,
                                ISymbol operandExpression) {
        super("CastExpression");
        this.setType(castExprType);
        this.children.add(operandExpression);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    public ISymbol getOperandExpression(){
        return this.children.get(0);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        this.getType().accept(visitor);

        for (ISymbol child : children) {
            child.accept(visitor);
        }
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduceToLiteral() {
        // ISymbol operand = getOperandExpression();

//        TODO: finish this
        return null;
    }
}
