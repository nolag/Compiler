package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.exceptions.OutOfRangeException;

import java.math.BigInteger;

public class IntegerLiteralSymbol extends TypeableTerminal implements INumericLiteral {
    public static final String myName = "IntegerLiteral";
    private static final BigInteger MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger MIN = BigInteger.valueOf(Integer.MIN_VALUE);

    public final int intVal;

    public IntegerLiteralSymbol(ATerminal in, boolean isNegative) throws OutOfRangeException {
        super(myName, isNegative ? "-" + in.value : in.value);
        BigInteger bigInt = new BigInteger(value);
        if (bigInt.compareTo(MIN) == -1 || bigInt.compareTo(MAX) == 1) {
            throw new OutOfRangeException(bigInt);
        }
        intVal = bigInt.intValue();
    }

    public IntegerLiteralSymbol(int intVal) {
        super(myName, Integer.toString(intVal));
        this.intVal = intVal;
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
        return intVal;
    }
}
