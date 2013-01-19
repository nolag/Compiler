package cs444.parser.factory;

import cs444.lexer.Token;
import cs444.parser.symbols.Terminal;

public class TerminalFactory implements ISymbolFactory{
    private final Token.Type type;

    public TerminalFactory(Token.Type type){
        this.type = type;
    }

    public Terminal create() {
        return new Terminal(type);
    }

    public String getType() {
        return type.name();
    }
}
