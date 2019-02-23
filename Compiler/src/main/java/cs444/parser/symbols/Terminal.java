package cs444.parser.symbols;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.lexer.Token;
import cs444.lexer.Token.Parse;

public final class Terminal extends ATerminal{
    private final boolean empty;

    public Terminal(final Token token){
        super(token.type.toString(), token.lexeme);
        empty = Token.typeToParse.get(token.type) != Parse.VALID;
    }

    @Override
    public boolean empty() {
        return empty;
    }

    @Override
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(final CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
