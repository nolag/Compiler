package cs444.cfgrulesgenerator;

import cs444.cfgrulesgenerator.lexer.Token;

public class Rule{
    public final Token leftHandSide;
    public Token[] righHandSide;

    public Rule(Token leftHandSide, Token[] righHandSide){
        this.leftHandSide = leftHandSide;
        this.righHandSide = righHandSide;
    }
}
