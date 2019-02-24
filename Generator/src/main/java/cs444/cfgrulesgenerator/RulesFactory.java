package cs444.cfgrulesgenerator;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.exceptions.UnexpectedTokenException;
import cs444.cfgrulesgenerator.lexer.ILexer;
import cs444.cfgrulesgenerator.lexer.LexerException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.cfgrulesgenerator.lexer.Token.Parse;

import java.io.IOException;
import java.util.*;

public class RulesFactory implements IRulesFactory {
    private final ILexer lexer;
    private final Queue<Rule> buffer;
    private final RuleExpander ruleExpander = new RuleExpander();
    private Token currentLHS = null;

    public RulesFactory(ILexer lexer) {
        this.lexer = lexer;
        buffer = new LinkedList<>();
    }

    // return null when no more rules
    public Rule getNextRule() throws UnexpectedTokenException, LexerException, IOException, BNFParseException {
        if (!buffer.isEmpty()) {
            return getNextExpandedRuleFromBuffer();
        }

        Token token = getNextRelevantToken();

        if (token.type == Token.Type.EOF) {
            return null;
        }

        List<Token> rightHandSide = new ArrayList<Token>();
        if (token.type == Token.Type.LHS) {
            currentLHS = token;
            token = getNextRelevantToken(); // get first symbol of RHS
        }

        rightHandSide.add(token); // add first symbol of rightHandSide
        if (currentLHS != null) {
            Rule initialRule = getNextBNFRule(rightHandSide); // add rest of symbols
            buffer.add(initialRule);
            return getNextExpandedRuleFromBuffer();
        } else {                  // right hand side without left hand side => ERROR
            Set<String> expected = new TreeSet<>();
            expected.add(Token.Type.LHS.toString());
            throw new UnexpectedTokenException(token, expected);
        }
    }

    // gets next expanded rule from buffer
    private Rule getNextExpandedRuleFromBuffer() throws BNFParseException {
        Rule rule;
        while (!(rule = buffer.remove()).isInSimpleForm()) {
            buffer.addAll(ruleExpander.expand(rule));
        }

        return rule;
    }

    private Rule getNextBNFRule(List<Token> rightHandSide) throws LexerException, IOException {
        Token token;

        // there is at most a rule per line
        while ((token = lexer.getNextToken()).type != Token.Type.EOF && token.type != Token.Type.NEWLINE) {
            if (Token.typeToParse.get(token.type) == Parse.IGNORE) {
                continue;
            }
            rightHandSide.add(token);
        }

        return new Rule(currentLHS, rightHandSide);
    }

    private Token getNextRelevantToken() throws LexerException, IOException {
        Token token;

        // skip all whitespace and newline until first symbol
        while ((token = lexer.getNextToken()).type != Token.Type.EOF &&
                (Token.typeToParse.get(token.type) == Parse.IGNORE ||
                        token.type == Token.Type.NEWLINE)) {
        }

        return token;
    }
}
