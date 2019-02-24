package cs444.cfgrulesgenerator;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.cfgrulesgenerator.lexer.Token.Parse;
import cs444.cfgrulesgenerator.lexer.Token.Type;
import cs444.generator.lexer.nfa.transition.Range;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    public final Token leftHandSide;
    private final List<Token> rightHandSide;

    public Rule(Token leftHandSide, List<Token> righHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = replaceTerminalSymbols(righHandSide);
    }

    private List<Token> replaceTerminalSymbols(List<Token> tokens) {
        List<Token> ret = new ArrayList<>();

        for (Token origToken : tokens) {
            Token.Type type = origToken.type;
            if (isALogogram(type)) {
                ret.add(new Token(type, type.toString().toLowerCase()));
            } else {
                ret.add(origToken);
            }
        }

        return ret;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        String lHSLexeme = leftHandSide.lexeme;
        // do not return ':'
        int lastChar = lHSLexeme.length() - 1;
        if (lHSLexeme.charAt(lastChar) == ':') {
            result.append(lHSLexeme.subSequence(0, lHSLexeme.length() - 1));
        } else {
            result.append(lHSLexeme);
        }

        for (Token symbol : rightHandSide) {
            result.append(" " + symbol.lexeme);
        }

        return result.toString();
    }

    public boolean isInSimpleForm() {
        return rightHandSide.stream().noneMatch(t -> Token.typeToParse.get(t.type) == Parse.SYNTAX_ONLY);
    }

    public Range getFirstBNFExpression() throws BNFParseException {
        int fromIndex = 0;
        for (; fromIndex < rightHandSide.size(); fromIndex++) {
            if (Token.typeToParse.get(rightHandSide.get(fromIndex).type) == Parse.SYNTAX_ONLY) {
                break;
            }
        }

        // no bnfExpr found
        if (fromIndex == rightHandSide.size()) {
            throw new BNFParseException("No BNF expression found in: " + this);
        }

        int toIndex = fromIndex;
        Token.Type openToken = rightHandSide.get(fromIndex).type;
        Token.Type closeToken = closingTokenFor(openToken);
        for (int tokenCounter = 0; toIndex < rightHandSide.size(); toIndex++) {
            Token currentToken = rightHandSide.get(toIndex);
            if (currentToken.type == openToken) {
                tokenCounter++;
            } else if (currentToken.type == closeToken) {
                tokenCounter--;
            }

            if (tokenCounter == 0) {
                break;
            }
        }

        if (toIndex == rightHandSide.size()) {
            throw new BNFParseException("No closing pair for: '" + openToken + "'");
        }
        return new Range(fromIndex, toIndex);
    }

    private Type closingTokenFor(Type tokenType) {
        switch (tokenType) {
            case LBRACKET:
                return Token.Type.RBRACKET;
            case LPAREN:
                return Token.Type.RPAREN;
            case LBRACE:
                return Token.Type.RBRACE;
            default:
                return null;
        }
    }

    public Token getRightHandSideToken(int index) {
        return rightHandSide.get(index);
    }

    public int rightHandSideSize() {
        return rightHandSide.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Rule)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        Rule other = (Rule) obj;
        // let's use toString for now
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    // check if token is representing one of the symbols ; , = + ...
    private boolean isALogogram(Token.Type type) {
        return Token.typeToParse.get(type) == Parse.VALID &&
                type != Token.Type.LHS &&
                type != Token.Type.TERMINAL &&
                type != Token.Type.NON_TERMINAL;
    }
}
