package cs444.types.exceptions;

import cs444.CompilerException;

public class IllegalInstanceOfException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public IllegalInstanceOfException(String className, String methodName, String from, String to) {
        super("Illegal cast from " + from + " to " + to + " in " + methodName + " " + className);
    }
}
