package cs444.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cs444.parser.symbols.SymbolState;
import cs444.parser.symbols.factories.JoosNonTerminalFactory;
import cs444.parser.symbols.factories.NonTerminalFactory;

public class TextReadingRules implements IParserRule{
    private final File readFrom;

    public TextReadingRules(File readFrom){
        this.readFrom = readFrom;
    }

    private Map<String, SymbolState> getRule(Map<Integer, Map<String, SymbolState>> rules, int number){
        if(rules.containsKey(number)) return rules.get(number);
        Map<String, SymbolState> newState = new HashMap<String, SymbolState>();
        rules.put(number, newState);
        return newState;
    }

    public Map<Integer, Map<String, SymbolState>> getRules() throws IOException {
        Map<Integer, Map<String, SymbolState>> rules = new HashMap<Integer, Map<String, SymbolState>>();

        BufferedReader reader = new BufferedReader(new FileReader(readFrom));

        Map<String, NonTerminalFactory> factories = new HashMap<String, NonTerminalFactory>();

        String line;
        while((line = reader.readLine()) != null){
            String[] split = line.split(" ");
            SymbolState symbolState;
            switch(split.length){
            case 1:
                factories.put(line, new JoosNonTerminalFactory(line));
                break;
            case 3:
                symbolState = new SymbolState(Integer.parseInt(split[2]));
                getRule(rules, Integer.parseInt(split[0])).put(split[1], symbolState);
                break;
            case 4:
                symbolState = new SymbolState(factories.get(split[2]), Integer.parseInt(split[3]));
                getRule(rules, Integer.parseInt(split[0])).put(split[1], symbolState);
                break;
            default:
                System.err.println("Rule " + line + " is not understood, skipping");
            }
        }

        reader.close();

        return rules;
    }

}
