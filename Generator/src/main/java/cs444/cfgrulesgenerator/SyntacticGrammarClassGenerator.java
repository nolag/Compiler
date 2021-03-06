package cs444.cfgrulesgenerator;

import cs444.generator.Generator;

import java.io.IOException;
import java.io.Writer;

public class SyntacticGrammarClassGenerator extends Generator {
    private final IRulesFactory rules;
    private final String className;

    public SyntacticGrammarClassGenerator(Writer writer, IRulesFactory rules, String className) {
        super(writer);
        this.rules = rules;
        this.className = className;
    }

    @Override
    public void generate() throws IOException {
        writeLine("//******************************************************");
        writeLine("// Autogenerated code. Do not modify this file manually.");
        writeLine("//******************************************************");

        writeLine("package cs444.generator.parser;\n");

        writeLine("import java.io.Writer;");
        writeLine("import java.util.LinkedList;");
        writeLine("import java.util.List;\n");

        writeLine("import cs444.generator.lexer.grammar.JoosGrammar;\n");

        writeLine("public class " + className + "extends Language {\n");

        indent();
        writeLine("private static final JoosGrammar tokenGrammar = new JoosGrammar();");
        writeLine("private static final String startRule = \"CompilationUnit\";");
        writeLine("private static final List<String> rules = new LinkedList<String>();\n");

        writeLine("static{");
        indent();

        Rule rule;
        try {
            while ((rule = rules.getNextRule()) != null) {
                writeLine("rules.add(\"" + rule + "\");");
            }
        } catch (Exception e) {
            String eMessage = "Error generating Syntactic Grammar because an " +
                    "exception was thrown: " + e.getClass() + ". Message: " +
                    e.getMessage();

            writeLine(eMessage);
            throw new IOException(eMessage); // propagate
        }
        dedent();
        writeLine("}");
        dedent();
        writeLine("}");
    }
}
