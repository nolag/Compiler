package cs444.cfgrulesgenerator;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;

public class RuleExpanderTest extends TestHelper{
    @Test
    public void testSquareBrackets() throws BNFParseException {
       	// N -> A [B] C
       	Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.LBRACKET, "["),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.RBRACKET, "]"),
                               new Token(Token.Type.NON_TERMINAL, "C"));

        // N -> A C
       	Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "C"));

        // N -> A B C
       	Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "B"),
                                     new Token(Token.Type.NON_TERMINAL, "C"));

       	List<Rule> expandedRules = new RuleExpander().expand(BNFrule);
        assertEquals(2, expandedRules.size());
       	assertEquals(expectedRule1.toString(), expandedRules.get(0).toString());
       	assertEquals(expectedRule2.toString(), expandedRules.get(1).toString());
    }

    @Test
    public void testSquareBracketsMultiSymbols() throws BNFParseException {
       	// N -> A [B C]
       	Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.LBRACKET, "["),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.NON_TERMINAL, "C"),
                               new Token(Token.Type.RBRACKET, "]"));

        // N -> A
       	Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"));

        // N -> A B C
       	Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "B"),
                                     new Token(Token.Type.NON_TERMINAL, "C"));

       	List<Rule> expandedRules = new RuleExpander().expand(BNFrule);
        assertEquals(2, expandedRules.size());
       	assertEquals(expectedRule1.toString(), expandedRules.get(0).toString());
       	assertEquals(expectedRule2.toString(), expandedRules.get(1).toString());
    }

    @Test
    public void testBracesFull() throws BNFParseException {
        // N -> A {B} C
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.LBRACE, "{"),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.RBRACE, "}"),
                               new Token(Token.Type.NON_TERMINAL, "C"));

        // N -> A N_B_0 C
        Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "N_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "C"));

        // N_B1 -> N_B_0 B
        Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "N_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "B"));

        // N_B_0 -> epsilon
        Rule expectedRule3 = ruleFor(new Token(Token.Type.LHS, "N_B_0"),
                                     new Token(Token.Type.EPSILON, " "));

        List<Rule> expandedRules = new RuleExpander().expand(BNFrule);
        assertEquals(expectedRule1, expandedRules.get(0));
        assertEquals(expectedRule2, expandedRules.get(1));
        assertEquals(expectedRule3, expandedRules.get(2));
    }

    @Test
    public void testBracesSimple() throws BNFParseException {
        // N -> {B}
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.LBRACE, "{"),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.RBRACE, "}"));

        // N -> N_B_0
        Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "N_B_0"));

        // N_B_0 -> N_B_0 B
        Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "N_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "B"));

        // N_B_0 -> epsilon
        Rule expectedRule3 = ruleFor(new Token(Token.Type.LHS, "N_B_0"),
                                     new Token(Token.Type.EPSILON, " "));

        List<Rule> expandedRules = new RuleExpander().expand(BNFrule);
        assertEquals(expectedRule1, expandedRules.get(0));
        assertEquals(expectedRule2, expandedRules.get(1));
        assertEquals(expectedRule3, expandedRules.get(2));
    }

    @Test
    public void testBracesMultiSymbols() throws BNFParseException {
        // N -> {A B} C
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.LBRACE, "{"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.RBRACE, "}"),
                               new Token(Token.Type.NON_TERMINAL, "C"));

        // N -> N_A_B_0 C
        Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "N_A_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "C"));

        // N_A_B_0 -> N_A_B_0 A B
        Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N_A_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "N_A_B_0"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "B"));

        // N_A_B_0 -> epsilon
        Rule expectedRule3 = ruleFor(new Token(Token.Type.LHS, "N_A_B_0"),
                                     new Token(Token.Type.EPSILON, " "));

        List<Rule> expandedRules = new RuleExpander().expand(BNFrule);
        assertEquals(expectedRule1, expandedRules.get(0));
        assertEquals(expectedRule2, expandedRules.get(1));
        assertEquals(expectedRule3, expandedRules.get(2));
    }

    @Test
    public void testBracesReuseNewRules() throws BNFParseException {
        // N -> A {B}
        Rule BNFrule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.LBRACE, "{"),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.RBRACE, "}"));

        // M -> {B} C
        Rule BNFrule2 = ruleFor(new Token(Token.Type.LHS, "M:"),
                               new Token(Token.Type.LBRACE, "{"),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.RBRACE, "}"),
                               new Token(Token.Type.NON_TERMINAL, "C"));

        // The result should be:
        // N -> A N_B_0
        // N -> N_B_0 C
        // N_B_0 -> N_B_0 B
        // N_B_0 -> epsilon
        RuleExpander expander = new RuleExpander();
        List<Rule> expandedRules = expander.expand(BNFrule1);
        expandedRules.addAll(expander.expand(BNFrule2));

        assertEquals(4, expandedRules.size());
    }

    @Test
    public void testOr() throws BNFParseException {
        // N -> A (B | C) D
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.LPAREN, "("),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.PIPE, "|"),
                               new Token(Token.Type.NON_TERMINAL, "C"),
                               new Token(Token.Type.RPAREN, ")"),
                               new Token(Token.Type.NON_TERMINAL, "D"));

        // N -> A B D
        Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "B"),
                                     new Token(Token.Type.NON_TERMINAL, "D"));

        // N: -> A C D
        Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "C"),
                                     new Token(Token.Type.NON_TERMINAL, "D"));

        List<Rule> expandedRules = new RuleExpander().expand(BNFrule);
        assertEquals(expectedRule1, expandedRules.get(0));
        assertEquals(expectedRule2, expandedRules.get(1));
    }

    @Test
    public void testMultiOrs() throws BNFParseException {
        // N -> A (B | C D | E)
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                               new Token(Token.Type.NON_TERMINAL, "A"),
                               new Token(Token.Type.LPAREN, "("),
                               new Token(Token.Type.NON_TERMINAL, "B"),
                               new Token(Token.Type.PIPE, "|"),
                               new Token(Token.Type.NON_TERMINAL, "C"),
                               new Token(Token.Type.NON_TERMINAL, "D"),
                               new Token(Token.Type.PIPE, "|"),
                               new Token(Token.Type.NON_TERMINAL, "E"),
                               new Token(Token.Type.RPAREN, ")"));

        // N -> A B
        Rule expectedRule1 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "B"));

        // N: -> A C D
        Rule expectedRule2 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "C"),
                                     new Token(Token.Type.NON_TERMINAL, "D"));

        // N -> A E
        Rule expectedRule3 = ruleFor(new Token(Token.Type.LHS, "N:"),
                                     new Token(Token.Type.NON_TERMINAL, "A"),
                                     new Token(Token.Type.NON_TERMINAL, "E"));

        List<Rule> expandedRules = new RuleExpander().expand(BNFrule);
        assertEquals(expectedRule1, expandedRules.get(0));
        assertEquals(expectedRule2, expandedRules.get(1));
        assertEquals(expectedRule3, expandedRules.get(2));
    }
}
