package cs444.types.exceptions;

import cs444.CompilerException;

public class UndefinedSymbolException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public UndefinedSymbolException(String symbol, String in) {
        super("Undefined symbol " + symbol + " in " + in);
    }
}
