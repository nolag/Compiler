package cs444.generator;

import cs444.generator.lexer.dfa.DFAClassGenerator;
import cs444.generator.lexer.grammar.JoosGrammar;
import cs444.generator.lexer.grammar.TokenClassGenerator;
import cs444.generator.parser.ParserGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;

public class Program {

    private static void tryClose(Writer writer) {

        try {
            if (null != writer) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing output file.");
        }
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Call must contain exactly one argument for the project directory");
            System.exit(1);
        }

        String projectDir = args[0];
        String compilerMain = Paths.get(projectDir, "Compiler", "src", "main").toString();
        File lexerDir = Paths.get(compilerMain, "java", "cs444", "lexer").toFile();
        JoosGrammar grammar = new JoosGrammar();

        try (FileWriter writer = new FileWriter(new File(lexerDir, "Token.java"))) {
            TokenClassGenerator token = new TokenClassGenerator(grammar, writer);
            token.generate();
        } catch (IOException e) {
            System.err.println("Error writing to Token.java.");
            throw e;
        }

        try (FileWriter writer = new FileWriter(new File(lexerDir, "JoosDFA.java"))) {
            DFAClassGenerator lexer = new DFAClassGenerator(grammar, writer);
            lexer.generate();
        } catch (IOException e) {
            System.err.println("Error writing to JoosDFA.java.");
            throw e;
        }

        try (FileWriter writer = new FileWriter(
                Paths.get(compilerMain, "resources", "JoosRules.txt").toFile())) {
            ParserGenerator.generate(writer);
        } catch (IOException e) {
            System.err.println("Error writing to JoosRules.txt.");
            throw e;
        }

        System.out.println("DONE");
    }
}
