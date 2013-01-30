package cs444.integration.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import cs444.TestHelper;
import cs444.lexer.Lexer;
import cs444.lexer.LexerException;
import cs444.parser.Parser;
import cs444.parser.JoosDFA;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class JoosRulesTest {

    @Test
    public void testParseEmptyPackage() throws IOException,
                                            LexerException,
                                            UnexpectedTokenException {
        ISymbol parseTree = parseTreeForFile("EmptyPackage");

        String expected =  "COMPILATIONUNIT -> PACKAGEDECLARATION \n" +
            "PACKAGEDECLARATION -> PACKAGE PACKAGENAME \n" +
            "PACKAGE -> package\n" +
            "PACKAGENAME -> ID \n" +
            "ID -> mypackage";

        assertEquals(expected, parseTree.rule());
    }

    @Test
    public void testParseCompleteCompUnit() throws IOException,
                                                   LexerException,
                                                   UnexpectedTokenException {
        parseTreeForFile("CompleteCompUnit");
    }

    @Test
    public void testParseMultiplesImportDcl() throws Exception {
        String code = "import importedpackages;\n" +
            "import another._package;\n";

        parseTreeFor(code);
    }

    @Test(expected = UnexpectedTokenException.class)
    public void testParseErrorOnImportPackName() throws Exception {
        String code = "import importedpackages;\n" +
            "import another.;\n" +
            "public class MyClass { }";

        parseTreeFor(code);
    }

    @Test(expected = UnexpectedTokenException.class)
    public void testParseErrorOnMissingClassKeyword() throws Exception {
        String code = "public MyClass { }";

        parseTreeFor(code);
    }

    @Test
    public void testParseExtendsAndImplements() throws Exception {
        String code = "public class MyClass extends SuperClass implements a._interface.somewhere , AnotherInterface { }";

        parseTreeFor(code);
    }

    @Test(expected = UnexpectedTokenException.class)
    public void testParseErrorMissingInterface() throws Exception {
        String code = "public class MyClass extends SuperClass implements a._interface.somewhere , { }";

        parseTreeFor(code);
    }

    private void parseTreeFor(String code) throws IOException, LexerException,
                                                  UnexpectedTokenException {
        Parser parser = new Parser(new JoosDFA());
        parser.parse(new Lexer(new StringReader(code)));
    }

    private ISymbol parseTreeForFile(String fileName) throws IOException,
                                                             LexerException,
                                                             UnexpectedTokenException{
        String filePath = "tst/cs444/integration/fixtures/" + fileName + ".java";
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(new JoosDFA());

        return parser.parse(lexer);
    }
}
