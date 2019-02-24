package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public class AndExprSymbol extends BinOpExpr {
    public final static String myName = "And";

    public AndExprSymbol(ISymbol left, ISymbol right) {
        super(myName, left, right);
    }

    public static Typeable eval(BooleanLiteralSymbol booleanSymbol, Typeable other) {
        if (booleanSymbol.boolValue) {
            return other;
        }
        return booleanSymbol;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        children.get(0).accept(visitor);
        children.get(1).accept(visitor);
        visitor.visit(this);
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public Typeable reduce() {
        Typeable rightOperand = (Typeable) getRightOperand();
        Typeable leftOperand = (Typeable) getLeftOperand();

        boolean rightBoolean = rightOperand instanceof BooleanLiteralSymbol;
        boolean leftBoolean = leftOperand instanceof BooleanLiteralSymbol;

        if (rightBoolean && leftBoolean) {
            boolean val1 = ((BooleanLiteralSymbol) leftOperand).boolValue;
            boolean val2 = ((BooleanLiteralSymbol) rightOperand).boolValue;
            return new BooleanLiteralSymbol(val1 && val2);
        } else if (leftBoolean) {
            return eval((BooleanLiteralSymbol) leftOperand, rightOperand);
        } else if (leftOperand instanceof NotOpExprSymbol && rightOperand instanceof NotOpExprSymbol) {
            return new NotOpExprSymbol(new OrExprSymbol(leftOperand, rightOperand));
        }
        return null;
    }
}