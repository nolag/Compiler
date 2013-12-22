package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;

public class ShortLiteralSymbol extends TypeableTerminal implements INumericLiteral {
    public static final String myName = "ShortLiteral";

    public final short shortVal;

    public ShortLiteralSymbol(final short shortVal){
        super(myName, Integer.toString(shortVal));
        this.shortVal = shortVal;
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
    public final long getAsLongValue() {
        return shortVal;
    }
}
