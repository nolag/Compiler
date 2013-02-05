package cs444.generator.lexer.dfa;

import java.io.IOException;
import java.io.Writer;

import cs444.generator.Generator;
import cs444.generator.lexer.grammar.LexicalGrammar;

public class DFAClassGenerator extends Generator {

    private final LexicalGrammar grammar;
	private final String packageId;
	private final String className;

    public DFAClassGenerator(LexicalGrammar grammar, Writer writer) {
        this(grammar, writer, "JoosDFA", "cs444.lexer");
    }

    public DFAClassGenerator(LexicalGrammar grammar, Writer writer, String className, String packageId) {
    	super(writer);

        this.grammar = grammar;
        this.className = className;
        this.packageId = packageId;
	}

	@Override
    public void generate() throws IOException {

        writeLine("//******************************************************");
        writeLine("// Autogenerated code. Do not modify this file manually.");
        writeLine("//******************************************************");

        writeLine("package " + this.packageId + ";");

        writeLine("import java.util.Collections;");
        writeLine("import java.util.HashMap;");
        writeLine("import java.util.Map;");

        writeLine("public class " + this.className + " implements IDFA {");
        indent();

        // States

        int[][] stateTable = grammar.getStateTable();
        int[] acceptTable = grammar.getAcceptTable();

        writeLine("private static final Map<Integer, LexerState> states;");
        writeLine("static {");
        indent();
        writeLine("HashMap<Integer, LexerState> map = new HashMap<Integer, LexerState>();");

        for (int i = 0; i < stateTable.length; i++) {
            writeLine("map.put(" + i + ", State" + i + ".instance);");
        }

        writeLine("states = Collections.unmodifiableMap(map);");
        dedent();
        writeLine("}");

        for (int i = 0; i < stateTable.length; i++) {

            String transitions = "{ ";

            int[] row = stateTable[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] != -1)
                    transitions += row[j];
                else
                    transitions += "-1";

                if (j < row.length - 1)
                    transitions += ", ";
            }

            transitions += " }";

            boolean accepting = (acceptTable[i] != -1);

            writeLine("private static final class State" + i + " {");
            indent();

            String instance = "public static final LexerState instance = new LexerState(new int[] " +
                              transitions + ", " + accepting;

            if (accepting) {
                String type = grammar.getTokenName(acceptTable[i]);
                instance += ", Token.Type." + type;
            }

            instance += ");";

            writeLine(instance);
            dedent();
            writeLine("}");
        }

        writeLine("public LexerState getInitialState() {");
        indent();

        writeLine("return getState(0);");

        dedent();
        writeLine("}");

        writeLine("public LexerState getState(int stateId) {");
        indent();

        writeLine("return states.get(stateId);");

        dedent();
        writeLine("}");

        dedent();
        writeLine("}");
    }
}