package cs444.parser.symbols.ast.expressions;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public class NotOpExprSymbol extends UnaryOpExprSymbol{
    public static final String myName = "Not";

    public NotOpExprSymbol(final ISymbol on) {
        super(myName, on);
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException{
        children.get(0).accept(visitor);
        visitor.visit(this);
    }

    @Override
    public void accept(final CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public Typeable reduce() {
        final ISymbol operand = getOperand();

        if (operand instanceof BooleanLiteralSymbol){
            final boolean val = ((BooleanLiteralSymbol)operand).boolValue;
            return new BooleanLiteralSymbol(!val);
        } else if(operand instanceof NotOpExprSymbol) {
            final NotOpExprSymbol not = (NotOpExprSymbol) operand;
            return (Typeable) not.getOperand();
        } else {
            return null;
        }
    }
}
