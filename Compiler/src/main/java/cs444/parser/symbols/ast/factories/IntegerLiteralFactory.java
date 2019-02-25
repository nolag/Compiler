package cs444.parser.symbols.ast.factories;

import cs444.lexer.Token;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.exceptions.OutOfRangeException;

import java.util.LinkedList;
import java.util.List;

public class IntegerLiteralFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException {
        if (from.getName().toUpperCase().equals(Token.Type.DECIMAL_INTEGER_LITERAL.toString())) {
            return new IntegerLiteralSymbol((ATerminal) from, false);
        }

        if (from.getName().toUpperCase().equals(Token.Type.LONG_INTEGER_LITERAL.toString())) {
            return new LongLiteralSymbol((ATerminal) from, false);
        }

        if (!JoosNonTerminal.UNARY_EXPRESSION.equalsIgnoreCase(from.getName())) {
            return from;
        }

        ANonTerminal nonTerm = (ANonTerminal) from;

        int length = nonTerm.children.size();
        boolean lastWasMinus = false;

        List<ISymbol> remove = new LinkedList<ISymbol>();

        for (int i = 0; i < length; i++) {
            ISymbol child = nonTerm.children.get(i);

            if (child.getName().toUpperCase().equals(Token.Type.MINUS.toString())) {
                lastWasMinus = true;
            } else {
                if (child.getName().equalsIgnoreCase(Token.Type.DECIMAL_INTEGER_LITERAL.toString())) {
                    remove.add(nonTerm.children.get(i));
                    nonTerm.children.add(i, new IntegerLiteralSymbol((ATerminal) child, lastWasMinus));
                    //we don't need the -ve from before the number anymore
                    if (lastWasMinus) {
                        remove.add(nonTerm.children.get(i - 1));
                    }
                }
                if (child.getName().equalsIgnoreCase(Token.Type.LONG_INTEGER_LITERAL.toString())) {
                    remove.add(nonTerm.children.get(i));
                    nonTerm.children.add(i, new LongLiteralSymbol((ATerminal) child, lastWasMinus));
                    //we don't need the -ve from before the number anymore
                    if (lastWasMinus) {
                        remove.add(nonTerm.children.get(i - 1));
                    }
                }
                lastWasMinus = false;
            }
        }

        nonTerm.children.removeAll(remove);
        if (nonTerm.children.size() == 1) {
            return nonTerm.children.get(0);
        }
        return from;
    }
}
