package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.ByteLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.ShortLiteralSymbol;
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
        ISymbol operand = getOperandExpression();

        if (operand instanceof INumericLiteral){
            INumericLiteral literal = (INumericLiteral) operand;

            if (this.getType().value.equals(JoosNonTerminal.INTEGER)){
                return new IntegerLiteralSymbol(literal.getValue());
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
