package cs444.types;

import cs444.parser.symbols.ast.AModifiersOptSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.types.exceptions.UndeclaredException;

public class ContextInfo {
    public final String enclosingClassName;
    private AModifiersOptSymbol currentMember;

    public ContextInfo(String enclosingClassName) {
        this.enclosingClassName = enclosingClassName;
    }

    public AModifiersOptSymbol getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(AModifiersOptSymbol currentMC) {
        currentMember = currentMC;
    }

    public String getMemberName() throws UndeclaredException {
        AModifiersOptSymbol current = getCurrentMember();
        if (current instanceof MethodOrConstructorSymbol) {
            return APkgClassResolver.generateUniqueName((MethodOrConstructorSymbol) current, current.dclName);
        } else {
            return APkgClassResolver.getUniqueNameFor((DclSymbol) current);
        }
    }

    public boolean insideField() {
        return currentMember instanceof DclSymbol;
    }
}