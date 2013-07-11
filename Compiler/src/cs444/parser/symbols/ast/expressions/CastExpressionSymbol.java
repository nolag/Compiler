package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;

public class CastExpressionSymbol extends BaseExprSymbol{

    public CastExpressionSymbol(final TypeSymbol castExprType,
                                final ISymbol operandExpression) {
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
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        this.getType().accept(visitor);

        for (final ISymbol child : children) {
            child.accept(visitor);
        }
        visitor.visit(this);
    }

    @Override
    public void accept(final CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduceToLiteral() {
        final ISymbol operand = getOperandExpression();

        if (operand instanceof INumericLiteral){
            final INumericLiteral literal = (INumericLiteral) operand;

            //TODO long here
            if (this.getType().value.equals(JoosNonTerminal.INTEGER)){
                return new IntegerLiteralSymbol((int)literal.getValue());
            }else if (this.getType().value.equals(JoosNonTerminal.SHORT)){
                return new ShortLiteralSymbol((short)literal.getValue());
            }else if (this.getType().value.equals(JoosNonTerminal.BYTE)){
                return new ByteLiteralSymbol((byte)literal.getValue());
            }else if (this.getType().value.equals(JoosNonTerminal.CHAR)){
                return new CharacterLiteralSymbol((char)literal.getValue());
            }
        }
        return null;
    }
}
