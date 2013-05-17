
package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class OrExprSymbol extends BinOpExpr {
    public final static String myName = "Or";

    public OrExprSymbol(ISymbol left, ISymbol right) {
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

        if (rightOperand instanceof BooleanLiteralSymbol &&
                leftOperand instanceof BooleanLiteralSymbol){
            boolean val1 = ((BooleanLiteralSymbol)leftOperand).boolValue;
            boolean val2 = ((BooleanLiteralSymbol)rightOperand).boolValue;
            return new BooleanLiteralSymbol(val1 || val2);
        }else{
            return null;
        }
    }
}