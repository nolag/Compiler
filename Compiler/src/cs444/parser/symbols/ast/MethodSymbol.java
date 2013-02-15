package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;


public class MethodSymbol extends AModifiersOptSymbol{

    public MethodSymbol(String dclName, ANonTerminal from, TypeSymbol type, ANonTerminal body)
        throws IllegalModifierException, UnsupportedException {

        super("Method", dclName, getModifiersParent(from), type);

        if(body != null) children.addAll(body.children);
    }

    private static ANonTerminal getModifiersParent(ANonTerminal from) {
        return (ANonTerminal)from.firstOrDefault("MethodHeader");
    }

    @Override
    public void validate() throws UnsupportedException {
        if(getProtectionLevel() == ProtectionLevel.NOT_VALID)
            throw new UnsupportedException("Package private protection for methods");
        if(isStatic() && getImplementationLevel() == ImplementationLevel.FINAL)
            throw new UnsupportedException("A static method cannot be final");
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
}
