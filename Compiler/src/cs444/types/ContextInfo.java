package cs444.types;

import cs444.parser.symbols.ast.AModifiersOptSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.types.exceptions.UndeclaredException;

public class ContextInfo {
    private AModifiersOptSymbol currentMember;
    public final String enclosingClassName;

    public ContextInfo(String enclosingClassName) {
        this.enclosingClassName = enclosingClassName;
    }

    public AModifiersOptSymbol getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(AModifiersOptSymbol currentMC) {
        this.currentMember = currentMC;
    }

    public String getMemberName() throws UndeclaredException {
        AModifiersOptSymbol current = this.getCurrentMember();
        if (current instanceof MethodOrConstructorSymbol){
            return APkgClassResolver.generateUniqueName((MethodOrConstructorSymbol) current, current.dclName);
        }else{
            return APkgClassResolver.getUniqueNameFor((DclSymbol) current);
        }
    }

    public boolean insideField() {
        return currentMember instanceof DclSymbol;
    }
}