package cs444.parser.symbols;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ISymbolChoiceVisitor;
import cs444.lexer.Token;
import cs444.lexer.Token.Parse;

public final class Terminal extends ATerminal{
    private final boolean empty;

    public Terminal(Token token){
        super(token.type.toString(), token.lexeme);
        empty = Token.typeToParse.get(token.type) != Parse.VALID;
    }

    public boolean empty() {
        return empty;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public void accept(ISymbolChoiceVisitor visitor) {
        visitor.visit(this);
    }
}
