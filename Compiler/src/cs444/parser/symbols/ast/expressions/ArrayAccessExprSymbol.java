package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class ArrayAccessExprSymbol extends BaseExprSymbol{

    public ArrayAccessExprSymbol(ISymbol who, ISymbol value) {
        super("Array Accessor");
        children.add(who);
        children.add(value);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        children.get(0).accept(visitor);
        children.get(1).accept(visitor);
        visitor.visit(this);
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
