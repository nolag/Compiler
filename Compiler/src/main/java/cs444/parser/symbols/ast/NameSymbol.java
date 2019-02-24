package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.types.LookupLink;

public class NameSymbol extends ATerminal implements Typeable {
    public final Type type;

    private LookupLink dclNode = null;

    public NameSymbol(String value, Type type) {
        super("Name", value);
        this.type = type;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    public LookupLink getDclNode() {
        return dclNode;
    }

    public void setDclNode(LookupLink node) {
        dclNode = node;
    }

    public Iterable<Typeable> getLookupPath() {
        return dclNode.dcls;
    }

    public DclSymbol getLastLookupDcl() {
        return (DclSymbol) dclNode.lastDcl;
    }

    @Override
    public TypeSymbol getType() {
        return dclNode.lastDcl.getType();
    }

    @Override
    public void setType(TypeSymbol type) { }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    public enum Type {ID_SYMBOL, PACKAGE, IMPORT, STAR_IMPORT}
}
