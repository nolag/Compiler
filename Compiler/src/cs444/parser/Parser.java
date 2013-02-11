package cs444.parser;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import cs444.lexer.ILexer;
import cs444.lexer.LexerException;
import cs444.lexer.Token;
import cs444.lexer.Token.Parse;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.SymbolState;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class Parser {
    private final Map<Integer, Map<String, SymbolState>> states;

    private final Stack<ISymbol> symbolStack = new Stack<ISymbol>();
    private final Stack<Integer> stateStack = new Stack<Integer>();

    public Parser(IParserRule rule) throws IOException{
        states = rule.getRules();
    }

    //TODO make this return a tree
    public NonTerminal parse(ILexer lexer) throws IOException, LexerException, UnexpectedTokenException{
        Token token;
        stateStack.push(0);
        Map<String, SymbolState> nextStates = states.get(0);

        while((token = lexer.getNextToken()) != null){
            if(Token.typeToParse.get(token.type) == Parse.IGNORE) continue;
            Terminal terminal = new Terminal(token);
            SymbolState symbolState = nextStates.get(terminal.token.type.toString());

            if(symbolState == null) throw new UnexpectedTokenException(token, nextStates.keySet() );

            while(symbolState.shouldReduce()){
                int numReduce = symbolState.numRed;
                ISymbol [] children = new ISymbol[numReduce];

                for(int i = 0; i < numReduce; i++){
                    children[numReduce - 1 - i] = symbolStack.pop();
                    stateStack.pop();

                }
                nextStates = states.get(stateStack.peek());
                NonTerminal nonTerminal = symbolState.factory.create(children);
                symbolState = nextStates.get(nonTerminal.getName());
                if(symbolState == null) throw new UnexpectedTokenException(token, nextStates.keySet() );
                symbolStack.push(nonTerminal);
                stateStack.push(symbolState.to);
                nextStates = states.get(symbolState.to);
                symbolState = nextStates.get(terminal.token.type.toString());
                if(symbolState == null) throw new UnexpectedTokenException(token, nextStates.keySet() );
            }

            stateStack.push(symbolState.to);
            nextStates = states.get(symbolState.to);
            symbolStack.push(terminal);
        }
        //Pop off EOF
        symbolStack.pop();
        return (NonTerminal)symbolStack.lastElement();
    }
}
