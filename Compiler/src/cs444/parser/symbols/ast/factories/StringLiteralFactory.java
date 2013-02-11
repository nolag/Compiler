package cs444.parser.symbols.ast.factories;

import cs444.lexer.Token;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.parser.symbols.exceptions.OutOfRangeException;

public class StringLiteralFactory extends ASTSymbolFactory {

	@Override
	protected ISymbol convert(ANonTerminal from) throws OutOfRangeException {
	    //TODO change when we know the rule

	    if(!from.getName().equalsIgnoreCase("Holder")) return from;

		for (int i = 0; i < from.children.size(); i++) {

			ISymbol symbol = from.children.get(i);
			String name = symbol.getName();

			if (name == Token.Type.CHAR_LITERAL.toString()) {
				from.children.remove(i);
				from.children.add(i, new CharacterLiteralSymbol((Terminal)symbol));
			}
			else if (name == Token.Type.STR_LITERAL.toString()) {
				from.children.remove(i);
				from.children.add(i, new StringLiteralSymbol((Terminal)symbol));
			}
		}

		return from;
	}
}
