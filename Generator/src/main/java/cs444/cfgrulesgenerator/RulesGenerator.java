package cs444.cfgrulesgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cs444.cfgrulesgenerator.exceptions.BNFParseException;
import cs444.cfgrulesgenerator.exceptions.UnexpectedTokenException;
import cs444.cfgrulesgenerator.lexer.Lexer;
import cs444.cfgrulesgenerator.lexer.LexerException;

public class RulesGenerator {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("JoosSyntax.txt"));
            IRulesFactory rulesFactory = new RulesFactory(new Lexer(reader));

//            generateSyntacticGrammarClass(rulesFactory);

            // for debugging
            generateSimpleOutput(rulesFactory);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader)
                reader.close();
        }
    }

    @SuppressWarnings("unused")
	private static void generateSyntacticGrammarClass(IRulesFactory rulesFactory)
        throws IOException{

        Writer writer = null;

        try{
            String filePath = "JoosSyntacticGrammar.java";
            writer = new FileWriter(new File(filePath));
            SyntacticGrammarClassGenerator generator = new SyntacticGrammarClassGenerator(writer, rulesFactory, "JoosSyntacticGrammar");

            generator.generate();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if (null != writer)
                writer.close();
        }
    }

    
    // for testing and debugging
	private static void generateSimpleOutput(IRulesFactory rulesFactory)
        throws UnexpectedTokenException, LexerException, IOException,
               BNFParseException{
        Rule rule;
        while((rule = rulesFactory.getNextRule()) != null){
            System.out.println(rule);
        }
    }
}
