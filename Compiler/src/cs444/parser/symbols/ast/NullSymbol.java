package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;

public class NullSymbol extends TypeableTerminal {

    public NullSymbol() {
        super("null", "null");
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(final CodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
