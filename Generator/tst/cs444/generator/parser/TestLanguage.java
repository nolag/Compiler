package cs444.generator.parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import cs444.generator.lexer.grammar.JoosGrammar;


public class TestLanguage extends Language{

    private static final JoosGrammar tokenGrammar = new JoosGrammar();
    private static final String startRule = "DCLS_BECOMES";
    private static final List<String> rules = new LinkedList<String>();

    static{
        rules.add("ID_NUM id");
        rules.add("ID_NUM decimal_integer_literal");
        rules.add("ASSIGN id eq ID_NUM");
        rules.add("ASSIGN id plus eq ID_NUM");
        rules.add("ASSIGN id minus eq ID_NUM");
        rules.add("DCL int id eq ID_NUM SEMI");
        rules.add("ASSIGNS ASSIGN ASSIGNS");
        rules.add("ASSIGNS ");
        rules.add("DCLS DCLS DCL");
        rules.add("DCLS ");
        rules.add("DCLS_BECOMES DCLS ASSIGNS");
    }

    private TestLanguage(Writer writer){
        super(writer, rules, startRule, tokenGrammar, "TestRule");
    }

    public static void main(String args[]) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        Language lang = new TestLanguage(writer);
        lang.generate();
        writer.close();
    }
}
