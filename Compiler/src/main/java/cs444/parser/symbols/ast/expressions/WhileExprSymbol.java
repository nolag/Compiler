package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.TypeableTerminal;

import java.util.List;

public class WhileExprSymbol extends BaseExprSymbol {

    public WhileExprSymbol(List<ISymbol> children) {
        super("While");
        this.children.addAll(children);
    }

    public Typeable getCondition() {
        return (Typeable) getConditionSymbol();
    }

    public ANonTerminal getBody() {
        return (ANonTerminal) children.get(1);
    }

    public ISymbol getConditionSymbol() {
        return children.get(0);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        visitor.prepareCondition(getCondition());
        getConditionSymbol().accept(visitor);
        getBody().accept(visitor);
        visitor.close(this);
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
