package cs444.types.exceptions;

import cs444.CompilerException;

public class ForwardReferenceException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public ForwardReferenceException(String field, String forwRef, String in){
        super("In " + in + ", field " + field + " forward reference to " + forwRef + " is used.");
    }
}
