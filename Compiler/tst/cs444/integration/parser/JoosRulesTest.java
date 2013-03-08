package cs444.integration.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;

import org.junit.Test;

import cs444.lexer.Lexer;
import cs444.lexer.LexerException;
import cs444.parser.Parser;
import cs444.parser.TextReadingRules;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;

public class JoosRulesTest {

    @Test
    public void testParseEmptyPackage() throws IOException,
                                            LexerException,
                                            UnexpectedTokenException, URISyntaxException {
        parseTreeForFile("EmptyPackage");
    }

    @Test
    public void testParseCompleteCompUnit() throws IOException,
                                                   LexerException,
                                                   UnexpectedTokenException, URISyntaxException {
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
                                                  UnexpectedTokenException, URISyntaxException {
        Parser parser = new Parser(new TextReadingRules());
        parser.parse(new Lexer(new StringReader(code)));
    }

    private ISymbol parseTreeForFile(String fileName) throws IOException,
                                                             LexerException,
                                                             UnexpectedTokenException, URISyntaxException{
        String filePath =  fileName + ".java";
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(new TextReadingRules());

        return parser.parse(lexer);
    }
}
