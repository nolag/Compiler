package cs444.parser.symbols.ast.factories;

import cs444.CompilerException;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;

public abstract class ASTSymbolFactory {

    /**
     * @param start the start symbol to convet with all its children
     * @return the AST
     * @throws CompilerException
     */
    public ISymbol convertAll(ISymbol start) throws CompilerException {
        ISymbol retVal = convert(start);
        if (!(retVal instanceof ANonTerminal)) {
            return retVal;
        }

        ANonTerminal nonTerm = (ANonTerminal) retVal;

        int numChildren = nonTerm.children.size();
        for (int i = 0; i < numChildren; i++) {
            ISymbol child = nonTerm.children.remove(i);
            nonTerm.children.add(i, convertAll(child));
        }

        return retVal;
    }

    public ISymbol convertBottomUp(ISymbol start) throws CompilerException {
        if (!(start instanceof ANonTerminal)) {
            return convert(start);
        }

        ANonTerminal nonTerm = (ANonTerminal) start;
        int numChildren = nonTerm.children.size();
        for (int i = 0; i < numChildren; i++) {
            ISymbol child = nonTerm.children.remove(i);
            nonTerm.children.add(i, convertBottomUp(child));
        }

        return convert(start);
    }

    /**
     * @param from the symbol to convert to the ast symbol
     * @return the ast symbol representing the symbol, or from if it is not valid for conversion
     * @throws CompilerException
     */
    protected abstract ISymbol convert(ISymbol from) throws CompilerException;
}
