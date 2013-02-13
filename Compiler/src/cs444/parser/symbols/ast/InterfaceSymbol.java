package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class InterfaceSymbol extends AInterfaceOrClassSymbol{
    public final Iterable<String> supers;

    public InterfaceSymbol(String interfaceName, Iterable<String> supers, ANonTerminal body, ANonTerminal from)
            throws IllegalModifierException, UnsupportedException {

        super("InterfaceDeclaration", interfaceName, from);
        this.supers = supers;
        if(null != body) children.add(body);
    }

    @Override
    public void validate() throws UnsupportedException {
        switch(getProtectionLevel()){
        case PRIVATE:
            throw new UnsupportedException("private interfaces");
        case NOT_VALID:
            throw new UnsupportedException("package declared interfaces");
        default:
            break;
        }
        //Since we don't have nested classes, the class can't be static
        if(isStatic()) throw new UnsupportedException("static interfacse");
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        //We don't support package private interfaces.
        return ProtectionLevel.NOT_VALID;
    }

    @Override
    public ImplementationLevel defaultImplementationLevel() {
        return ImplementationLevel.ABSTRACT;
    }

    @Override
    public boolean isClass() {
        return false;
    }
}
