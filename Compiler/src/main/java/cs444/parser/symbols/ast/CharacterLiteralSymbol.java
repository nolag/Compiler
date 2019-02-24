package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;

public class CharacterLiteralSymbol extends TypeableTerminal implements INumericLiteral {

    private static final String SYM_NAME = "CharacterLiteral";
    public final char charVal;

    public CharacterLiteralSymbol(ATerminal terminal) {

        super(SYM_NAME, terminal.value);

        char cvalue = value.charAt(1);

        int idx = value.indexOf('\\');
        if (idx != -1) {

            char escapeChar = value.charAt(idx + 1);
            if (StringEscapeUtils.isOctalDigit(escapeChar)) {

                String octal = value.substring(idx + 1, value.length() - 1);
                cvalue = StringEscapeUtils.octalEscape(octal);
            } else {
                cvalue = StringEscapeUtils.simpleEscape(escapeChar);
            }
        }

        charVal = cvalue;
    }

    public CharacterLiteralSymbol(char value) {
        super(SYM_NAME, Character.toString(value));
        charVal = value;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }

    @Override
    public final long getAsLongValue() {
        return charVal;
    }
}
