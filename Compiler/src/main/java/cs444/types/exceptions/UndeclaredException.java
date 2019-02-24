package cs444.types.exceptions;

import cs444.CompilerException;

public class UndeclaredException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public UndeclaredException(String what, String in) {
        super("In " + in + ", " + what + " is undeclared.");
    }
}
