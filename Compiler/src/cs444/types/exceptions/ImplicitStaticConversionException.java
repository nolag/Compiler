package cs444.types.exceptions;

public class ImplicitStaticConversionException extends Exception{
    private static final long serialVersionUID = 1L;

    public ImplicitStaticConversionException(String what){
        super("Implicit static conversion for " + what + " is not allowed in JOOS");
    }
}
