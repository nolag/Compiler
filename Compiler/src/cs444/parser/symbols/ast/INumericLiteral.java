package cs444.parser.symbols.ast;


public interface INumericLiteral extends Typeable, ILiteralSymbol {
    @Override
    public long getAsLongValue();
}
