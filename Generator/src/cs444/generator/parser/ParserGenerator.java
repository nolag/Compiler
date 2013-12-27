package cs444.generator.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cs444.cfgrulesgenerator.IRulesFactory;
import cs444.cfgrulesgenerator.RulesFactory;
import cs444.cfgrulesgenerator.lexer.Lexer;

public class ParserGenerator {

    public static void generate(Writer writer) throws IOException{
        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader("JoosSyntax.txt"));
        IRulesFactory rulesFactory = new RulesFactory(new Lexer(reader));

        Language language = new JoosSyntacticGrammar(writer, rulesFactory);
        language.generate();
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("JoosDFA.java"));

            generate(writer);

        } catch (IOException e) {
            System.err.println("Error writing to JoosDFA.java.");
        } finally {
            if (writer != null) writer.close();
        }
    }
}
