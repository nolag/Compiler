package cs444.parser.symbols.ast.expressions;

import java.util.List;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class WhileExprSymbol extends BaseExprSymbol {

    public WhileExprSymbol(List<ISymbol> children) {
        super("While");
        this.children.addAll(children);
    }

    public Typeable getCondition(){
        return (Typeable) children.get(0);
    }

    public ANonTerminal getBody() {
        return (ANonTerminal) children.get(1);
    }

    public BaseExprSymbol getConditionExpression() {
        return (BaseExprSymbol) children.get(0);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        visitor.prepareCondition(getCondition());
        visitor.visit(this);
        conditionAccept(visitor);
        getBody().accept(visitor);
        visitor.close(this);
    }

    private void conditionAccept(ISymbolVisitor visitor) throws CompilerException {

        // TODO pull this to a Decorator????
        ISymbol iSymBody = children.get(0);

        if(iSymBody instanceof BaseExprSymbol){
            ((BaseExprSymbol)iSymBody).accept(visitor);
        }else if(iSymBody instanceof NameSymbol){
            ((NameSymbol) iSymBody).accept(visitor);
        }else if (iSymBody instanceof BooleanLiteralSymbol){
            ((BooleanLiteralSymbol) iSymBody).accept(visitor);
        }else if ((iSymBody) instanceof MethodInvokeSymbol){
            ((MethodInvokeSymbol) iSymBody).accept(visitor);
        }else{
            String errorMsg = "Unexpected while's body. Probably a bug?. Got symbol: " + iSymBody.getName();
            System.err.println(errorMsg);
            throw new CompilerException(errorMsg);
        }
    }
}
