
package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public class EAndExprSymbol extends BinOpExpr {
    public final static String myName = "And";

    public EAndExprSymbol(final ISymbol left, final ISymbol right) {
        super(myName, left, right);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        children.get(0).accept(visitor);
        children.get(1).accept(visitor);
        visitor.visit(this);
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(final CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public Typeable reduce() {
        final ISymbol rightOperand = getRightOperand();
        final ISymbol leftOperand = getLeftOperand();

        if (rightOperand instanceof BooleanLiteralSymbol &&
                leftOperand instanceof BooleanLiteralSymbol) {

            final boolean val1 = ((BooleanLiteralSymbol)leftOperand).boolValue;
            final boolean val2 = ((BooleanLiteralSymbol)rightOperand).boolValue;
            return new BooleanLiteralSymbol(val1 & val2);
        }else if (leftOperand instanceof NotOpExprSymbol && rightOperand instanceof NotOpExprSymbol) {
                return new NotOpExprSymbol(new EOrExprSymbol(leftOperand, rightOperand));
        } else {
            return null;
        }
    }
}