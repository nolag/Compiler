package cs444.parser.symbols.ast.expressions;

import java.util.List;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeSymbol;

public class CreationExpression extends BaseExprSymbol{
    public final TypeSymbol createType;

    public CreationExpression(TypeSymbol type, List<ISymbol> args) {
        super("InstanceCreationExpression");

        this.setType(type);
        this.createType = type;
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
}
