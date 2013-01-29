package cs444.lexer;

import java.io.IOException;
import java.io.Reader;

public class Lexer implements ILexer{
    private final Reader reader;
	private boolean emittedLastToken;
	private boolean emittedEOF;
    private int nextChar;
    private IDFA dfa;

    public Lexer(Reader reader) throws IOException {
        this.reader = reader;
        nextChar = reader.read();
        this.dfa = new JoosDFA();
    }

    public Token getNextToken() throws LexerException, IOException {
        String lexeme = "";
        LexerState state = dfa.getInitialState();
        while (nextChar != -1) {
            int previewState = state.getNextState(nextChar);
            if (previewState == -1) {
                if (state.isAccepting())
                    return state.createToken(lexeme);
                else
                    throw new LexerException("Error scanning " +
                    		lexeme + (char)nextChar + ".");
            } else {
                lexeme += (char)nextChar;
                nextChar = reader.read();
                state = dfa.getState(previewState);
            }
        }
		
        if (!emittedLastToken) {
        	
        	emittedLastToken = true;
        	if (state.isAccepting())
        		return state.createToken(lexeme);
        	else
        		throw new LexerException("Error scanning " +
                		lexeme + (char)nextChar + ".");
        	
        } else if (emittedLastToken && !emittedEOF) {
			
        	emittedEOF = true;
			return new Token(Token.Type.EOF, "");
		}
        
        if (lexeme != "") {
        	throw new LexerException("Unexpected end of file reached while scanning a token");
        }

        return null;
    }
}
