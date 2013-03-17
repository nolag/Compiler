package cs444.parser.symbols.ast;

import java.util.LinkedList;
import java.util.List;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol{
    public final Iterable<String> impls;
    public final String superName;
    public final Iterable<NameSymbol> pkgImports;
    private ConstructorSymbol defaultConstructor;

    protected AInterfaceOrClassSymbol(String ruleName, String dclName, ANonTerminal from, Iterable<String> impls, List<ISymbol> body,
            String superName, Iterable<NameSymbol> pkgImports) throws IllegalModifierException, UnsupportedException {
        super(ruleName, dclName, from, null);
        this.impls = impls;
        children.addAll(body);
        this.superName = superName;
        this.pkgImports = pkgImports;
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

    public Iterable<AMethodSymbol> getMethods() {
        List<AMethodSymbol> methodSymbols = new LinkedList<AMethodSymbol>();

        for(ISymbol child : children){
            if(child instanceof AMethodSymbol) methodSymbols.add((AMethodSymbol)child);
        }

        return methodSymbols;
    }

    public Iterable<AMethodSymbol> getUninheritedMethods() {
        List<AMethodSymbol> methodSymbols = new LinkedList<AMethodSymbol>();

        for(AMethodSymbol method : this.getMethods()){
            if ((method instanceof MethodSymbol) && ((MethodSymbol) method).parent == this){
                methodSymbols.add((AMethodSymbol)method);
            }
        }

        return methodSymbols;
    }

    public Iterable<ConstructorSymbol> getConstructors(){
        List<ConstructorSymbol> constructorSymbols = new LinkedList<ConstructorSymbol>();

        for(ISymbol child : children){
            if(child instanceof ConstructorSymbol) constructorSymbols.add((ConstructorSymbol)child);
        }

        return constructorSymbols;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    public boolean isAbstract(){
        return hasAbstract;
    }

    public void setDefaultConstructor(ConstructorSymbol constructor) {
        this.defaultConstructor = constructor;
    }

    public ConstructorSymbol getDefaultConstructor() {
        return this.defaultConstructor;
    }
}
