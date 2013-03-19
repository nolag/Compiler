package cs444.parser.symbols.ast.expressions;

import cs444.parser.symbols.ISymbol;

public abstract class BinOpExpr extends BaseExprSymbol{

    protected BinOpExpr(String name, ISymbol left, ISymbol right) {
        super(name);
        children.add(left);
        children.add(right);
    }

    public ISymbol getLeftOperand(){
        return children.get(0);
    }

    public ISymbol getRightOperand(){
        return children.get(1);
    }
}
