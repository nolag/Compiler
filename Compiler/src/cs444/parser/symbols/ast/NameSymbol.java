package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
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

    public Iterable<Typeable> getLookupPath(){
        return this.dclNode.dcls;
    }

    public TypeSymbol getType(){
        return this.dclNode.lastDcl.getType();
    }
}
