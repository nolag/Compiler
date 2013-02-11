package cs444;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import cs444.lexer.Lexer;
import cs444.parser.IASTBuilder;
import cs444.parser.JoosASTBuilder;
import cs444.parser.Parser;
import cs444.parser.TextReadingRules;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public class Compiler {

    private static void die(Exception e){
        e.printStackTrace();
        System.exit(42);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args){

        Reader reader = null;
        NonTerminal parseTree = null;

        try {

            reader = new FileReader(/*args[0]*/ "JoosPrograms/SyntacticallyValidPrograms/Implements.java");
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(new TextReadingRules(new File("JoosRules.txt")));

            parseTree = parser.parse(lexer);

            System.out.println(parseTree.rule());

        } catch (Exception e) {
            die(e);
        } finally {

            if (null != reader){
                try {
                    reader.close();
                } catch (IOException e) {
                    die(e);
                }
            }
        }

        IASTBuilder builder = new JoosASTBuilder();

        for(ASTSymbolFactory astSymbol : builder.getSimplifcations()){
            try {
                astSymbol.convertAll(parseTree);
            } catch (Exception e) {
                die(e);
            }
        }
    }
}
