package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeSymbol;

public class CreationExpression extends BaseExprSymbol{
    public final TypeSymbol type;
    public final Iterable<ISymbol> args;

    // public final boolean arrayCreation;
    public final ISymbol arrayDimExpr;

    public CreationExpression(TypeSymbol type, Iterable<ISymbol> args) {
        super("InstanceCreationExpression");

        this.type = type;
        this.args = args;
        this.arrayDimExpr = null;
    }

    public CreationExpression(TypeSymbol type, ISymbol arrayDimExpr) {
        super("ArrayCreationExpression");

        this.type = type;
        this.arrayDimExpr = arrayDimExpr;
        this.args = null;
    }

    public boolean isArrayCreation(){
        return arrayDimExpr != null;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        this.type.accept(visitor);

        if (this.args != null){
            for (ISymbol arg : this.args) {
                arg.accept(visitor);
            }
        }else{
            this.arrayDimExpr.accept(visitor);
        }

        visitor.close(this);
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

}
