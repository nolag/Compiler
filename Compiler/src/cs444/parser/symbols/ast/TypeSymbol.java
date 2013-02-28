package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.types.PkgClassResolver;

public class TypeSymbol extends ATerminal{
    public boolean isArray;
    private PkgClassResolver typeResolver;

    public TypeSymbol(String value, boolean isArray) {
        super("Type", value);
        this.isArray = isArray;
    }

    public PkgClassResolver getTypeDclNode() {
        return typeResolver;
    }

    public void setTypeDclNode(PkgClassResolver typeDclNode) {
        this.typeResolver = typeDclNode;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }
}
