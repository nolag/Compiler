
package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class InstanceOfExprSymbol extends BinOpExpr {
    public final static String myName = "InstanceOf";

    public InstanceOfExprSymbol(ISymbol left, ISymbol right) {
        super(myName, left, right);
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        children.get(0).accept(visitor);
        children.get(1).accept(visitor);
        visitor.visit(this);
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
    public TypeableTerminal reduceToLiteral() {
         return null;
    }
}