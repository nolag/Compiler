package cs444.cfgrulesgenerator;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.exceptions.UnexpectedTokenException;
import cs444.cfgrulesgenerator.lexer.ILexer;
import cs444.cfgrulesgenerator.lexer.Lexer;
import cs444.cfgrulesgenerator.lexer.LexerException;
import cs444.cfgrulesgenerator.lexer.Token;

public class RuleExpanderTest extends TestHelper{

    @Test
    public void testSquareBrackets() throws BNFParseException {
       	// N -> A [B] C
       	Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.LBRACKET, "["),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.RBRACKET, "]"),
                               new Token(Token.Type.NON_TERMINAL, "C"));

        // N -> A C
       	Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "C"));

        // N -> A B C
       	Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "B"),
                                     new Token(Token.Type.NON_TERMINAL, "C"));

       	List<Rule> expandedRules = RuleExpander.expand(BNFrule);
        assertEquals(2, expandedRules.size());
       	assertEquals(expectedRule1, expandedRules.get(0));
       	assertEquals(expectedRule2, expandedRules.get(1));
    }

    //    @Test
//    public void testBracesFull() {
//    	// N -> A {B} C
//    	Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N"),
//    			new Token(Token.Type.NON_TERMINAL, "A"),
//    			new Token(Token.Type.LBRACE, "{"),
//    			new Token(Token.Type.NON_TERMINAL, "B"),
//    			new Token(Token.Type.RBRACE, "}"),
//    			new Token(Token.Type.NON_TERMINAL, "C"));
//
//    	Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N"),
//    			new Token(Token.Type.NON_TERMINAL, "A"),
//    			new Token(Token.Type.NON_TERMINAL, "N_B1"),
//    			new Token(Token.Type.NON_TERMINAL, "C"));
//
//    	Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N_B1"),
//    			new Token(Token.Type.NON_TERMINAL, "N_B1"),
//    			new Token(Token.Type.NON_TERMINAL, "B"));
//
//    	Rule expectedRule3 = ruleFor(new Token(Token.Type.LHS, "N_B1"),
//    			new Token(Token.Type.EPSILON, " "));
//
//    	List<Rule> expandedRules = RuleExpander.expand(BNFrule);
//    	assertEquals(expectedRule1, expandedRules.get(0));
//    	assertEquals(expectedRule2, expandedRules.get(1));
//    	assertEquals(expectedRule3, expandedRules.get(2));
//    }
}
