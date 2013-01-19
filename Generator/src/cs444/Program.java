package cs444;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cs444.grammar.JoosGrammar;

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
            
            writer = new BufferedWriter(new FileWriter("Lexer.java"));
            LexerClassGenerator lexer = new LexerClassGenerator(grammar, writer);
            lexer.generate();
            
        } catch (IOException e) {

            System.err.println("Error writing to Lexer.java.");
            
        } finally {
            
            tryClose(writer);
        }
    }
}
