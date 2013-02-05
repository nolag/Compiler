package cs444.parser.symbols;

import java.util.LinkedList;
import java.util.List;

import cs444.lexer.Token;

public class Terminal implements ISymbol{

    public final Token token;

    public static final List<ISymbol> nochildren = new LinkedList<ISymbol>();

    public Terminal(Token token){
        this.token = token;
    }

    public String getType() {
        return token.type.name();
    }

	public String rule() {
		return token.type.toString() + " -> " + token.lexeme;
	}

	public String getName() {
	    if(Token.typeToParse.get(token.type) != Token.Parse.VALID) return "";
		return token.type.toString();
	}

    public boolean empty() {
        return Token.typeToParse.get(token.type) != Token.Parse.VALID;
    }

    @Override
    public Iterable<ISymbol> getChildren() {
        return nochildren;
    }
}
