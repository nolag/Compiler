package cs444.parser.symbols.ast;

import cs444.parser.symbols.ATerminal;

public class StringLiteralSymbol extends ATerminal {

	public final String value;

	public StringLiteralSymbol(ATerminal terminal) {
		super("StringLiteral", terminal.lexeme);

		StringBuilder builder = new StringBuilder();

		int idx = 1;
		while (idx < lexeme.length() - 1) {
			char ch = lexeme.charAt(idx);
			if (ch != '\\') {
				builder.append(ch);
				idx++;
			} else {
				idx++;
				char escapeChar = lexeme.charAt(idx);
				if (Character.isDigit(escapeChar)) {
					String str = "";
					int offset = 0;
					while (Character.isDigit(lexeme.charAt(idx + offset))) {
						str += lexeme.charAt(idx + offset);
						offset++;
					}
					builder.append(StringEscapeUtils.octalEscape(str));
					idx += offset;
				} else {
					builder.append(StringEscapeUtils.simpleEscape(escapeChar));
					idx++;
				}
			}
		}

		this.value = builder.toString();
	}

	@Override
	public boolean empty() {
		return false;
	}
}
