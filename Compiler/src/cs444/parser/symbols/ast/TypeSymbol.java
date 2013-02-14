package cs444.parser.symbols.ast;

import cs444.parser.symbols.ATerminal;

public class TypeSymbol extends ATerminal{
    public boolean isArray;

    public TypeSymbol(String value, boolean isArray) {
        super("Type", value);
        this.isArray = isArray;
    }

    @Override
    public boolean empty() {
        return false;
    }
}
