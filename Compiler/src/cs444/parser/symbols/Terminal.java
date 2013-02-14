package cs444.parser.symbols;

import cs444.lexer.Token;
import cs444.lexer.Token.Parse;

public class Terminal extends ATerminal{
    private final boolean empty;

    public Terminal(Token token){
        super(token.type.toString(), token.lexeme);
        empty = Token.typeToParse.get(token.type) != Parse.VALID;
    }

    public boolean empty() {
        return empty;
    }
}
