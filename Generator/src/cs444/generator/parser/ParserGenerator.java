package cs444.generator.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ParserGenerator {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("JoosDFA.java"));
            Language language = new JoosSyntacticGrammar(writer);
            language.generate();

        } catch (IOException e) {
            System.err.println("Error writing to JoosDFA.java.");
        } finally {
            writer.close();
        }
    }
}
