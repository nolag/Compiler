package cs444.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cs444.lexer.ILexer;
import cs444.lexer.LexerException;
import cs444.lexer.Token;
import cs444.parser.rules.TestRule;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class AllTests {

    private final Parser parser = new Parser(new TestRule());

    private static class MockLexer implements ILexer{

        private final Iterator<Token> tokenIt;

        public MockLexer(List<Token> tokens){
            tokenIt = tokens.iterator();
        }

        public Token getNextToken(){
            return tokenIt.next();
        }
    }

    @Test
    public void testGoodSequence() throws IOException, LexerException, UnexpectedTokenException{
        List<Token> tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.Type.INT, "int"));
        tokens.add(new Token(Token.Type.ID, "i"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.DECIMAL_INTEGER_LITERAL, "11"));
        tokens.add(new Token(Token.Type.SEMI, ";"));
        tokens.add(new Token(Token.Type.INT, "int"));
        tokens.add(new Token(Token.Type.ID, "x"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.ID, "q"));
        tokens.add(new Token(Token.Type.SEMI, ";"));
        tokens.add(new Token(Token.Type.EOF, "<EOF>"));
        tokens.add(null);
        MockLexer lexer = new MockLexer(tokens);
        ISymbol start = parser.parse(lexer);

        String expected =  "decl -> <INT, int> <ID, i> <EQ, => <DECIMAL_INTEGER_LITERAL, 11> decl \n"
        		+ "decl -> <INT, int> <ID, x> <EQ, => <ID, q> ";

        assertEquals(expected, start.rule());

        List<Token> tokens2 = new LinkedList<Token>();
        tokens2.add(new Token(Token.Type.ID, "d"));
        tokens2.add(new Token(Token.Type.PLUS, "+"));
        tokens2.add(new Token(Token.Type.EQ, "="));
        tokens2.add(new Token(Token.Type.ID, "o"));
        tokens2.add(new Token(Token.Type.ID, "x"));
        tokens2.add(new Token(Token.Type.MINUS, "-"));
        tokens2.add(new Token(Token.Type.EQ, "="));
        tokens2.add(new Token(Token.Type.DECIMAL_INTEGER_LITERAL, "100"));
        tokens2.add(new Token(Token.Type.EOF, "<EOF>"));
        tokens2.add(null);
        MockLexer lexer2 = new MockLexer(tokens2);
        ISymbol start2 = parser.parse(lexer2);

        String expected2 =  "becomes -> <ID, d> <PLUS, +> <EQ, => <ID, o> becomes \n"
                + "becomes -> <ID, x> <MINUS, -> <EQ, => <DECIMAL_INTEGER_LITERAL, 100> ";

        assertEquals(expected2, start2.rule());
    }

    @Test(expected = UnexpectedTokenException.class)
    public void testBadSequence() throws IOException, LexerException, UnexpectedTokenException{
        List<Token> tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.Type.INT, "int"));
        tokens.add(new Token(Token.Type.ID, "i"));
        tokens.add(new Token(Token.Type.WHITESPACE, "  "));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.DECIMAL_INTEGER_LITERAL, "11"));
        tokens.add(new Token(Token.Type.SEMI, ";"));
        tokens.add(new Token(Token.Type.INT, "int"));
        tokens.add(new Token(Token.Type.ID, "x"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.ID, "q"));
        tokens.add(new Token(Token.Type.SEMI, ";"));
        tokens.add(new Token(Token.Type.ID, "w"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.DECIMAL_INTEGER_LITERAL, "10"));
        tokens.add(new Token(Token.Type.ID, "d"));
        tokens.add(new Token(Token.Type.PLUS, "-"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.EOF, "<EOF>"));
        tokens.add(null);
        MockLexer lexer = new MockLexer(tokens);
        parser.parse(lexer);
    }

    @Test(expected = UnexpectedTokenException.class)
    public void testExcessTokens() throws IOException, LexerException, UnexpectedTokenException{
        List<Token> tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.Type.INT, "int"));
        tokens.add(new Token(Token.Type.ID, "i"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.DECIMAL_INTEGER_LITERAL, "11"));
        tokens.add(new Token(Token.Type.SEMI, ";"));
        tokens.add(new Token(Token.Type.INT, "int"));
        tokens.add(new Token(Token.Type.ID, "x"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.ID, "q"));
        tokens.add(new Token(Token.Type.SEMI, ";"));
        tokens.add(new Token(Token.Type.ID, "w"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.DECIMAL_INTEGER_LITERAL, "10"));
        tokens.add(new Token(Token.Type.ID, "d"));
        tokens.add(new Token(Token.Type.PLUS, "+"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.ID, "o"));
        tokens.add(new Token(Token.Type.INT, "int"));
        tokens.add(new Token(Token.Type.ID, "i"));
        tokens.add(new Token(Token.Type.EQ, "="));
        tokens.add(new Token(Token.Type.DECIMAL_INTEGER_LITERAL, "11"));
        tokens.add(new Token(Token.Type.SEMI, ";"));
        tokens.add(new Token(Token.Type.ID, "l"));
        tokens.add(new Token(Token.Type.EOF, "<EOF>"));
        tokens.add(null);
        MockLexer lexer = new MockLexer(tokens);
        parser.parse(lexer);
    }
}
