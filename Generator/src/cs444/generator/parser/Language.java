package cs444.generator.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cs444.generator.Generator;
import cs444.generator.lexer.grammar.LexicalGrammar;
import cs444.generator.lexer.grammar.TokenMetadata;

public abstract class Language extends Generator{

    private final List<String> terminals = new LinkedList<String>();
    private final Set<String> nonTerminals = new HashSet<String>();
    private final List<String> rules;
    private final String startRule;
    private final String name;

    protected Language(Writer writer, List<String> rules, String startRule, LexicalGrammar tokenGrammar, String name){
        super(writer);

        this.rules = toUpperCase(rules);

        this.startRule = startRule.toUpperCase();
        this.name = name;

        TokenMetadata [] metaDataList = tokenGrammar.getTokenMetadata();

        for(TokenMetadata metaData :  metaDataList) terminals.add(metaData.name);

        for(String rule : this.rules) nonTerminals.add(rule.split(" ")[0]);

        terminals.add("EOF");

        nonTerminals.add("S");
    }

	public ByteArrayInputStream getLangStream() {
        StringBuilder sb = new StringBuilder().append(terminals.size()).append("\n");

        for(String terminal : terminals) sb.append(terminal).append("\n");

        sb.append(nonTerminals.size()).append("\n");

        for(String nonTerminal : nonTerminals) sb.append(nonTerminal).append("\n");

        //Start Rule
        sb.append("S\n");

        sb.append(rules.size() + 1).append("\nS ").append(startRule).append(" EOF\n");

        for(String rule : rules) sb.append(rule).append("\n");

        ByteArrayInputStream language = new ByteArrayInputStream(sb.toString().getBytes());
        return language;
    }

    @Override
    public void generate() throws IOException{
        writeLine("//This file is automatically generated do not modify by hand.");
        writeLine("package cs444.parser;");
        writeLine("");
        writeLine("import java.util.HashMap;");
        writeLine("import java.util.Map;");
        writeLine("");
        writeLine("import cs444.parser.IParserRule;");
        writeLine("import cs444.parser.symbols.SymbolState;");
        writeLine("import cs444.parser.symbols.factories.NonTerminalFactory;");
        writeLine("public class " + name +" implements IParserRule{");
        indent();
        writeLine("public Map<Integer, Map<String, SymbolState>> getRules() {");
        indent();

        writeLine("");
        writeLine("Map<Integer, Map<String, SymbolState>> rules = new HashMap<Integer, Map<String, SymbolState>>();");

        for(String nonTerminal : nonTerminals )
            writeLine("NonTerminalFactory " + nonTerminal.toLowerCase() + " = new NonTerminalFactory(\"" + nonTerminal + "\");");

        writeLine("");

        List<String> addTo = new LinkedList<String>();

        LanguageGenerator.create(getLangStream(), addTo);

        for(String line : addTo) writeLine(line);

        writeLine("return rules;");

        dedent();
        writeLine("}");

        dedent();
        writeLine("}");
    }
    
    private List<String> toUpperCase(List<String> rules) {
		List<String> upperCasedRules = new LinkedList<String>();
		for(String rule : rules){
			upperCasedRules.add(rule.toUpperCase());
		}
		return upperCasedRules;
	}
}
