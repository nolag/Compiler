package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;

public class CastExpressionSymbol extends BaseExprSymbol {

    public CastExpressionSymbol(TypeSymbol castExprType,
                                ISymbol operandExpression) {
        super("CastExpression");
        setType(castExprType);
        children.add(operandExpression);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    public ISymbol getOperandExpression() {
        return children.get(0);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        getType().accept(visitor);

        for (ISymbol child : children) {
            child.accept(visitor);
        }
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduce() {
        Typeable operand = (Typeable) getOperandExpression();
        String toType = getType().value;

        if (operand instanceof INumericLiteral) {
            INumericLiteral literal = (INumericLiteral) operand;

            if (toType.equals(JoosNonTerminal.LONG)) {
                return new LongLiteralSymbol(literal.getAsLongValue());
            } else if (toType.equals(JoosNonTerminal.INTEGER)) {
                return new IntegerLiteralSymbol((int) literal.getAsLongValue());
            } else if (toType.equals(JoosNonTerminal.SHORT)) {
                return new ShortLiteralSymbol((short) literal.getAsLongValue());
            } else if (toType.equals(JoosNonTerminal.BYTE)) {
                return new ByteLiteralSymbol((byte) literal.getAsLongValue());
            } else if (toType.equals(JoosNonTerminal.CHAR)) {
                return new CharacterLiteralSymbol((char) literal.getAsLongValue());
            }
        }

        return null;
    }
}
