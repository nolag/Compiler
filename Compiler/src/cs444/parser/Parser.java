package cs444.parser;

import java.io.IOException;

import cs444.lexer.ILexer;
import cs444.lexer.LexerException;
import cs444.lexer.Token;
import cs444.lexer.Token.Parse;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.StateTerminal;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;
import cs444.parser.symbols.factories.ISymbolFactory;

public class Parser {
    private final ISymbolFactory startFactory;

    public Parser(IParserRule rule){
        this.startFactory = rule.getStartSymbol();
    }

    //TODO make this return a tree
    public ISymbol parse(ILexer lexer) throws IOException, LexerException, UnexpectedTokenException{
        ISymbol start = startFactory.create();
        Token token;
        while((token = lexer.getNextToken()) != null){
            if(Token.typeToParse.get(token.type) == Parse.IGNORE) continue;
            if(start.giveToken(token)){
                if(token.type !=  Token.Type.EOF) throw new UnexpectedTokenException(token, new StateTerminal [] {});
            }
        }
        return start;
    }
}
