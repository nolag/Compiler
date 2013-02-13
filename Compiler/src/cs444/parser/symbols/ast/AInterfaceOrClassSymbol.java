package cs444.parser.symbols.ast;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol{

    protected AInterfaceOrClassSymbol(String ruleName, String dclName, ANonTerminal from, String type)
            throws IllegalModifierException, UnsupportedException {

        super(ruleName, dclName, from, type);
    }

    @Override
    public boolean isCollapsable(){
        return false;
    }

    public abstract boolean isClass();

    public Iterable<FieldSymbol> getFields(){
        List<FieldSymbol> fieldSymbols = new LinkedList<FieldSymbol>();

        for(ISymbol child : children){
            if(FieldSymbol.class.isInstance(child)) fieldSymbols.add((FieldSymbol)child);
        }

        return fieldSymbols;
    }
}
