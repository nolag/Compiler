package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public class FieldAccessSymbol extends ANonTerminal implements Typeable {

    public FieldAccessSymbol(ISymbol iSymbol, ISymbol me) {
        this(iSymbol, me, null);
    }

    public FieldAccessSymbol(ISymbol iSymbol, ISymbol me, TypeSymbol type) {
        super("FieldAccess");
        children.add(iSymbol);
        children.add(me);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.prepare(this);
        children.get(0).accept(visitor);
        visitor.open(this);
        children.get(1).accept(visitor);
        visitor.close(this);
    }

    @Override
    public TypeSymbol getType() {
        return ((Typeable) children.get(1)).getType();
    }

    @Override
    public void setType(TypeSymbol type) { }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
