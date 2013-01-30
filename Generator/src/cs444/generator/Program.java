package cs444.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cs444.generator.lexer.dfa.DFAClassGenerator;
import cs444.generator.lexer.grammar.JoosGrammar;
import cs444.generator.lexer.grammar.TokenClassGenerator;
import cs444.generator.parser.JoosSyntacticGrammar;
import cs444.generator.parser.Language;

public class Program {

    private static void tryClose(Writer writer) {

        try {
            if (null != writer)
                writer.close();
        } catch (IOException e) {
            System.err.println("Error closing output file.");
        }
    }

    private static Writer createFileAndWriter(String loc) throws IOException{
        File file = new File(loc);
        file.createNewFile();

        return new FileWriter(file);
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args){

        Writer writer = null;
        JoosGrammar grammar = new JoosGrammar();

        try {
            writer = createFileAndWriter("../Compiler/src/cs444/lexer/Token.java");
            TokenClassGenerator token = new TokenClassGenerator(grammar, writer);
            token.generate();

        } catch (IOException e) {

            System.err.println("Error writing to Token.java.");

        } finally {

            tryClose(writer);
        }

        try {
            writer = createFileAndWriter("../Compiler/src/cs444/lexer/JoosDFA.java");
            DFAClassGenerator lexer = new DFAClassGenerator(grammar, writer);
            lexer.generate();

        } catch (IOException e) {

            System.err.println("Error writing to JoosDFA.java.");

        } finally {

            tryClose(writer);
        }

        try {
            writer = createFileAndWriter("../Compiler/src/cs444/parser/JoosDFA.java");
            Language language = new JoosSyntacticGrammar(writer);
            language.generate();

        } catch (IOException e) {
            System.err.println("Error writing to JoosDFA.java.");
        } finally {
            tryClose(writer);
        }
    }
}
