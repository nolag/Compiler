package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class ASTSymbolFactory{

    /**
     *
     * @param start the start symbol to conver with all its children
     * @return the AST
     * @throws UnsupportedException
     * @throws IllegalModifierException
     */
    public ISymbol convertAll(ISymbol start) throws OutOfRangeException, UnsupportedException, IllegalModifierException{
        ISymbol retVal  = convert(start);
        if(!ANonTerminal.class.isInstance(retVal)) return retVal;

        ANonTerminal nonTerm = (ANonTerminal) retVal;

        int numChildren = nonTerm.children.size();
        for(int i = 0; i < numChildren; i++){
            ISymbol child = nonTerm.children.remove(i);
            nonTerm.children.add(i, convertAll(child));
        }

        return retVal;
    }

    public ISymbol convertBottomUp(ISymbol start) throws OutOfRangeException, UnsupportedException, IllegalModifierException{
        if(!ANonTerminal.class.isInstance(start)){
            return convert(start);
        }

        ANonTerminal nonTerm = (ANonTerminal) start;
        int numChildren = nonTerm.children.size();
        for(int i = 0; i < numChildren; i++){
            ISymbol child = nonTerm.children.remove(i);
            nonTerm.children.add(i, convertAll(child));
        }

        return convert(start);
    }

    /**
     *
     * @param from the symbol to convert to the ast symbol
     * @return the ast symbol representing the symbol, or from if it is not valid for conversion
     * @throws OutOfRangeException
     * @throws UnsupportedException
     * @throws IllegalModifierException
     */
    protected abstract ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException;
}
