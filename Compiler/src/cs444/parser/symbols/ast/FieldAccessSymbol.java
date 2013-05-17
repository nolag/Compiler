package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public class FieldAccessSymbol extends ANonTerminal implements Typeable {

    private TypeSymbol type;

    public FieldAccessSymbol(final ISymbol iSymbol, final ISymbol me) {
        this(iSymbol, me, null);
    }

    public FieldAccessSymbol(final ISymbol iSymbol, final ISymbol me, final TypeSymbol type) {
        super("FieldAccess");
        children.add(iSymbol);
        children.add(me);
        this.type = type;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.prepare(this);
        children.get(0).accept(visitor);
        visitor.open(this);
        children.get(1).accept(visitor);
        visitor.close(this);
    }

    @Override
    public TypeSymbol getType() {
        return type;
    }

    @Override
    public void setType(final TypeSymbol type) {
        this.type = type;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(final CodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
