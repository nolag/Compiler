package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public class OneChildFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) {
        if(!ANonTerminal.class.isInstance(from)) return from;

        ANonTerminal retVal = (ANonTerminal)from;
        while(retVal.isCollapsable() && retVal.children.size() == 1){
            from = retVal.children.get(0);
            if(!ANonTerminal.class.isInstance(from))break;
            else retVal =  (ANonTerminal) from;
        }

        return from;
    }
}
