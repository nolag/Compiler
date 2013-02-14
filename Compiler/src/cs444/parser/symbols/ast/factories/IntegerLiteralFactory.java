package cs444.parser.symbols.ast.factories;

import cs444.lexer.Token;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class IntegerLiteralFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException {
        //TODO when the name of the expression type is known use it here
        if(!"numHolder".equalsIgnoreCase(from.getName())) return from;

        ANonTerminal nonTerm = (ANonTerminal) from;

        int length = nonTerm.children.size();
        boolean lastWasMinus = false;

        for(int i = 0; i < length; i++){
            ISymbol child = nonTerm.children.get(i);

            if(child.getName().toUpperCase().equals(Token.Type.MINUS.toString())){
                lastWasMinus = true;
            }
            else{
                if(child.getName().toUpperCase().equals(Token.Type.DECIMAL_INTEGER_LITERAL.toString())){
                    nonTerm.children.remove(i);
                    nonTerm.children.add(i, new IntegerLiteralSymbol((Terminal)child, lastWasMinus));
                    //we don't need the -ve from before the number anymore
                    if(lastWasMinus) nonTerm.children.remove(i - 1);
                }
                lastWasMinus = false;
            }
        }

        return from;
    }
}
