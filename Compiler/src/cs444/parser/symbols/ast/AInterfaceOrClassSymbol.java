package cs444.parser.symbols.ast;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol{
    public final Iterable<String> impls;
    public final String superName;

    protected AInterfaceOrClassSymbol(String ruleName, String dclName, ANonTerminal from, Iterable<String> impls, List<ISymbol> body, String superName)
            throws IllegalModifierException, UnsupportedException {
        super(ruleName, dclName, from, null);
        this.impls = impls;
        children.addAll(body);
        this.superName = superName;
    }

    @Override
    public boolean isCollapsable(){
        return false;
    }

    public abstract boolean isClass();

    public Iterable<DclSymbol> getFields(){
        List<DclSymbol> fieldSymbols = new LinkedList<DclSymbol>();

        for(ISymbol child : children){
            if(DclSymbol.class.isInstance(child)) fieldSymbols.add((DclSymbol)child);
        }

        return fieldSymbols;
    }

    public Iterable<MethodSymbol> getMethods() {
        List<MethodSymbol> methodSymbols = new LinkedList<MethodSymbol>();

        for(ISymbol child : children){
            if(MethodSymbol.class.isInstance(child)) methodSymbols.add((MethodSymbol)child);
        }

        return methodSymbols;
    }

    public Iterable<ConstructorSymbol> getConstructors(){
        List<ConstructorSymbol> constructorSymbols = new LinkedList<ConstructorSymbol>();

        for(ISymbol child : children){
            if(ConstructorSymbol.class.isInstance(child)) constructorSymbols.add((ConstructorSymbol)child);
        }

        return constructorSymbols;
    }
}
