package cs444.parser.symbols.ast.expressions;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.TypeableTerminal;

public abstract class BaseExprSymbol extends ANonTerminal implements Typeable{
    private TypeSymbol type;

    protected BaseExprSymbol(String name) {
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
    public void setType(TypeSymbol type){
        this.type = type;
    }

    /**
    *
    * @return literal if it can reduce, null otherwise
    */
    public abstract TypeableTerminal reduceToLiteral();
}
