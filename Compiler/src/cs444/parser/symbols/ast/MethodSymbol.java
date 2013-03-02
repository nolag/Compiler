package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;


public class MethodSymbol extends MethodOrConstructorSymbol {
    public MethodSymbol(MethodHeader header, ANonTerminal modifiersParent, TypeSymbol type, ANonTerminal body)
        throws IllegalModifierException, UnsupportedException {
        super("Method", header, modifiersParent, body, type);
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
    public boolean isCollapsable() {
        return false;
    }
}
