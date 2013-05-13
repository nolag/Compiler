package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.types.LookupLink;

public class NameSymbol extends ATerminal implements Typeable{
    public static enum Type { ID_SYMBOL, PACKAGE, IMPORT, STAR_IMPORT };

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

    public void setDclNode(LookupLink node){
        this.dclNode = node;
    }

    public LookupLink getDclNode(){
        return dclNode;
    }

    public Iterable<Typeable> getLookupPath(){
        return this.dclNode.dcls;
    }

    public DclSymbol getLastLookupDcl(){
        return (DclSymbol) this.dclNode.lastDcl;
    }

    @Override
    public TypeSymbol getType(){
        return this.dclNode.lastDcl.getType();
    }

    @Override
    public void setType(TypeSymbol type) { }

    @Override
    public void accept(ICodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
