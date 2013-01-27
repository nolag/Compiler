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
        rules.add("DCLS int id eq id semi DCLS");
        rules.add("DCLS int id eq decimal_integer_literal semi DCLS");
        rules.add("BECOMES id eq id BECOMES");
        rules.add("BECOMES id plus eq id BECOMES");
        rules.add("BECOMES id minus eq id BECOMES");
        rules.add("BECOMES id eq decimal_integer_literal BECOMES");
        rules.add("BECOMES id plus eq decimal_integer_literal BECOMES");
        rules.add("BECOMES id minus eq decimal_integer_literal BECOMES");
        rules.add("BECOMES ");
        rules.add("DCLS ");
        rules.add("DCLS_BECOMES DCLS BECOMES EOF");
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