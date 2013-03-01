package cs444.parser.symbols.exceptions;

import java.math.BigInteger;

import cs444.CompilerException;

public class OutOfRangeException extends CompilerException{
    private static final long serialVersionUID = 1L;

    public OutOfRangeException(BigInteger value){
        super("The value " + value + " is out of range");
    }
}
