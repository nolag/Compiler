package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.TypeableTerminal;

public class ForExprSymbol extends BaseExprSymbol {
    private int offsetSize = 0;

    public ForExprSymbol(ISymbol forInit, ISymbol forCondition,
            ISymbol forUpdate, ISymbol body) {
        super("For");
        children.add(forInit);
        children.add(forCondition);
        children.add(forUpdate);
        children.add(body);
    }

    public Typeable getCondition(){
        return (Typeable) getConditionExpr();
    }

    public ISymbol getForInit(){
        return children.get(0);
    }

    public ISymbol getConditionExpr(){
        return children.get(1);
    }

    public ISymbol getForUpdate(){
        return children.get(2);
    }

    public ISymbol getBody(){
        return children.get(3);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        visitor.prepareCondition(getCondition());
        getForInit().accept(visitor);
        visitor.afterClause(this);
        getConditionExpr().accept(visitor);
        visitor.afterCondition(this);
        visitor.afterClause(this);
        getForUpdate().accept(visitor);
        visitor.afterClause(this);
        final ISymbol body = getBody();
        if(body != null) body.accept(visitor);
        visitor.close(this);
    }

    public void setOffsetSize(int offsetSize){
        this.offsetSize = offsetSize;
    }

    public int getOffsetSize(){
        return offsetSize;
    }

    @Override
    public void accept(CodeGenVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public TypeableTerminal reduceToLiteral() {
         return null;
    }
}
