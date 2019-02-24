package cs444.parser.symbols;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;

public interface ISymbol {

    /**
     * @return the rule or token that is represented by this ISymbol
     */
    String getRule();

    /**
     * @return the name of the symbol
     */
    String getName();

    /**
     * @return if the rule becomes empty so that it can be known if it should be added to a tree
     */
    boolean empty();

    /**
     * accepts a visitor that walks the tree
     *
     * @throws CompilerException
     */
    void accept(ISymbolVisitor visitor) throws CompilerException;

    void accept(CodeGenVisitor<?, ?> visitor);
}
