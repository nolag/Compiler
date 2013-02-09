package cs444.parser.symbols.ast;

import java.util.Collections;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;

public class StringLiteralSymbol implements ISymbol {

	private final Terminal terminal;
	public final String value;
	
	public StringLiteralSymbol(Terminal terminal) {
		this.terminal = terminal;
		
		String lexeme = terminal.token.lexeme;
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
	public String rule() {
		return terminal.rule();
	}

	@Override
	public String getName() {
		return terminal.getName();
	}

	@Override
	public boolean empty() {
		return terminal.empty();
	}

	@Override
	public Iterable<ISymbol> getChildren() {
		return Collections.emptyList();
	}

}
