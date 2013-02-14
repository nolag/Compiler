package cs444.parser.symbols.exceptions;

import java.math.BigInteger;

public class OutOfRangeException extends Exception{
    private static final long serialVersionUID = 1L;

    public OutOfRangeException(BigInteger value){
        super("The value " + value + " is out of range");
    }
}
