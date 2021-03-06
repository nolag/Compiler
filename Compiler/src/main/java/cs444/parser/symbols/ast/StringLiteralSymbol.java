package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;

public class StringLiteralSymbol extends TypeableTerminal {

    private static final String SYMBOL_NAME = "StringLiteral";
    public final String strValue;

    public StringLiteralSymbol(String strVal) {
        super(SYMBOL_NAME, strVal);
        strValue = strVal;
    }

    public StringLiteralSymbol(ATerminal terminal) {
        super(SYMBOL_NAME, terminal.value);

        StringBuilder builder = new StringBuilder();

        int idx = 1;
        while (idx < value.length() - 1) {
            char ch = value.charAt(idx);
            idx++;
            if (ch != '\\') {
                builder.append(ch);
            } else {
                char escapeChar = value.charAt(idx);
                if (StringEscapeUtils.isOctalDigit(escapeChar)) {
                    String str = "";
                    int offset = 0;
                    int maxEscape = StringEscapeUtils.isZeroToThree(value.charAt(idx + offset)) ? 3 : 2;
                    for (int x = 0; x < maxEscape && StringEscapeUtils.isOctalDigit(value.charAt(idx + offset)); x++) {
                        str += value.charAt(idx + offset);
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

        strValue = builder.toString();
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
