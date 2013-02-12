package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public class OneChildFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ANonTerminal from) {
        ISymbol retVal = from;
        while(from.isCollapsable() && from.children.size() == 1){
            retVal = from.children.get(0);
            if(ANonTerminal.class.isInstance(retVal)) from = (ANonTerminal) retVal;
            else break;
        }

        return retVal;
    }
}
