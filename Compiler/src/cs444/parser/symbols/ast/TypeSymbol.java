package cs444.parser.symbols.ast;

import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ATerminal;

public class TypeSymbol extends ATerminal{
    public boolean isArray;
    private AInterfaceOrClassSymbol typeDclNode;

    public TypeSymbol(String value, boolean isArray) {
        super("Type", value);
        this.isArray = isArray;
    }

    public AInterfaceOrClassSymbol getTypeDclNode() {
        return typeDclNode;
    }

    public void setTypeDclNode(AInterfaceOrClassSymbol typeDclNode) {
        this.typeDclNode = typeDclNode;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }
}
