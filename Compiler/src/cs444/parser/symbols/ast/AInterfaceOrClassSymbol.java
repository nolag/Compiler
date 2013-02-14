package cs444.parser.symbols.ast;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol{

    final Iterable<String> impls;

    protected AInterfaceOrClassSymbol(String ruleName, String dclName, ANonTerminal from, Iterable<String> impls, List<ISymbol> body)
            throws IllegalModifierException, UnsupportedException {

        super(ruleName, dclName, from, null);
        this.impls = impls;
        children.addAll(body);
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
