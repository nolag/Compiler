package cs444.parser.symbols;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;


public interface ISymbol{

    /**
     *
     * @return the rule or token that is represented by this ISymbol
     */
    public String getRule();

    /**
     *
     * @return the name of the symbol
     */
    public String getName();

    /**
     *
     * @return if the rule becomes empty so that it can be known if it should be added to a tree
     */
    public boolean empty();

    /**
     *
     * accepts a visitor that walks the tree
     * @throws CompilerException
     */
    public void accept(final ISymbolVisitor visitor) throws CompilerException;

    public void accept(final CodeGenVisitor<?, ?> visitor);
}
