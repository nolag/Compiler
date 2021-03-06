package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class LSExprSymbol extends BinOpExpr {
    public final static String myName = "LeftShift";

    public LSExprSymbol(ISymbol left, ISymbol right) {
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
    public TypeableTerminal reduce() {
        ISymbol rightOperand = getRightOperand();
        ISymbol leftOperand = getLeftOperand();

        if (rightOperand instanceof INumericLiteral && leftOperand instanceof INumericLiteral) {
            long val1 = ((INumericLiteral) leftOperand).getAsLongValue();
            int val2 = (int) ((INumericLiteral) rightOperand).getAsLongValue();
            if (leftOperand instanceof LongLiteralSymbol) {
                return new LongLiteralSymbol(val1 << val2);
            }

            return new IntegerLiteralSymbol((int) val1 << val2);
        } else {
            return null;
        }
    }
}