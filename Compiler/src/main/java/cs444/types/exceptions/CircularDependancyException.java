package cs444.types.exceptions;

import cs444.CompilerException;

public class CircularDependancyException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public CircularDependancyException(String name) {
        super("Circular reference with " + name);
    }
}
