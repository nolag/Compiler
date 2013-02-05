package cs444.parser.symbols.ast;

import java.math.BigInteger;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class IntegerLiteralSymbol implements ISymbol{
    private static final BigInteger NEG_ONE = BigInteger.valueOf(-1);
    private static final BigInteger MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger MIN = BigInteger.valueOf(Integer.MIN_VALUE);

    public  final int value;

    private final String rule;
    private final String name;

    public IntegerLiteralSymbol(Terminal in, boolean isNegative) throws OutOfRangeException{
        BigInteger bigInt = new BigInteger(in.token.lexeme);
        if(isNegative) bigInt = bigInt.multiply(NEG_ONE);
        if(bigInt.compareTo(MIN) == -1 || bigInt.compareTo(MAX) == 1) throw new OutOfRangeException(in.token, bigInt);
        value = bigInt.intValue();
        rule = in.rule();
        name = in.getName();
    }

    public String rule() {
        return rule;
    }

    public String getName() {
        return name;
    }

    public boolean empty() {
        return false;
    }

    public Iterable<ISymbol> getChildren() {
        return Terminal.nochildren;
    }
}
