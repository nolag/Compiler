package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

import java.util.List;

public class CreationExpression extends BaseExprSymbol {
    public CreationExpression(TypeSymbol type, List<ISymbol> args) {
        super("InstanceCreationExpression");

        setType(type);
        children.addAll(args);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        getType().accept(visitor);

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
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduce() {
        return null;
    }
}
