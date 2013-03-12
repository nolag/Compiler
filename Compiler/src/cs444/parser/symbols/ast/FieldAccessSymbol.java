package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public class FieldAccessSymbol extends ANonTerminal implements Typeable {

    private TypeSymbol type;

    public FieldAccessSymbol(ISymbol iSymbol, NameSymbol me) {
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
        //TODO
        /*visitor.open(this);
        for(ISymbol symbol : children) symbol.accept(visitor);
        visitor.close(this);*/
    }

    @Override
    public TypeSymbol getType() {
        return type;
    }

    @Override
    public void setType(TypeSymbol type) {
        this.type = type;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

}
