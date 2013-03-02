package cs444.parser.symbols.ast;

import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class MethodHeader {
    public final NameSymbol name;
    public final TypeSymbol type;
    public final Iterable<DclSymbol> params;

    public MethodHeader(NameSymbol name, TypeSymbol type,  Iterable<DclSymbol> dcls) throws IllegalModifierException, UnsupportedException {
        this.name = name;
        this.type = type;
        this.params = dcls;
    }
}
