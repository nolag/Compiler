package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public abstract class ASTSymbolFactory{

    /**
     *
     * @param start the start symbol to conver with all its children
     * @return the AST
     */
    public ISymbol convertAll(ANonTerminal start){
        ISymbol retVal  = convert(start);

        if(!ANonTerminal.class.isAssignableFrom(retVal.getClass())) return retVal;
        start = (ANonTerminal) retVal;
        int numChildren = start.children.size();
        for(int i = 0; i < numChildren; i++){
            if(!ANonTerminal.class.isInstance(start.children.get(i))) continue;
            ANonTerminal child = (ANonTerminal) start.children.remove(i);
            start.children.add(i, convertAll(child));
        }
        return retVal;
    }

    /**
     *
     * @param from the symbol to convert to the ast symbol
     * @return the ast symbol representing the symbol, or from if it is not valid for conversion
     */
    protected abstract ISymbol convert(ANonTerminal from);
}
