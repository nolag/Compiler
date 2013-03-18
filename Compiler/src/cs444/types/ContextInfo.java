package cs444.types;

import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.types.exceptions.UndeclaredException;

public class ContextInfo {
    private MethodOrConstructorSymbol currentMC;
    public final String enclosingClassName;

    public ContextInfo(String enclosingClassName) {
        this.enclosingClassName = enclosingClassName;
    }

    public MethodOrConstructorSymbol getCurrentMC() {
        return currentMC;
    }

    public void setCurrentMC(MethodOrConstructorSymbol currentMC) {
        this.currentMC = currentMC;
    }

    public String getMethodName() throws UndeclaredException {
        return APkgClassResolver.generateUniqueName(this.getCurrentMC(), this.getCurrentMC().dclName);
    }
}