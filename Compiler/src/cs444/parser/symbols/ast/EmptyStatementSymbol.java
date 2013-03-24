package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ISymbolChoiceVisitor;

public class EmptyStatementSymbol extends TypeableTerminal {

    public EmptyStatementSymbol() {
        super(";", "EmptyStatement");
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(ISymbolChoiceVisitor visitor) {
        visitor.visit(this);
    }
}
