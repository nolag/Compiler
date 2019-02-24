package cs444.parser.symbols.ast;

import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.List;

public class MethodHeader {
    public final NameSymbol name;
    public final TypeSymbol type;
    public final List<DclSymbol> params;

    public MethodHeader(NameSymbol name, TypeSymbol type, List<DclSymbol> dcls) throws IllegalModifierException,
            UnsupportedException {
        this.name = name;
        this.type = type;
        params = dcls;
    }
}
