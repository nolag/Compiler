package cs444.types.exceptions;

import cs444.CompilerException;

public class ImplicitStaticConversionException extends CompilerException{
    private static final long serialVersionUID = 1L;

    public ImplicitStaticConversionException(String what){
        super("Implicit static conversion for " + what + " is not allowed in JOOS");
    }
}
