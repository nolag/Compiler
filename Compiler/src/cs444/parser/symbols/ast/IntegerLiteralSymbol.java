package cs444.parser.symbols.ast;

import java.math.BigInteger;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class IntegerLiteralSymbol extends TypeableTerminal implements INumericLiteral {
    public static final String myName = "IntegerLiteral";
    private static final BigInteger MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger MIN = BigInteger.valueOf(Integer.MIN_VALUE);

    public final int intVal;

    public IntegerLiteralSymbol(final ATerminal in, final boolean isNegative) throws OutOfRangeException{
        super(myName, isNegative ? "-" + in.value : in.value);
        final BigInteger bigInt = new BigInteger(value);
        if(bigInt.compareTo(MIN) == -1 || bigInt.compareTo(MAX) == 1) throw new OutOfRangeException(bigInt);
        intVal = bigInt.intValue();
    }

    public IntegerLiteralSymbol(final int intVal){
        super(myName, Integer.toString(intVal));
        this.intVal = intVal;
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(final CodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public final long getValue() {
        return intVal;
    }
}
