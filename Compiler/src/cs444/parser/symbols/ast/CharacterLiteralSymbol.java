package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;

public class CharacterLiteralSymbol extends TypeableTerminal implements INumericLiteral {

	private static final String SYM_NAME = "CharacterLiteral";
    public final char charVal;

	public CharacterLiteralSymbol(final ATerminal terminal) {

	    super(SYM_NAME, terminal.value);

		char cvalue = value.charAt(1);

		final int idx = value.indexOf('\\');
		if (idx != -1) {

			final char escapeChar = value.charAt(idx + 1);
			if (StringEscapeUtils.isOctalDigit(escapeChar)) {

				final String octal = value.substring(idx + 1, value.length() - 1);
				cvalue = StringEscapeUtils.octalEscape(octal);
			} else {
				cvalue = StringEscapeUtils.simpleEscape(escapeChar);
			}
		}

		this.charVal = cvalue;
	}

	public CharacterLiteralSymbol(final char value) {
	    super(SYM_NAME, Character.toString(value));
	    this.charVal = value;
    }

    @Override
	public void accept(final ISymbolVisitor visitor) throws CompilerException {
	    visitor.visit(this);
	}

    @Override
    public void accept(final CodeGenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public final long getValue() {
        return charVal;
    }
}
