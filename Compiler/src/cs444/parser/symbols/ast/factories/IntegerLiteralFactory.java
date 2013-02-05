package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class IntegerLiteralFactory extends ASTSymbolFactory{
    private static final String INT_LITERAL = "DECIMAL_INTEGER_LITERAL";

    @Override
    protected ISymbol convert(ANonTerminal from) throws OutOfRangeException {
        int length = from.children.size();
        boolean lastWasMinus = false;

        for(int i = 0; i < length; i++){
            ISymbol child = from.children.get(i);

            if(child.getName() == "MINUS"){
                lastWasMinus = true;
            }
            else{
                if(child.getName() == INT_LITERAL){
                    from.children.remove(i);
                    from.children.add(i, new IntegerLiteralSymbol((Terminal)child, lastWasMinus));
                    //we don't need the -ve from before the number anymore
                    if(lastWasMinus) from.children.remove(i - 1);
                }
                lastWasMinus = false;
            }
        }

        return from;
    }
}
