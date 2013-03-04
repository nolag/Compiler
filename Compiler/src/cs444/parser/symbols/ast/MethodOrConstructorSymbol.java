package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.LocalDclLinker;
import cs444.types.PkgClassResolver;

public abstract class MethodOrConstructorSymbol extends AModifiersOptSymbol {

    public PkgClassResolver resolver;

    public final Iterable<DclSymbol> params;

    public MethodOrConstructorSymbol(String nodeName, MethodHeader header,
            ANonTerminal from, ANonTerminal body, TypeSymbol type)
            throws IllegalModifierException, UnsupportedException {
        super(nodeName, header.name.value, from, type);

        this.params = header.params;
        if(body != null) children.addAll(body.children);
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        return ProtectionLevel.NOT_VALID;
    }

    @Override
    public ImplementationLevel defaultImplementationLevel() {
        return ImplementationLevel.NORMAL;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        for (ISymbol param : this.params) {
            param.accept(visitor);
        }

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    private boolean arelocalVarsLinked = false;
    public void resolveLocalVars(String enclosingClassName) throws CompilerException {
        if (arelocalVarsLinked) return;

        this.accept(new LocalDclLinker(enclosingClassName));

        arelocalVarsLinked = true;
    }
}