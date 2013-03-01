package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;


public class InterfaceMethodSymbol extends AModifiersOptSymbol{

    public InterfaceMethodSymbol(MethodHeader header, ANonTerminal from)
        throws IllegalModifierException, UnsupportedException {

        super("InterfaceMethod", header.name.value, getModifiersParent(from), header.type);

    }

    private static ANonTerminal getModifiersParent(ANonTerminal from) {
        return (ANonTerminal)from.firstOrDefault("MethodHeader");
    }

    @Override
    public void validate() throws UnsupportedException {
        ImplementationLevel implLvl = getImplementationLevel();
        if(isStatic() || isNative() || implLvl == ImplementationLevel.FINAL)
            throw new UnsupportedException("A static method cannot be final");
        if(hasBody())
            throw new UnsupportedException("An interface method must not have a body");

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
        visitor.visit(this);
    }
}
