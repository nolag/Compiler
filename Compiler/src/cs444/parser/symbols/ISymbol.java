package cs444.parser.symbols;


public interface ISymbol {

    /**
     *
     * @return the rule or token that is represented by this ISymbol
     */
    String rule();

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

}
