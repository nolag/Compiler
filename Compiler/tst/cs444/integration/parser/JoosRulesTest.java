package cs444.integration.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cs444.lexer.Lexer;
import cs444.lexer.LexerException;
import cs444.parser.Parser;
import cs444.parser.rules.JoosRules;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class JoosRulesTest {

    @Test
    public void testSimpleCompUnit() throws IOException,
                                            LexerException,
                                            UnexpectedTokenException {
        ISymbol parseTree = parseTreeFor("SimpleCompUnit");

    }

    private ISymbol parseTreeFor(String fileName) throws IOException,
                                                         LexerException,
                                                         UnexpectedTokenException{
        String filePath = "../fixtures/" + fileName + ".java";
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(new JoosRules());

        return parser.parse(lexer);
    }
}
