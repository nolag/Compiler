
package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.*;

public class AddExprSymbol extends BinOpExpr {
    public final static String myName = "Add";

    public AddExprSymbol(final ISymbol left, final ISymbol right) {
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
    public TypeableTerminal reduce() {
        final Typeable rightOperand = (Typeable)getRightOperand();
        final Typeable leftOperand = (Typeable)getLeftOperand();

        if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof INumericLiteral){
            final long val1 = ((INumericLiteral)leftOperand).getValue();
            final long val2 = ((INumericLiteral)rightOperand).getValue();
            if(rightOperand instanceof LongLiteralSymbol || leftOperand instanceof LongLiteralSymbol){
                return new LongLiteralSymbol(val1 + val2);
            }
            return new IntegerLiteralSymbol((int)(val1 + val2));
        } else if (rightOperand instanceof StringLiteralSymbol &&
                leftOperand instanceof INumericLiteral){
            final String val1 = getValFrom(leftOperand);
            final String val2 = ((StringLiteralSymbol)rightOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof StringLiteralSymbol){
            final String val1 = ((StringLiteralSymbol)leftOperand).strValue;
            final String val2 = getValFrom(rightOperand);
            return new StringLiteralSymbol(val1 + val2);
        } else if(rightOperand instanceof StringLiteralSymbol &&
                leftOperand instanceof StringLiteralSymbol){
            final String val1 = ((StringLiteralSymbol) leftOperand).strValue;
            final String val2 = ((StringLiteralSymbol) rightOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        }else{
            return null;
        }
    }

    private String getValFrom(final ISymbol leftOperand) {
        final long val1 = ((INumericLiteral)leftOperand).getValue();
        String strVal = String.valueOf(val1);
        if (leftOperand instanceof CharacterLiteralSymbol){
            strVal = String.valueOf((char)val1);
        }
        return strVal;
    }
}