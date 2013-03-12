package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.types.APkgClassResolver;

public class TypeSymbol extends ATerminal{
    public boolean isArray;
    public final boolean isClass;
    private APkgClassResolver typeResolver;

    public TypeSymbol(String value, boolean isArray, boolean isClass) {
        super("Type", value);
        this.isArray = isArray;
        this.isClass = isClass;
    }

    public APkgClassResolver getTypeDclNode() {
        return typeResolver;
    }

    public void setTypeDclNode(APkgClassResolver typeDclNode) {
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
