package cs444.parser.symbols.ast;

import java.util.Collections;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;

public class CharacterLiteralSymbol implements ISymbol {

	private final Terminal terminal;
	public final char value;
	
	public CharacterLiteralSymbol(Terminal terminal) {
		
		this.terminal = terminal;
		
		String lexeme = terminal.token.lexeme;
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
