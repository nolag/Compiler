package cs444.cfgrulesgenerator;

import java.util.ArrayList;
import java.util.List;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.lexer.Token;
import cs444.cfgrulesgenerator.lexer.Token.Parse;
import cs444.cfgrulesgenerator.lexer.Token.Type;
import cs444.generator.lexer.nfa.transition.Range;

public class Rule{
    public final Token leftHandSide;
    private final List<Token> rightHandSide;

    public Rule(Token leftHandSide, List<Token> righHandSide){
        this.leftHandSide = leftHandSide;
        this.rightHandSide = replaceTerminalSymbols(righHandSide);
    }

    private List<Token> replaceTerminalSymbols(List<Token> tokens) {
    	List<Token> ret = new ArrayList<Token>();

    	for (Token origToken : tokens) {
            Token newToken;
            Token.Type type = origToken.type;
            if(isALogogram(type)){
                newToken = new Token(type, type.toString().toLowerCase());
            }else{
                newToken = origToken;
            }
            ret.add(newToken);
        }

        return ret;
    }

    public String toString(){
        StringBuffer result = new StringBuffer();

        String lHSLexeme = leftHandSide.lexeme;
        // do not return ':'
        result.append(lHSLexeme.subSequence(0, leftHandSide.lexeme.length() - 1));

        for (Token symbol : rightHandSide) {
            result.append(" " + symbol.lexeme);
        }

        return result.toString();
    }

    public boolean isInSimpleForm(){
        for(int i = 0; i < rightHandSideSize(); i++){
            if (Token.typeToParse.get(rightHandSide.get(i).type) ==
                Parse.SYNTAX_ONLY) return false;
        }

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

    public Token getRightHandSideToken(int index){
    	return this.rightHandSide.get(index);
    }

    public int rightHandSideSize(){
        return this.rightHandSide.size();
    }

    @Override
    public boolean equals(Object obj) {
    	if (!obj.getClass().equals(Rule.class)) return false;

    	Rule other = (Rule) obj;
        // let's use toString for now
        return this.toString().equals(other.toString());
    }
   
    // check if token is representing one of the symbols ; , = + ...
	private boolean isALogogram(Token.Type type) {
		return Token.typeToParse.get(type) == Parse.VALID &&
		   type != Token.Type.LHS &&
		   type != Token.Type.TERMINAL &&
		   type != Token.Type.NON_TERMINAL;
	}

}
