package cs444.parser.symbols.ast;

import java.math.BigInteger;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class LongLiteralSymbol extends TypeableTerminal implements INumericLiteral {
    public static final String myName = "LongLiteral";
    private static final BigInteger MAX = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger MIN = BigInteger.valueOf(Long.MIN_VALUE);

    public final long longVal;

    public LongLiteralSymbol(final ATerminal in, final boolean isNegative) throws OutOfRangeException{
        super(myName, isNegative ? "-" + in.value : in.value);
        final BigInteger bigInt = new BigInteger(value.substring(0, value.length() - 1));
        if(bigInt.compareTo(MIN) == -1 || bigInt.compareTo(MAX) == 1) throw new OutOfRangeException(bigInt);
        longVal = bigInt.longValue();
    }

    public LongLiteralSymbol(final long l){
        super(myName, Long.toString(l));
        this.longVal = l;
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(final CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public final long getValue() {
        return longVal;
    }
}
