package cs444.parser.symbols.ast.factories;

import cs444.CompilerException;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public abstract class ASTSymbolFactory{

    /**
     * 
     * @param start the start symbol to convet with all its children
     * @return the AST
     * @throws CompilerException
     */
    public ISymbol convertAll(final ISymbol start) throws CompilerException{
        final ISymbol retVal  = convert(start);
        if(!ANonTerminal.class.isInstance(retVal)) return retVal;

        final ANonTerminal nonTerm = (ANonTerminal) retVal;

        final int numChildren = nonTerm.children.size();
        for(int i = 0; i < numChildren; i++){
            final ISymbol child = nonTerm.children.remove(i);
            nonTerm.children.add(i, convertAll(child));
        }

        return retVal;
    }

    public ISymbol convertBottomUp(final ISymbol start) throws CompilerException{
        if(!ANonTerminal.class.isInstance(start)){
            return convert(start);
        }

        final ANonTerminal nonTerm = (ANonTerminal) start;
        final int numChildren = nonTerm.children.size();
        for(int i = 0; i < numChildren; i++){
            final ISymbol child = nonTerm.children.remove(i);
            nonTerm.children.add(i, convertBottomUp(child));
        }

        return convert(start);
    }

    /**
     * 
     * @param from the symbol to convert to the ast symbol
     * @return the ast symbol representing the symbol, or from if it is not valid for conversion
     * @throws CompilerException
     */
    protected abstract ISymbol convert(ISymbol from) throws CompilerException;
}
