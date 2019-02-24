package cs444.cfgrulesgenerator;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.exceptions.UnexpectedTokenException;
import cs444.cfgrulesgenerator.lexer.Lexer;
import cs444.cfgrulesgenerator.lexer.LexerException;

import java.io.*;

public class RulesGenerator {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("JoosSyntax.txt"))) {
            IRulesFactory rulesFactory = new RulesFactory(new Lexer(reader));

            // generateSyntacticGrammarClass(rulesFactory);

            // for debugging
            generateSimpleOutput(rulesFactory);
        } catch (IOException | BNFParseException | UnexpectedTokenException | LexerException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("unused")
    private static void generateSyntacticGrammarClass(IRulesFactory rulesFactory) throws IOException {
        String filePath = "JoosSyntacticGrammar.java";
        try (Writer writer = new FileWriter(new File(filePath))) {
            new SyntacticGrammarClassGenerator(writer, rulesFactory, "JoosSyntacticGrammar").generate();
        }
    }

    // for testing and debugging
    private static void generateSimpleOutput(IRulesFactory rulesFactory)
            throws UnexpectedTokenException, LexerException, IOException, BNFParseException {
        Rule rule;
        while ((rule = rulesFactory.getNextRule()) != null) {
            System.out.println(rule);
        }
    }
}
