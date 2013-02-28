package cs444.types.exceptions;

import cs444.CompilerException;

public class DuplicateDeclearationException extends CompilerException{
    private static final long serialVersionUID = 1L;

    public DuplicateDeclearationException(String symbol, String in){
        super("Duplicate symbol " + symbol + " in " + in);
    }
}
