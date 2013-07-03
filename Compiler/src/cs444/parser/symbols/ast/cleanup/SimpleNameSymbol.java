package cs444.parser.symbols.ast.cleanup;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;

public class SimpleNameSymbol extends ATerminal implements Typeable{
    public final DclSymbol dcl;

    public SimpleNameSymbol(DclSymbol dcl) {
        super("Simple Name", dcl.dclName);
        this.dcl = dcl;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeSymbol getType() {
        return dcl.getType();
    }

    @Override
    public void setType(TypeSymbol type) {
        dcl.setType(type);
    }

}
