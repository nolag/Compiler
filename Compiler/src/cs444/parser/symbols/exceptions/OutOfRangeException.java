package cs444.parser.symbols.exceptions;

import java.math.BigInteger;

import cs444.lexer.Token;

public class OutOfRangeException extends Exception{
    private static final long serialVersionUID = 1L;

    public OutOfRangeException(Token t, BigInteger value){
        super("The value " + value + " is out of range");
    }
}
