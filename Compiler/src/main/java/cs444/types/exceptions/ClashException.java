package cs444.types.exceptions;

import cs444.CompilerException;

public class ClashException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public ClashException(String clasher1, String clasher2) {
        super(clasher1 + " clashes with " + clasher2);
    }

}
