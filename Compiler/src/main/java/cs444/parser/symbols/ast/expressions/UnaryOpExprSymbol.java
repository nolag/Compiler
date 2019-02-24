package cs444.parser.symbols.ast.expressions;

import cs444.parser.symbols.ISymbol;

public abstract class UnaryOpExprSymbol extends BaseExprSymbol {

    protected UnaryOpExprSymbol(String name, ISymbol on) {
        super(name);
        children.add(on);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    public ISymbol getOperand() {
        return children.get(0);
    }
}
