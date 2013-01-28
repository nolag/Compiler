//This file is automatically generated do not modify by hand.
package cs444.parser.rules;

import java.util.HashMap;
import java.util.Map;

import cs444.parser.IParserRule;
import cs444.parser.symbols.SymbolState;
import cs444.parser.symbols.factories.NonTerminalFactory;
public class JoosRules implements IParserRule{
    private NonTerminalFactory start;
    public Map<Integer, Map<String, SymbolState>> getRules() {
        
        Map<Integer, Map<String, SymbolState>> rules = new HashMap<Integer, Map<String, SymbolState>>();
        NonTerminalFactory dcls_becomes = new NonTerminalFactory("DCLS_BECOMES");
        
        Map<String, SymbolState> state0 = new HashMap<String, SymbolState>();
        rules.put(0, state0);
        state0.put("ID", new SymbolState(1));
        Map<String, SymbolState> state1 = new HashMap<String, SymbolState>();
        rules.put(1, state1);
        state1.put("EOF", new SymbolState(2));
        start = dcls_becomes;
        return rules;
    }
    
    public NonTerminalFactory getStartRule(){
        return start;
    }
}
