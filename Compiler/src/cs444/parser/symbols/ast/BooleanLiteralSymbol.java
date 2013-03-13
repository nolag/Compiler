package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.JoosNonTerminal;

public class BooleanLiteralSymbol extends TypeableTerminal{
    public final boolean boolValue;

    public BooleanLiteralSymbol(String value) {
        super("Boolean", value);
        boolValue = value.equals(JoosNonTerminal.TRUE_VALUE);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);

    }
}
