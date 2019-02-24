package cs444.parser.symbols.ast;

import cs444.parser.symbols.ISymbol;

public interface Typeable extends ISymbol {
    TypeSymbol getType();

    void setType(TypeSymbol type);
}
