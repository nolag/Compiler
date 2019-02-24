package cs444.types.exceptions;

import cs444.CompilerException;

public class BadOperandsTypeException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public BadOperandsTypeException(String className, String methodName, String type1, String type2) {
        super("Bad operands types " + type1 + " to " + type2 + " in " + methodName + " " + className);
    }
}
