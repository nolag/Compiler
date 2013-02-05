package cs444.cfgrulesgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cs444.cfgrulesgenerator.lexer.Lexer;

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

            Rule rule;
            while((rule = rulesFactory.getNextRule()) != null){
            	System.out.println(rule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (null != reader)
                reader.close();
        }
    }
}
