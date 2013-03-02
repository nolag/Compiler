package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;


public class MethodSymbol extends AModifiersOptSymbol{
    public final Iterable<DclSymbol> params;

    public MethodSymbol(String dclName, ANonTerminal modifiersParent, TypeSymbol type, ANonTerminal body, Iterable<DclSymbol> args)
        throws IllegalModifierException, UnsupportedException {
        super("Method", dclName, modifiersParent, type);

        if(body != null) children.addAll(body.children);
        params = args;
    }

    @Override
    public void validate() throws UnsupportedException {
        ImplementationLevel implLvl = getImplementationLevel();
        if(isStatic() && implLvl == ImplementationLevel.FINAL)
            throw new UnsupportedException("A static method cannot be final");

        if(hasBody() && (isNative() || implLvl == ImplementationLevel.ABSTRACT))
        	throw new UnsupportedException("Abstract or Native method with a body");
        if(!hasBody() && !isNative() && implLvl != ImplementationLevel.ABSTRACT)
        	throw new UnsupportedException("Only Abstract or Native methods don't require a body");

        super.validate();
    }

    private boolean hasBody() {
        return children.size() > 0;
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        // We don't support package private members
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

        for (DclSymbol param : this.params) {
            param.accept(visitor);
        }

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    private boolean arelocalVarsLinked = false;
    public boolean areLocalVarLinked() {
        return arelocalVarsLinked;
    }

    public void localVarsLinked() {
        arelocalVarsLinked = true;
    }
}
