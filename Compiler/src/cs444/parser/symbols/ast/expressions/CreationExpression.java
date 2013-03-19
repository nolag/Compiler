package cs444.parser.symbols.ast.expressions;

import java.util.List;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class CreationExpression extends BaseExprSymbol{
    public CreationExpression(TypeSymbol type, List<ISymbol> args) {
        super("InstanceCreationExpression");

        this.setType(type);
        this.children.addAll(args);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        this.getType().accept(visitor);

        for (ISymbol child : children) {
            child.accept(visitor);
        }
        visitor.close(this);
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public TypeableTerminal reduceToLiteral() {
        return null;
    }
}
