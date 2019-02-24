package cs444.parser.symbols.exceptions;

import cs444.CompilerException;

import java.math.BigInteger;

public class OutOfRangeException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public OutOfRangeException(BigInteger value) {
        super("The value " + value + " is out of range");
    }
}
