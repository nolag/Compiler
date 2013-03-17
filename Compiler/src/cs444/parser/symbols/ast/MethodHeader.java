package cs444.parser.symbols.ast;

import java.util.List;

import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class MethodHeader {
    public final NameSymbol name;
    public final TypeSymbol type;
    public final List<DclSymbol> params;

    public MethodHeader(NameSymbol name, TypeSymbol type,  List<DclSymbol> dcls) throws IllegalModifierException, UnsupportedException {
        this.name = name;
        this.type = type;
        this.params = dcls;
    }
}
