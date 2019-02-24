package cs444.parser.symbols.ast;

public interface INumericLiteral extends ILiteralSymbol {
    @Override
    long getAsLongValue();
}
