package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class NegOpExprSymbol extends UnaryOpExprSymbol{
    public static final String myName = "Negative";

    public NegOpExprSymbol(ISymbol on) {
        super(myName, on);
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        children.get(0).accept(visitor);
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduceToLiteral() {
        ISymbol operand = getOperand();

        if (operand instanceof INumericLiteral){
            int val = ((INumericLiteral)operand).getValue();
            return new IntegerLiteralSymbol(-val);
        }else{
            return null;
        }
    }
}
