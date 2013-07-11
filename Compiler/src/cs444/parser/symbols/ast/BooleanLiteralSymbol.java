package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.JoosNonTerminal;

public class BooleanLiteralSymbol extends TypeableTerminal{
    private static final String SYM_NAME = "Boolean";
    public final boolean boolValue;

    public BooleanLiteralSymbol(String value) {
        super(SYM_NAME, value);
        boolValue = value.equals(JoosNonTerminal.TRUE_VALUE);
    }

    public BooleanLiteralSymbol(boolean value) {
        super(SYM_NAME, String.valueOf(value));
        this.boolValue = value;
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
