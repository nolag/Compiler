package cs444.cfgrulesgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cs444.cfgrulesgenerator.lexer.Lexer;
import cs444.cfgrulesgenerator.lexer.Token;

public class RulesGenerator {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("JavaSyntax.txt"));
            Lexer lexer = new Lexer(reader);

            Token token;
			while((token = lexer.getNextToken()) != null){
            	System.out.println(token);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (null != reader)
                reader.close();
        }
	}
}
