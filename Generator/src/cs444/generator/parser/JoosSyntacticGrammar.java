package cs444.generator.parser;

import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import cs444.generator.lexer.grammar.JoosGrammar;

public class JoosSyntacticGrammar extends Language {

    private static final JoosGrammar tokenGrammar = new JoosGrammar();
    private static final String startRule = "DCLS_BECOMES";
    private static final List<String> rules = new LinkedList<String>();

    static{
        rules.add("DCLS_BECOMES id EOF");
    }

    JoosSyntacticGrammar(Writer writer){
        super(writer, rules, startRule, tokenGrammar, "JoosRules");
    }
}
