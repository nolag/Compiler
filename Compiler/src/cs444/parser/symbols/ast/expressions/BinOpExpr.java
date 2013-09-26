package cs444.parser.symbols.ast.expressions;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public abstract class BinOpExpr extends BaseExprSymbol{

    protected BinOpExpr(final String name, final ISymbol left, final ISymbol right) {
        super(name);
        children.add(left);
        children.add(right);
    }

    protected static Typeable zeroReducer(final INumericLiteral number, final Typeable leftOperand) {
        if (number.getValue() == 0) {
            if (number instanceof LongLiteralSymbol && !leftOperand.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)) {
                final CastExpressionSymbol cast = new CastExpressionSymbol(number.getType(), leftOperand);
                return cast;
            }
            //If it's not an integer, it will be loaded like one or it will already be a long
            return leftOperand;
        }

        return null;
    }

    public ISymbol getLeftOperand(){
        return children.get(0);
    }

    public ISymbol getRightOperand(){
        return children.get(1);
    }
}
