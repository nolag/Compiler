package cs444.generator.parser.grammar.rules;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cs444.generator.lexer.grammar.JoosGrammar;
import cs444.generator.parser.Rule;
import cs444.generator.parser.exceptions.UndeclaredRuleException;
import cs444.generator.parser.grammar.Grammar;


public class TestRules{
    public static void main(String args []) throws IOException, UndeclaredRuleException{

        Writer writer = new BufferedWriter(new FileWriter("Tmp.java"));
        Grammar grammar = new Grammar("Tmp", "DCLS_OR_BECOMES", new JoosGrammar(), writer);
        grammar.addRule(Rule.union("DCLS",
                        grammar.makeAddRule("DCLS1", "int id eq id semi DCLS"),
                        grammar.makeAddRule("DCLS2", "int id eq decimal_integer_literal semi DCLS")));

        grammar.addRule(Rule.union("BECOMES",
                grammar.makeAddRule("BECOMES1", "id eq id BECOMES"),
                grammar.makeAddRule("BECOMES2", "id eq decimal_integer_literal BECOMES"),
                grammar.makeAddRule("BECOMES3", "id plus eq id BECOMES"),
                grammar.makeAddRule("BECOMES4", "id plus eq decimal_integer_literal BECOMES")));

        grammar.addRule(Rule.union("DCLS_OR_BECOMES",
                grammar.getRule("DCLS"),
                grammar.getRule("BECOMES")));

        grammar.generate();

        writer.close();

    }
}
