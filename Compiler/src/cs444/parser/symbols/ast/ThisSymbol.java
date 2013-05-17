package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;

public class ThisSymbol extends TypeableTerminal{
    public final boolean implied;

    public ThisSymbol() {
        this(false);
    }

    public ThisSymbol(final boolean implied){
        super("this", "this");
        this.implied = implied;
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
