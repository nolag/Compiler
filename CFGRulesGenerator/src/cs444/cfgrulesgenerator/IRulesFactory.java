package cs444.cfgrulesgenerator;

import java.io.IOException;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.exceptions.UnexpectedTokenException;
import cs444.cfgrulesgenerator.lexer.LexerException;

public interface IRulesFactory {
	public Rule getNextRule() throws UnexpectedTokenException, LexerException, IOException, BNFParseException;
}
