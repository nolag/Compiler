package cs444.parser.symbols.ast.expressions;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;

public abstract class BaseExprSymbol extends ANonTerminal implements Typeable{
    private TypeSymbol type;

    protected BaseExprSymbol(final String name) {
        super(name);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public TypeSymbol getType(){
        return type;
    }

    @Override
    public void setType(final TypeSymbol type){
        this.type = type;
    }

    /**
    *
    * @return reduces to an expression or returns null if it cannot reduce.
    */
    public abstract Typeable reduce();
}
