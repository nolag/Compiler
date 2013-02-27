package cs444.parser.symbols;

import cs444.ast.ISymbolVisitor;


public interface ISymbol {

    /**
     *
     * @return the rule or token that is represented by this ISymbol
     */
    String getRule();

    /**
     *
     * @return the name of the symbol
     */
    String getName();

    /**
     *
     * @return if the rule becomes empty so that it can be known if it should be added to a tree
     */
    boolean empty();

    /**
     *
     * accepts a visitor that walks the tree
     */
    void accept(final ISymbolVisitor visitor);
}
