package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.LinkedList;
import java.util.List;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol {
    public final Iterable<String> impls;
    public final Iterable<NameSymbol> pkgImports;

    public String superName;

    private ConstructorSymbol defaultConstructor;
    private long objectSize = 0;

    protected AInterfaceOrClassSymbol(String ruleName, String dclName, ANonTerminal from,
                                      Iterable<String> impls, List<ISymbol> body,
                                      String superName, Iterable<NameSymbol> pkgImports) throws IllegalModifierException, UnsupportedException {
        super(ruleName, dclName, from, null);
        this.impls = impls;
        children.addAll(body);
        this.superName = superName;
        this.pkgImports = pkgImports;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    public abstract boolean isClass();

    public Iterable<DclSymbol> getFields() {
        List<DclSymbol> fieldSymbols = new LinkedList<DclSymbol>();

        for (ISymbol child : children) {
            if (child instanceof DclSymbol) {
                fieldSymbols.add((DclSymbol) child);
            }
        }

        return fieldSymbols;
    }

    public Iterable<AMethodSymbol> getMethods() {
        List<AMethodSymbol> methodSymbols = new LinkedList<AMethodSymbol>();

        for (ISymbol child : children) {
            if (child instanceof AMethodSymbol) {
                methodSymbols.add((AMethodSymbol) child);
            }
        }

        return methodSymbols;
    }

    public Iterable<AMethodSymbol> getUninheritedMethods() {
        List<AMethodSymbol> methodSymbols = new LinkedList<AMethodSymbol>();

        for (AMethodSymbol method : getMethods()) {
            if ((method instanceof MethodSymbol) && ((MethodSymbol) method).parent == this) {
                methodSymbols.add(method);
            }
        }

        return methodSymbols;
    }

    public Iterable<ConstructorSymbol> getConstructors() {
        List<ConstructorSymbol> constructorSymbols = new LinkedList<ConstructorSymbol>();

        for (ISymbol child : children) {
            if (child instanceof ConstructorSymbol) {
                constructorSymbols.add((ConstructorSymbol) child);
            }
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

    public ConstructorSymbol getDefaultConstructor() {
        return defaultConstructor;
    }

    public void setDefaultConstructor(ConstructorSymbol constructor) {
        defaultConstructor = constructor;
    }

    public void computeFieldOffsets(Platform<?, ?> platform) {
        long nextOffset = platform.getObjectLayout().objSize();
        for (DclSymbol fieldDcl : getFields()) {
            if (fieldDcl.isStatic()) {
                continue;
            }

            long offset = fieldDcl.getOffset(platform);
            if (offset != 0 && nextOffset != offset) {
                //Should never get here this is an error!
                System.err.println("DOES NOT ADD UP FOR INHERITING " + fieldDcl.dclName + " in " + dclName);
            }

            // not a field from super:
            fieldDcl.setOffset(nextOffset, platform);
            TypeSymbol ts = fieldDcl.type;
            SizeHelper<?, ?> sizeHelper = platform.getSizeHelper();
            if (ts.isArray) {
                nextOffset += sizeHelper.getDefaultStackSize();
            } else {
                nextOffset += sizeHelper.getByteSizeOfType(fieldDcl.type.value);
            }
        }
        objectSize = nextOffset;
    }

    public long getObjectSize() {
        return objectSize;
    }
}
