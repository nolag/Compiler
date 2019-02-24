package cs444.generator.parser;

import cs444.cfgrulesgenerator.IRulesFactory;
import cs444.cfgrulesgenerator.Rule;
import cs444.generator.lexer.grammar.JoosGrammar;

import java.io.IOException;
import java.io.Writer;

public class JoosSyntacticGrammar extends Language {

    private static final JoosGrammar tokenGrammar = new JoosGrammar();
    private static final String startRule = "CompilationUnit";
    private IRulesFactory rulesFactory;

    public JoosSyntacticGrammar(Writer writer, IRulesFactory rulesFactory) {
        super(writer, startRule, tokenGrammar, "JoosDFA");
        this.rulesFactory = rulesFactory;
    }

    @Override
    public void generate() throws IOException {
        Rule rule;
        try {
            while ((rule = rulesFactory.getNextRule()) != null) {
                rules.add(rule.toString());
            }
        } catch (Exception e) {
            String eMessage = "Error generating Syntactic Grammar because an " +
                    "exception was thrown: " + e.getClass() + ". Message: " +
                    e.getMessage();

            writeLine(eMessage);
            throw new IOException(eMessage, e); // propagate
        }
        super.generate();
    }
}
