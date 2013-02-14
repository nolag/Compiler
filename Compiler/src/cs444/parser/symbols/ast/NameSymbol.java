package cs444.parser.symbols.ast;

import cs444.parser.symbols.ATerminal;

public class NameSymbol extends ATerminal{
    public static enum Type { ID_SYMBOL, PACKAGE, IMPORT, STAR_IMPORT };

    public final Type type;

    public NameSymbol(String value, Type type) {
        super("Name", value);
        this.type = type;
    }

    public boolean empty() {
        return false;
    }
}
