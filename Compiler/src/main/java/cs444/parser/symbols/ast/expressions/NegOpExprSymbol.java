package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public class NegOpExprSymbol extends UnaryOpExprSymbol {
    public static final String myName = "Negative";

    public NegOpExprSymbol(ISymbol on) {
        super(myName, on);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        children.get(0).accept(visitor);
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public Typeable reduce() {
        ISymbol operand = getOperand();
        if (operand instanceof INumericLiteral) {
            long val = ((INumericLiteral) operand).getAsLongValue();
            return operand instanceof LongLiteralSymbol ? new LongLiteralSymbol(-val) :
                    new IntegerLiteralSymbol((int) -val);
        } else if (operand instanceof NegOpExprSymbol) {
            NegOpExprSymbol neg = (NegOpExprSymbol) operand;
            return ((Typeable) neg.getOperand());
        } else {
            return null;
        }
    }
}
