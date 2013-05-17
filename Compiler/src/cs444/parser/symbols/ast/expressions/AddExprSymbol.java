
package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
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
    public void accept(CodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduceToLiteral() {
        ISymbol rightOperand = getRightOperand();
        ISymbol leftOperand = getLeftOperand();

        if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof INumericLiteral){
            int val1 = ((INumericLiteral)leftOperand).getValue();
            int val2 = ((INumericLiteral)rightOperand).getValue();
            return new IntegerLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof StringLiteralSymbol &&
                leftOperand instanceof INumericLiteral){
            String val1 = getValFrom(leftOperand);
            String val2 = ((StringLiteralSymbol)rightOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        } else if (rightOperand instanceof INumericLiteral &&
                leftOperand instanceof StringLiteralSymbol){
            String val1 = ((StringLiteralSymbol)leftOperand).strValue;
            String val2 = getValFrom(rightOperand);
            return new StringLiteralSymbol(val1 + val2);
        } else if(rightOperand instanceof StringLiteralSymbol && 
                leftOperand instanceof StringLiteralSymbol){
            String val1 = ((StringLiteralSymbol) leftOperand).strValue;
            String val2 = ((StringLiteralSymbol) rightOperand).strValue;
            return new StringLiteralSymbol(val1 + val2);
        }else{
            return null;
        }
    }

    private String getValFrom(ISymbol leftOperand) {
        int val1 = ((INumericLiteral)leftOperand).getValue();
        String strVal = String.valueOf(val1);
        if (leftOperand instanceof CharacterLiteralSymbol){
            strVal = String.valueOf((char)val1);
        }
        return strVal;
    }
}