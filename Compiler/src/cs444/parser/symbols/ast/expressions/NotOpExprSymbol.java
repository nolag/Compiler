package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;

public class NotOpExprSymbol extends UnaryOpExprSymbol{
    public static final String myName = "Not";

    public NotOpExprSymbol(ISymbol on) {
        super(myName, on);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException{
        children.get(0).accept(visitor);
        visitor.visit(this);
    }
}
