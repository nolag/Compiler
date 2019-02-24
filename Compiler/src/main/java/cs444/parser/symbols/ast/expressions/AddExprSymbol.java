package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.*;

public class AddExprSymbol extends BinOpExpr {
    public final static String myName = "Add";

    public AddExprSymbol(ISymbol left, ISymbol right) {
        super(myName, left, right);
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

        if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof INumericLiteral) {
            long val1 = ((INumericLiteral) leftOperand).getAsLongValue();
            long val2 = ((INumericLiteral) rightOperand).getAsLongValue();
            if (rightOperand instanceof LongLiteralSymbol || leftOperand instanceof LongLiteralSymbol) {
                return new LongLiteralSymbol(val1 + val2);
            }
            return new IntegerLiteralSymbol((int) (val1 + val2));
        } else if (rightOperand instanceof StringLiteralSymbol &&
                leftOperand instanceof INumericLiteral) {
            String val1 = getValFrom(leftOperand);
            String val2 = ((StringLiteralSymbol) rightOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof StringLiteralSymbol) {
            String val1 = ((StringLiteralSymbol) leftOperand).strValue;
            String val2 = getValFrom(rightOperand);
            return new StringLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof StringLiteralSymbol &&
                leftOperand instanceof StringLiteralSymbol) {
            String val1 = ((StringLiteralSymbol) leftOperand).strValue;
            String val2 = ((StringLiteralSymbol) rightOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof INumericLiteral) {
            return zeroReducer((INumericLiteral) rightOperand, leftOperand);
        } else if (leftOperand instanceof INumericLiteral) {
            return zeroReducer((INumericLiteral) leftOperand, rightOperand);
        }

        return null;
    }

    private String getValFrom(ISymbol leftOperand) {
        long val1 = ((INumericLiteral) leftOperand).getAsLongValue();
        String strVal = String.valueOf(val1);
        if (leftOperand instanceof CharacterLiteralSymbol) {
            strVal = String.valueOf((char) val1);
        }
        return strVal;
    }
}