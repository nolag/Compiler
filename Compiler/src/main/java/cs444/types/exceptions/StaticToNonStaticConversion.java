package cs444.types.exceptions;

import cs444.CompilerException;

public class StaticToNonStaticConversion extends CompilerException {
    private static final long serialVersionUID = 1L;

    public StaticToNonStaticConversion(String what) {
        super("Implicit static conversion for " + what + " is not allowed in JOOS");
    }
}
