package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.exceptions.OutOfRangeException;

import java.math.BigInteger;

public class LongLiteralSymbol extends TypeableTerminal implements INumericLiteral {
    public static final String myName = "LongLiteral";
    private static final BigInteger MAX = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger MIN = BigInteger.valueOf(Long.MIN_VALUE);

    public final long longVal;

    public LongLiteralSymbol(ATerminal in, boolean isNegative) throws OutOfRangeException {
        super(myName, isNegative ? "-" + in.value : in.value);
        BigInteger bigInt = new BigInteger(value.substring(0, value.length() - 1));
        if (bigInt.compareTo(MIN) == -1 || bigInt.compareTo(MAX) == 1) {
            throw new OutOfRangeException(bigInt);
        }
        longVal = bigInt.longValue();
    }

    public LongLiteralSymbol(long l) {
        super(myName, Long.toString(l));
        longVal = l;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public final long getAsLongValue() {
        return longVal;
    }
}
