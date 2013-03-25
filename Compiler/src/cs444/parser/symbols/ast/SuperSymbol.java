package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ICodeGenVisitor;

public class SuperSymbol extends TypeableTerminal{

    public SuperSymbol() {
        super("super", "super");
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(ICodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
