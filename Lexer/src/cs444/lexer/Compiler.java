package src.cs444.lexer;

import java.io.BufferedReader;
import java.io.FileReader;

public class Compiler {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        
        BufferedReader reader = null;
        
        try {
            
            reader = new BufferedReader(new FileReader(args[0]));
            Lexer scanner = new Lexer(reader);
            
            Token token;
            while (null != (token = scanner.getNextToken())) {
                
                System.out.println(token.toString());
            }
         
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            if (null != reader)
                reader.close();
        }
    }
}
