package cs444.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cs444.generator.lexer.dfa.DFAClassGenerator;
import cs444.generator.lexer.grammar.JoosGrammar;
import cs444.generator.lexer.grammar.TokenClassGenerator;
import cs444.generator.parser.ParserGenerator;

public class Program {

    private static void tryClose(final Writer writer) {

        try {
            if (null != writer)
                writer.close();
        } catch (final IOException e) {
            System.err.println("Error closing output file.");
        }
    }

    private static Writer createFileAndWriter(final String loc) throws IOException{
        final File file = new File(loc);
        file.createNewFile();

        return new FileWriter(file);
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args){

        Writer writer = null;
        final JoosGrammar grammar = new JoosGrammar();

        try {
            writer = createFileAndWriter("../Compiler/src/cs444/lexer/Token.java");
            final TokenClassGenerator token = new TokenClassGenerator(grammar, writer);
            token.generate();

        } catch (final IOException e) {

            System.err.println("Error writing to Token.java.");

        } finally {

            tryClose(writer);
        }

        try {
            writer = createFileAndWriter("../Compiler/src/cs444/lexer/JoosDFA.java");
            final DFAClassGenerator lexer = new DFAClassGenerator(grammar, writer);
            lexer.generate();

        } catch (final IOException e) {

            System.err.println("Error writing to JoosDFA.java.");

        } finally {

            tryClose(writer);
        }

        try {
            createCompilerBin();

            writer = createFileAndWriter("../Compiler/bin/JoosRules.txt");

            ParserGenerator.generate(writer);
        } catch (final IOException e) {

            System.err.println("Error writing to JoosRules.txt.");

        } finally {

            tryClose(writer);
        }

        System.out.println("DONE");
    }

    private static void createCompilerBin() {
        final File theDir = new File("../Compiler/bin");
        if (!theDir.exists()){
            final boolean result = theDir.mkdir();
            if(!result){
                System.out.println("Failed creating directory");
            }
        }
    }
}
