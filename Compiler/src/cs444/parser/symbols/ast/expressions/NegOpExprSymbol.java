package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.ast.TypeableTerminal;

public class NegOpExprSymbol extends UnaryOpExprSymbol{
    public static final String myName = "Negative";

    public NegOpExprSymbol(final ISymbol on) {
        super(myName, on);
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        children.get(0).accept(visitor);
        visitor.visit(this);
    }

    @Override
    public void accept(final CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeableTerminal reduce() {
        final ISymbol operand = getOperand();

        if (operand instanceof INumericLiteral){
            final long val = ((INumericLiteral)operand).getValue();
            return operand instanceof LongLiteralSymbol ? new LongLiteralSymbol(-val) : new IntegerLiteralSymbol((int)-val);
        }else{
            return null;
        }
    }
}
