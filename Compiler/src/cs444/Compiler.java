package cs444;

import java.io.BufferedReader;
import java.io.FileReader;

import cs444.lexer.Lexer;
import cs444.parser.JoosDFA;
import cs444.parser.Parser;
import cs444.parser.symbols.ISymbol;

public class Compiler {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new FileReader(args[0]));
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(new JoosDFA());

            ISymbol parseTree = parser.parse(lexer);

            System.out.println(parseTree.rule());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (null != reader)
                reader.close();
        }
    }
}
