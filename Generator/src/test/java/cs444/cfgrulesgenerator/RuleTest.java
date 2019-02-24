package cs444.cfgrulesgenerator;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.generator.lexer.nfa.transition.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RuleTest extends TestHelper {
    @Test
    public void testGetFirstBNFExpression() throws BNFParseException {
        // N -> A [B] C
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                new Token(Token.Type.NON_TERMINAL, "A"),
                new Token(Token.Type.LBRACKET, "["),
                new Token(Token.Type.NON_TERMINAL, "B"),
                new Token(Token.Type.RBRACKET, "]"),
                new Token(Token.Type.NON_TERMINAL, "C"));

        Range bnfExpr = BNFrule.getFirstBNFExpression();
        assertEquals(1, bnfExpr.from);
        assertEquals(3, bnfExpr.to);
    }

    @Test
    public void testGetFirstBNFExpressionWithNestedExpr() throws BNFParseException {
        // N -> A {[B] C } D
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                new Token(Token.Type.NON_TERMINAL, "A"),
                new Token(Token.Type.LBRACE, "{"),
                new Token(Token.Type.LBRACKET, "["),
                new Token(Token.Type.NON_TERMINAL, "B"),
                new Token(Token.Type.RBRACKET, "]"),
                new Token(Token.Type.NON_TERMINAL, "C"),
                new Token(Token.Type.RBRACE, "}"),
                new Token(Token.Type.NON_TERMINAL, "D"));

        Range bnfExpr = BNFrule.getFirstBNFExpression();
        assertEquals(1, bnfExpr.from);
        assertEquals(6, bnfExpr.to);
    }

    @Test
    public void testGetFirstBNFExpressionUsingParensWithNestedExpr() throws BNFParseException {
        // N -> A A2 ([B] C | D)
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                new Token(Token.Type.NON_TERMINAL, "A"),
                new Token(Token.Type.NON_TERMINAL, "A2"),
                new Token(Token.Type.LPAREN, "("),
                new Token(Token.Type.LBRACKET, "["),
                new Token(Token.Type.NON_TERMINAL, "B"),
                new Token(Token.Type.RBRACKET, "]"),
                new Token(Token.Type.NON_TERMINAL, "C"),
                new Token(Token.Type.PIPE, "|"),
                new Token(Token.Type.NON_TERMINAL, "D"),
                new Token(Token.Type.RPAREN, ")"));

        Range bnfExpr = BNFrule.getFirstBNFExpression();
        assertEquals(2, bnfExpr.from);
        assertEquals(9, bnfExpr.to);
    }

    @Test(expected = BNFParseException.class)
    public void testGetFirstBNFExpressionNoClosing() throws BNFParseException {
        // N -> A [B C
        Rule BNFrule = ruleFor(new Token(Token.Type.LHS, "N:"),
                new Token(Token.Type.NON_TERMINAL, "A"),
                new Token(Token.Type.LBRACKET, "["),
                new Token(Token.Type.NON_TERMINAL, "B"),
                new Token(Token.Type.NON_TERMINAL, "C"));

        BNFrule.getFirstBNFExpression();
    }
}
