package cs444.parser.symbols.ast.expressions;

import java.util.List;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.Typeable;

public class WhileExprSymbol extends BaseExprSymbol {

    public WhileExprSymbol(List<ISymbol> children) {
        super("While");
        this.children.addAll(children);
    }

    public Typeable getCondition(){
        return (Typeable) children.get(0);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        visitor.prepareCondition(getCondition());
        visitor.visit(this);
        visitor.close(this);
    }
}
