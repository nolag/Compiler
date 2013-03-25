package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class NotOpExprSymbol extends UnaryOpExprSymbol{
    public static final String myName = "Not";

    public NotOpExprSymbol(ISymbol on) {
        super(myName, on);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException{
        children.get(0).accept(visitor);
        visitor.visit(this);
    }

    @Override
    public void accept(ICodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduceToLiteral() {
        ISymbol operand = getOperand();

        if (operand instanceof BooleanLiteralSymbol){
            boolean val = ((BooleanLiteralSymbol)operand).boolValue;
            return new BooleanLiteralSymbol(!val);
        }else{
            return null;
        }
    }
}
