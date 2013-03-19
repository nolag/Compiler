
package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

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
    public TypeableTerminal reduceToLiteral() {
        ISymbol rightOperand = getRightOperand();
        ISymbol leftOperand = getLeftOperand();

        if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof INumericLiteral){
            int val1 = ((INumericLiteral)rightOperand).getValue();
            int val2 = ((INumericLiteral)leftOperand).getValue();
            return new IntegerLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof StringLiteralSymbol &&
                leftOperand instanceof INumericLiteral){
            String val1 = ((StringLiteralSymbol)rightOperand).strValue;
            int val2 = ((INumericLiteral)leftOperand).getValue();
            return new StringLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof StringLiteralSymbol){
            int val1 = ((INumericLiteral)rightOperand).getValue();
            String val2 = ((StringLiteralSymbol)leftOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        } else if(rightOperand instanceof StringLiteralSymbol && 
                leftOperand instanceof StringLiteralSymbol){
            String val1 = ((StringLiteralSymbol) rightOperand).strValue;
            String val2 = ((StringLiteralSymbol) leftOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        }else{
            return null;
        }
    }
}