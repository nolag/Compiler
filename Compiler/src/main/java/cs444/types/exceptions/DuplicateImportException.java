package cs444.types.exceptions;

import cs444.CompilerException;

public class DuplicateImportException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public DuplicateImportException(String symbol, String in){
        super("Duplicate import of " + symbol + " in " + in);
    }
}
