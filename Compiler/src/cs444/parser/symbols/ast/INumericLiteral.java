package cs444.parser.symbols.ast;

import cs444.parser.symbols.ISymbol;

public interface INumericLiteral extends ISymbol{
    public long getValue();
}
