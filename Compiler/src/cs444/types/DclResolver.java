package cs444.types;

import cs444.parser.symbols.ast.AModifiersOptSymbol;

public interface DclResolver {
    public AModifiersOptSymbol findDCl(String fullName);
}
