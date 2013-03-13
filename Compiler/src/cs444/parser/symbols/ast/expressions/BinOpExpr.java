package cs444.parser.symbols.ast.expressions;

import cs444.parser.symbols.ISymbol;

public abstract class BinOpExpr extends BaseExprSymbol{

    protected BinOpExpr(String name, ISymbol left, ISymbol right) {
        super(name);
        children.add(left);
        children.add(right);
    }
}
