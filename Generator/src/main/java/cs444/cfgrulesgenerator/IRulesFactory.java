package cs444.cfgrulesgenerator;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.exceptions.UnexpectedTokenException;
import cs444.cfgrulesgenerator.lexer.LexerException;

import java.io.IOException;

public interface IRulesFactory {
    Rule getNextRule() throws UnexpectedTokenException, LexerException, IOException, BNFParseException;
}
