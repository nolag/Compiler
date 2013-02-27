package cs444.parser.symbols.ast;

import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;


public class DclSymbol extends AModifiersOptSymbol{
    private final boolean isLocal;


    public DclSymbol(String dclName, ANonTerminal from, TypeSymbol type, ANonTerminal initVal, boolean isLocal)
            throws IllegalModifierException, UnsupportedException {

        super("Dcl", dclName, from, type);
        if(initVal != null) children.addAll(initVal.children);
        this.isLocal = isLocal;
    }

    @Override
    public void validate() throws UnsupportedException {
        ImplementationLevel lvl = getImplementationLevel();
        if(lvl == ImplementationLevel.ABSTRACT )throw new UnsupportedException("abstract fields");

        if(lvl == ImplementationLevel.FINAL ) throw new UnsupportedException("final fields");

        if(type.equals("void"))throw new UnsupportedException("void fields");

        super.validate();
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        // We don't support package private members
        return isLocal ? ProtectionLevel.PRIVATE : ProtectionLevel.NOT_VALID;
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
    public void accept(ISymbolVisitor visitor) {
        visitor.open(this);

        this.type.accept(visitor);

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }
}
