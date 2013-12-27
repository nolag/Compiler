package cs444.parser.symbols.ast.factories;

import cs444.lexer.Token;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class StringLiteralFactory extends ASTSymbolFactory {

	@Override
	protected ISymbol convert(ISymbol from) throws OutOfRangeException {
		String name = from.getName();

		if (name.equals(Token.Type.CHAR_LITERAL.toString())) return new CharacterLiteralSymbol((Terminal)from);

		if (name.equals(Token.Type.STR_LITERAL.toString())) return new StringLiteralSymbol((Terminal)from);

		return from;
	}
}
