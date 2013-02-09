package cs444.parser.symbols.ast;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class TypeSymbol implements ISymbol{
    public final String type;
    public final boolean isArray;

    public TypeSymbol(String type, int numArrays) throws UnsupportedException{
        this.type = type;
        this.isArray = numArrays == 0;
        if(numArrays > 1) throw new UnsupportedException("multi dimentional arrays");
    }

    public String rule() {
        return "Type -> " + type;
    }

    public String getName() {
        return "Type";
    }

    public boolean empty() {
        return false;
    }

    public Iterable<ISymbol> getChildren() {
        return Terminal.nochildren;
    }

}
