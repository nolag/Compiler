package cs444.parser.symbols.ast;

import java.math.BigInteger;

import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class IntegerLiteralSymbol extends ATerminal{
    private static final BigInteger NEG_ONE = BigInteger.valueOf(-1);
    private static final BigInteger MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger MIN = BigInteger.valueOf(Integer.MIN_VALUE);

    public final int intVal;

    public IntegerLiteralSymbol(ATerminal in, boolean isNegative) throws OutOfRangeException{
        super("IntegerLiteral", isNegative ? "-" + in.value : in.value);
        BigInteger bigInt = new BigInteger(value);
        if(isNegative) bigInt = bigInt.multiply(NEG_ONE);
        if(bigInt.compareTo(MIN) == -1 || bigInt.compareTo(MAX) == 1) throw new OutOfRangeException(bigInt);
        intVal = bigInt.intValue();
    }

    public boolean empty() {
        return false;
    }
}
