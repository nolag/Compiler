package cs444.types.exceptions;

import cs444.CompilerException;

public class UnimplementedException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public UnimplementedException(String what, String in) {
        super("In " + in + ", " + what + " is unimplemented.");
    }
}
