package cs444.types.exceptions;

import cs444.CompilerException;

public class DuplicateDeclarationException extends CompilerException{
    private static final long serialVersionUID = 1L;

    public DuplicateDeclarationException(String symbol, String in){
        super("Duplicate symbol " + symbol + " in " + in);
    }
}
