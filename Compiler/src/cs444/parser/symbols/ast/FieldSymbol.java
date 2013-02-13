package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class FieldSymbol extends AModifiersOptSymbol{

    public FieldSymbol(String dclName, ANonTerminal from) throws IllegalModifierException, UnsupportedException {
        super("FieldDecl", dclName, from);
    }

    @Override
    public void validate() throws UnsupportedException {
        //Can't be final for some reason and Java does not allow Abstract fields
        ImplementationLevel implLvl =  getImplementationLevel();
        if (implLvl != ImplementationLevel.NORMAL) throw new UnsupportedException(implLvl.toString() + " is not supported on fields");
        ProtectionLevel protectionLevel = getProtectionLevel();
        if(protectionLevel == ProtectionLevel.PRIVATE)throw new UnsupportedException("private fields are not supported on fields");
        if(protectionLevel == ProtectionLevel.NOT_VALID) throw new  UnsupportedException("package private fields are not supported on fields");
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
}
