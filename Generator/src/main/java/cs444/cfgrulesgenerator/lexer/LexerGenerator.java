package cs444.cfgrulesgenerator.lexer;

import cs444.generator.lexer.dfa.DFAClassGenerator;
import cs444.generator.lexer.grammar.TokenClassGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class LexerGenerator {
    private static Writer createFileAndWriter(String loc) throws IOException {
        File file = new File(loc);
        file.createNewFile();
        return new FileWriter(file);
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        BNFGrammar grammar = new BNFGrammar();
        String directory = "src/cs444/cfgrulesgenerator/lexer/";

        try (Writer writer = createFileAndWriter(directory + "Token.java")) {
            TokenClassGenerator token = new TokenClassGenerator(grammar, writer, "cs444.cfgrulesgenerator.lexer");
            token.generate();
        } catch (IOException e) {
            System.err.println("Error writing to Token.java.");
        }

        try (Writer writer = createFileAndWriter(directory + "BNFDFA.java")) {
            DFAClassGenerator lexer = new DFAClassGenerator(grammar, writer, "BNFDFA", "cs444.cfgrulesgenerator.lexer");
            lexer.generate();
        } catch (IOException e) {
            System.err.println("Error writing to JoosDFA.java.");
        }
    }
}
