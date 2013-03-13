package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.Typeable;

public class ForExprSymbol extends BaseExprSymbol {

    public ForExprSymbol(ISymbol forInit, ISymbol forCondition,
            ISymbol forUpdate, ISymbol body) {
        super("For");
        children.add(forInit);
        children.add(forCondition);
        children.add(forUpdate);
        children.add(body);
    }

    public Typeable getCondition(){
        return (Typeable) children.get(1);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        visitor.prepareCondition(getCondition());
//        TODO: don't think we need this
 //       visitor.visit(this);
        for (ISymbol child : children) {
            child.accept(visitor);
        }
        visitor.close(this);
    }
}
