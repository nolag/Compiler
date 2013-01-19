package cs444.parser.symbols;

import cs444.lexer.Token;
import cs444.parser.factory.TerminalFactory;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class Terminal implements ISymbol{

    private final Token.Type type;
    private Token token = null;

    public Terminal(Token.Type type){
        this.type = type;
    }

    private boolean reduce = false;

    public boolean giveToken(Token token) throws UnexpectedTokenException {
        if(reduce) return true;
        if(token.getType() != type) throw new UnexpectedTokenException(token,
                new StateTerminal[]{ new StateTerminal(new TerminalFactory(type), 0) });
        this.token = token;
        reduce = true;
        return false;
    }

    public String getType() {
        return type.name();
    }
}
