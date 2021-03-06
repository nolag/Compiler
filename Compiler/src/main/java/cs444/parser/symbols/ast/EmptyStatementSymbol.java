package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;

public class EmptyStatementSymbol extends TypeableTerminal {

    public EmptyStatementSymbol() {
        super(";", "EmptyStatement");
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
