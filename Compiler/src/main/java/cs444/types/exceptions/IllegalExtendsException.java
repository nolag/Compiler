package cs444.types.exceptions;

import cs444.CompilerException;

public class IllegalExtendsException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public IllegalExtendsException(String name) {
        super(name + "can't be extended");
    }
}
