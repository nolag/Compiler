package cs444.parser.symbols.ast;

import java.util.LinkedList;
import java.util.List;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.IPlatform;
import cs444.codegen.SizeHelper;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol{
    public final Iterable<String> impls;
    public final Iterable<NameSymbol> pkgImports;

    public String superName;

    private ConstructorSymbol defaultConstructor;
    private long objectSize = 0;

    protected AInterfaceOrClassSymbol(final String ruleName, final String dclName, final ANonTerminal from, final Iterable<String> impls, final List<ISymbol> body,
            final String superName, final Iterable<NameSymbol> pkgImports) throws IllegalModifierException, UnsupportedException {
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
        final List<DclSymbol> fieldSymbols = new LinkedList<DclSymbol>();

        for(final ISymbol child : children){
            if(DclSymbol.class.isInstance(child)) fieldSymbols.add((DclSymbol)child);
        }

        return fieldSymbols;
    }

    public Iterable<AMethodSymbol> getMethods() {
        final List<AMethodSymbol> methodSymbols = new LinkedList<AMethodSymbol>();

        for(final ISymbol child : children){
            if(child instanceof AMethodSymbol) methodSymbols.add((AMethodSymbol)child);
        }

        return methodSymbols;
    }

    public Iterable<AMethodSymbol> getUninheritedMethods() {
        final List<AMethodSymbol> methodSymbols = new LinkedList<AMethodSymbol>();

        for(final AMethodSymbol method : this.getMethods()){
            if ((method instanceof MethodSymbol) && ((MethodSymbol) method).parent == this){
                methodSymbols.add(method);
            }
        }

        return methodSymbols;
    }

    public Iterable<ConstructorSymbol> getConstructors(){
        final List<ConstructorSymbol> constructorSymbols = new LinkedList<ConstructorSymbol>();

        for(final ISymbol child : children){
            if(child instanceof ConstructorSymbol) constructorSymbols.add((ConstructorSymbol)child);
        }

        return constructorSymbols;
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        for (final ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    public void setDefaultConstructor(final ConstructorSymbol constructor) {
        this.defaultConstructor = constructor;
    }

    public ConstructorSymbol getDefaultConstructor() {
        return this.defaultConstructor;
    }

    public void computeFieldOffsets(final IPlatform<?> platform) {
        long nextOffset = platform.getObjectLayout().objSize();
        for (final DclSymbol fieldDcl : this.getFields()) {
            if (fieldDcl.isStatic()) continue;

            if(fieldDcl.getOffset() != 0 && nextOffset != fieldDcl.getOffset()){
                //Should never get here this is an error!
                System.err.println("DOES NOT ADD UP FOR INHERITING " + fieldDcl.dclName + " in " + dclName);
            }

            // not a field from super:
            fieldDcl.setOffset(nextOffset);
            final TypeSymbol ts = fieldDcl.type;
            final SizeHelper<?> sizeHelper = platform.getSizeHelper();
            if(ts.isArray) nextOffset += sizeHelper.getDefaultStackSize();
            else nextOffset += sizeHelper.getByteSizeOfType(fieldDcl.type.value);
        }
        this.objectSize = nextOffset;
    }

    public long getObjectSize(){
        return objectSize;
    }
}
