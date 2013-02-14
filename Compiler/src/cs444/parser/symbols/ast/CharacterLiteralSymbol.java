package cs444.parser.symbols.ast;

import cs444.parser.symbols.ATerminal;

public class CharacterLiteralSymbol extends ATerminal {

	public final char value;

	public CharacterLiteralSymbol(ATerminal terminal) {

	    super("CharacterLiteral", terminal.lexeme);

		char value = lexeme.charAt(1);

		int idx = lexeme.indexOf('\\');
		if (idx != -1) {

			char escapeChar = lexeme.charAt(idx + 1);
			if (Character.isDigit(escapeChar)) {
				String octal = lexeme.substring(idx + 1, lexeme.length() - 1);
				value = StringEscapeUtils.octalEscape(octal);
			} else {
				value = StringEscapeUtils.simpleEscape(escapeChar);
			}
		}

		this.value = value;
	}

	@Override
	public boolean empty() {
		return false;
	}
}
