package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ATerminal;

public class EmptyStatementSymbol extends ATerminal{

    public EmptyStatementSymbol() {
        super(";", "EmptyStatement");
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public boolean empty() {
        return false;
    }
}
