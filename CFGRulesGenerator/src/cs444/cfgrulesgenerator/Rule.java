package cs444.cfgrulesgenerator;

import java.util.List;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.cfgrulesgenerator.lexer.Token.Parse;
import cs444.cfgrulesgenerator.lexer.Token.Type;
import cs444.generator.lexer.nfa.transition.Range;

public class Rule{
    public final Token leftHandSide;
    public final List<Token> rightHandSide;

    public Rule(Token leftHandSide, List<Token> righHandSide){
        this.leftHandSide = leftHandSide;
        this.rightHandSide = righHandSide;
    }

    public String toString(){
        StringBuffer result = new StringBuffer();

        // TODO: remove ':' and wrap rules inside "rules.add("...");"
        result.append(leftHandSide.lexeme);

        for (Token symbol : rightHandSide) {
            result.append(" " + symbol.lexeme);
        }

        return result.toString();
    }

    public boolean isInSimpleForm(){
        // TODO
        return true;
    }

    public Range getFirstBNFExpression() throws BNFParseException {
        int fromIndex = 0;
        for(; fromIndex < rightHandSide.size(); fromIndex++){
            if (Token.typeToParse.get(rightHandSide.get(fromIndex).type) == Parse.SYNTAX_ONLY) break;
        }

        // no bnfExpr found
        if (fromIndex == rightHandSide.size()){
        	throw new BNFParseException("No BNF expression found in: " + this.toString());
        }

        int toIndex = fromIndex;
        Token.Type openToken = rightHandSide.get(fromIndex).type;
        Token.Type closeToken = closingTokenFor(openToken);
        for(int tokenCounter = 0; toIndex < rightHandSide.size(); toIndex++){
            Token currentToken = rightHandSide.get(toIndex);
            if (currentToken.type == openToken){
                tokenCounter++;
            }else if(currentToken.type == closeToken){
                tokenCounter--;
            }
            if(tokenCounter == 0) break;
        }

        if(toIndex == rightHandSide.size()){
        	throw new BNFParseException("No closing pair for: '" + openToken + "'");
        }
        return new Range(fromIndex, toIndex);
    }

    private Type closingTokenFor(Type tokenType){
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
}
