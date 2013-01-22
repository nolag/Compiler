package cs444.generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cs444.generator.lexer.dfa.DFAClassGenerator;
import cs444.generator.lexer.grammar.JoosGrammar;
import cs444.generator.lexer.grammar.TokenClassGenerator;

public class Program {

    private static void tryClose(Writer writer) {

        try {
            if (null != writer)
                writer.close();
        } catch (IOException e) {
            System.err.println("Error closing output file.");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        BufferedWriter writer = null;
        JoosGrammar grammar = new JoosGrammar();

        try {

            writer = new BufferedWriter(new FileWriter("Token.java"));
            TokenClassGenerator token = new TokenClassGenerator(grammar, writer);
            token.generate();

        } catch (IOException e) {

            System.err.println("Error writing to Token.java.");

        } finally {

            tryClose(writer);
        }

        try {

            writer = new BufferedWriter(new FileWriter("JoosDFA.java"));
            DFAClassGenerator lexer = new DFAClassGenerator(grammar, writer);
            lexer.generate();

        } catch (IOException e) {

            System.err.println("Error writing to JoosDFA.java.");

        } finally {

            tryClose(writer);
        }
    }
}
