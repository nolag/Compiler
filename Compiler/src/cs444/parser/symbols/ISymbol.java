package cs444.parser.symbols;

import cs444.lexer.Token;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public interface ISymbol {
    /**
     *
     * @param token the next token
     * @return if the ITerminal needs to reduce
     * @throws UnexpectedTokenException thrown if the token is illegal and the ITerminal can't reduce
     */
    boolean giveToken(Token token) throws UnexpectedTokenException;
    
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
    
}
