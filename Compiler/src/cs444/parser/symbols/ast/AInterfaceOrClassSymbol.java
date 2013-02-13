package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol{

    protected AInterfaceOrClassSymbol(String ruleName, String dclName, ANonTerminal from)
            throws IllegalModifierException, UnsupportedException {

        super(ruleName, dclName, from);
    }

    @Override
    public boolean isCollapsable(){
        return false;
    }

    public abstract boolean isClass();
}
