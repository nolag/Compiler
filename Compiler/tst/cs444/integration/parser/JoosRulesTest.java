package cs444.integration.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cs444.lexer.Lexer;
import cs444.lexer.LexerException;
import cs444.parser.Parser;
import cs444.parser.JoosDFA;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class JoosRulesTest {

    @Test
    public void testEmptyPackage() throws IOException,
                                            LexerException,
                                            UnexpectedTokenException {
        ISymbol parseTree = parseTreeFor("EmptyPackage");

        String expected =  "COMPILATIONUNIT -> PACKAGEDECLARATION \n" +
            "PACKAGEDECLARATION -> PACKAGE ID \n" +
            "PACKAGE -> package\n" +
            "ID -> mypackage";

        assertEquals(expected, parseTree.rule());
    }

    private ISymbol parseTreeFor(String fileName) throws IOException,
                                                         LexerException,
                                                         UnexpectedTokenException{
        String filePath = "tst/cs444/integration/fixtures/" + fileName + ".java";
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(new JoosDFA());

        return parser.parse(lexer);
    }
}
