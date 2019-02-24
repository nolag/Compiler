package cs444.generator.parser;

import cs444.generator.Generator;
import cs444.generator.lexer.grammar.LexicalGrammar;
import cs444.generator.lexer.grammar.TokenMetadata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public abstract class Language extends Generator {

    private final List<String> terminals = new LinkedList<>();
    private final Set<String> nonTerminals = new HashSet<>();
    private final String startRule;
    private final String name;
    protected List<String> rules = new ArrayList<>();

    protected Language(Writer writer, String startRule, LexicalGrammar tokenGrammar, String name) {
        super(writer);
        initializeTerminals(tokenGrammar);
        this.startRule = startRule.toUpperCase();
        this.name = name;
    }

    public ByteArrayInputStream getLangStream() {
        StringBuilder sb = new StringBuilder().append(terminals.size()).append("\n");

        for (String terminal : terminals) {
            sb.append(terminal).append("\n");
        }

        sb.append(nonTerminals.size()).append("\n");

        for (String nonTerminal : nonTerminals) {
            sb.append(nonTerminal).append("\n");
        }

        //Start Rule
        sb.append("S\n");

        sb.append(rules.size() + 1).append("\nS ").append(startRule).append(" EOF\n");

        for (String rule : rules) {
            sb.append(rule).append("\n");
        }

        ByteArrayInputStream language = new ByteArrayInputStream(sb.toString().getBytes());
        return language;
    }

    @Override
    public void generate() throws IOException {
        rules = toUpperCase(rules);
        initializeNonTerminals();
        for (String nonTerminal : nonTerminals) {
            writeLine(nonTerminal);
        }

        generateDFA();
    }

    private void generateDFA() throws IOException {
        List<String> addTo = new LinkedList<>();
        LanguageGenerator.create(getLangStream(), addTo);
        for (String line : addTo) {
            writeLine(line);
        }
    }

    private void initializeNonTerminals() {
        for (String rule : rules) {
            nonTerminals.add(rule.split(" ")[0]);
        }
        nonTerminals.add("S");
    }

    private void initializeTerminals(LexicalGrammar tokenGrammar) {
        TokenMetadata[] metaDataList = tokenGrammar.getTokenMetadata();

        for (TokenMetadata metaData : metaDataList) {
            terminals.add(metaData.name.toUpperCase());
        }
        terminals.add("EOF");
    }

    private List<String> toUpperCase(List<String> rules) {
        List<String> upperCasedRules = new LinkedList<>();
        for (String rule : rules) {
            upperCasedRules.add(rule.toUpperCase());
        }
        return upperCasedRules;
    }
}
