package cs444;

import java.io.BufferedReader;
import java.io.FileReader;

import cs444.lexer.Lexer;
import cs444.parser.IASTBuilder;
import cs444.parser.JoosASTBuilder;
import cs444.parser.JoosDFA;
import cs444.parser.Parser;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;

public class Compiler {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        BufferedReader reader = null;
        NonTerminal parseTree = null;

        try {

            reader = new BufferedReader(new FileReader(args[0]));
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(new JoosDFA());

            parseTree = parser.parse(lexer);

            System.out.println(parseTree.rule());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (null != reader)
                reader.close();
        }

        IASTBuilder builder = new JoosASTBuilder();

        for(ASTSymbolFactory astSymbol : builder.getSimplifcations()){
            astSymbol.convertAll(parseTree);
        }
    }
}
