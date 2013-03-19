package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;

public class ByteLiteralSymbol extends TypeableTerminal implements INumericLiteral {
    public static final String myName = "ByteLiteral";

    public final byte byteVal;

    public ByteLiteralSymbol(byte byteVal){
        super(myName, Integer.toString(byteVal));
        this.byteVal = byteVal;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public int getValue() {
        return byteVal;
    }
}
