package cs444.generator.parser;

import cs444.cfgrulesgenerator.IRulesFactory;
import cs444.cfgrulesgenerator.RulesFactory;
import cs444.cfgrulesgenerator.lexer.Lexer;

import java.io.*;

public class ParserGenerator {

    public static void generate(Writer writer) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(ParserGenerator.class.getClassLoader().getResource("JoosSyntax.txt").getFile()))) {
            IRulesFactory rulesFactory = new RulesFactory(new Lexer(reader));
            Language language = new JoosSyntacticGrammar(writer, rulesFactory);
            language.generate();
        }
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("JoosDFA.java"))) {
            generate(writer);
        } catch (IOException e) {
            System.err.println("Error writing to JoosDFA.java.");
            throw e;
        }
    }
}
