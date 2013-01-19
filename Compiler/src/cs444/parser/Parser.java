package cs444.parser;

import java.io.IOException;

import cs444.lexer.ILexer;
import cs444.lexer.LexerException;
import cs444.lexer.Token;
import cs444.parser.factory.ISymbolFactory;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.StateTerminal;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class Parser {
    private final ISymbolFactory startFactory;

    public Parser(IParserRule rule){
        this.startFactory = rule.getStartSymbol();
    }

    //TODO make this return a tree
    public void parse(ILexer lexer) throws IOException, LexerException, UnexpectedTokenException{
        ISymbol start = startFactory.create();
        Token token;
        while((token = lexer.getNextToken()) != null){
            if(start.giveToken(token)){
                if(token.getType() !=  Token.Type.EOF) throw new UnexpectedTokenException(token, new StateTerminal [] {});
            }
        }
        //TODO make the returned tree here :)
    }
}
