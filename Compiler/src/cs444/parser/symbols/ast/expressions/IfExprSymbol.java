package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.TypeableTerminal;

public class IfExprSymbol extends BaseExprSymbol {

    public IfExprSymbol(ISymbol condition, ISymbol ifBody, ISymbol elseBody) {
        this(condition, ifBody);
        this.children.add(elseBody);
    }

    public IfExprSymbol(ISymbol condition, ISymbol ifBody) {
        super("If");
        this.children.add(condition);
        this.children.add(ifBody);
    }

    public Typeable getCondition(){
        return (Typeable) getConditionSymbol();
    }

    public ANonTerminal getifBody() {
        return (ANonTerminal) children.get(1);
    }

    public ISymbol getConditionSymbol() {
        return children.get(0);
    }

    public ANonTerminal getElseBody() {
        if (children.size() < 3) return null;
        return (ANonTerminal) children.get(2);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);
        visitor.prepareCondition(getCondition());
        this.getConditionSymbol().accept(visitor);
        this.getifBody().accept(visitor);
        ISymbol elseBody = this.getElseBody();
        if (elseBody != null) {
            visitor.prepareElseBody(this);
            elseBody.accept(visitor);
        }
        visitor.close(this);
    }

    @Override
    public void accept(ICodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduceToLiteral() {
         return null;
    }
}
