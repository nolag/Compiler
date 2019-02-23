package cs444.parser.symbols.ast;

import cs444.parser.symbols.ISymbol;

public interface Typeable extends ISymbol {
    public TypeSymbol getType();
    public void setType(TypeSymbol type);
}
