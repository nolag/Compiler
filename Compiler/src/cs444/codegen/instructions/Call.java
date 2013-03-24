package cs444.codegen.instructions;

import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.exceptions.UndeclaredException;

public class Call implements Instruction {
    private final String call;

    public Call(MethodOrConstructorSymbol ms){
        String funcPart = null;
        try {
            funcPart = APkgClassResolver.generateUniqueName(ms, ms.dclName);
        } catch (UndeclaredException e) { /*Should not gest here*/ }
        call = "call " + ms.dclInResolver.fullName.replace('.', '_') + "_func_" + funcPart;
    }

    @Override
    public String generate() {
        return call;
    }
}
