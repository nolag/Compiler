package cs444.parser.symbols.exceptions;

public class UnsupportedException extends Exception{
    private static final long serialVersionUID = 1L;

    public UnsupportedException(String what){
        super(what + " is not supported in JOOS!");
    }
}
