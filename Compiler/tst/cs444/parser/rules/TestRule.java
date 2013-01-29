//This file is automatically generated do not modify by hand.
package cs444.parser.rules;

import java.util.HashMap;
import java.util.Map;

import cs444.parser.IParserRule;
import cs444.parser.symbols.SymbolState;
import cs444.parser.symbols.factories.NonTerminalFactory;
public class TestRule implements IParserRule{
    public Map<Integer, Map<String, SymbolState>> getRules() {
        
        Map<Integer, Map<String, SymbolState>> rules = new HashMap<Integer, Map<String, SymbolState>>();
        NonTerminalFactory s = new NonTerminalFactory("S");
        NonTerminalFactory n_becomes = new NonTerminalFactory("N_BECOMES");
        NonTerminalFactory dcls = new NonTerminalFactory("DCLS");
        NonTerminalFactory dcls_becomes = new NonTerminalFactory("DCLS_BECOMES");
        
        Map<String, SymbolState> state15 = new HashMap<String, SymbolState>();
        rules.put(15, state15);
        state15.put("N_BECOMES", new SymbolState(1));
        Map<String, SymbolState> state12 = new HashMap<String, SymbolState>();
        rules.put(12, state12);
        state12.put("ID", new SymbolState(2));
        Map<String, SymbolState> state14 = new HashMap<String, SymbolState>();
        rules.put(14, state14);
        state14.put("ID", new SymbolState(3));
        Map<String, SymbolState> state31 = new HashMap<String, SymbolState>();
        rules.put(31, state31);
        state31.put("ID", new SymbolState(4));
        Map<String, SymbolState> state19 = new HashMap<String, SymbolState>();
        rules.put(19, state19);
        state19.put("EQ", new SymbolState(5));
        Map<String, SymbolState> state5 = new HashMap<String, SymbolState>();
        rules.put(5, state5);
        state5.put("DECIMAL_INTEGER_LITERAL", new SymbolState(6));
        state15.put("ID", new SymbolState(4));
        state31.put("EOF", new SymbolState(n_becomes,  0));
        Map<String, SymbolState> state28 = new HashMap<String, SymbolState>();
        rules.put(28, state28);
        state28.put("DCLS", new SymbolState(7));
        Map<String, SymbolState> state16 = new HashMap<String, SymbolState>();
        rules.put(16, state16);
        state16.put("EOF", new SymbolState(dcls_becomes,  2));
        Map<String, SymbolState> state11 = new HashMap<String, SymbolState>();
        rules.put(11, state11);
        state11.put("EOF", new SymbolState(n_becomes,  4));
        Map<String, SymbolState> state6 = new HashMap<String, SymbolState>();
        rules.put(6, state6);
        state6.put("N_BECOMES", new SymbolState(8));
        Map<String, SymbolState> state2 = new HashMap<String, SymbolState>();
        rules.put(2, state2);
        state2.put("EOF", new SymbolState(n_becomes,  0));
        Map<String, SymbolState> state0 = new HashMap<String, SymbolState>();
        rules.put(0, state0);
        state0.put("DCLS", new SymbolState(9));
        Map<String, SymbolState> state22 = new HashMap<String, SymbolState>();
        rules.put(22, state22);
        state22.put("N_BECOMES", new SymbolState(10));
        state2.put("N_BECOMES", new SymbolState(11));
        Map<String, SymbolState> state8 = new HashMap<String, SymbolState>();
        rules.put(8, state8);
        state8.put("EOF", new SymbolState(n_becomes,  5));
        Map<String, SymbolState> state4 = new HashMap<String, SymbolState>();
        rules.put(4, state4);
        state4.put("EQ", new SymbolState(12));
        Map<String, SymbolState> state3 = new HashMap<String, SymbolState>();
        rules.put(3, state3);
        state3.put("EQ", new SymbolState(13));
        state0.put("EOF", new SymbolState(dcls,  0));
        Map<String, SymbolState> state7 = new HashMap<String, SymbolState>();
        rules.put(7, state7);
        state7.put("EOF", new SymbolState(dcls,  6));
        Map<String, SymbolState> state1 = new HashMap<String, SymbolState>();
        rules.put(1, state1);
        state1.put("EOF", new SymbolState(n_becomes,  4));
        Map<String, SymbolState> state10 = new HashMap<String, SymbolState>();
        rules.put(10, state10);
        state10.put("EOF", new SymbolState(n_becomes,  5));
        state28.put("ID", new SymbolState(dcls,  0));
        state28.put("INT", new SymbolState(14));
        state7.put("ID", new SymbolState(dcls,  6));
        Map<String, SymbolState> state24 = new HashMap<String, SymbolState>();
        rules.put(24, state24);
        state24.put("EOF", new SymbolState(dcls,  0));
        state12.put("DECIMAL_INTEGER_LITERAL", new SymbolState(15));
        Map<String, SymbolState> state9 = new HashMap<String, SymbolState>();
        rules.put(9, state9);
        state9.put("N_BECOMES", new SymbolState(16));
        state24.put("INT", new SymbolState(14));
        state0.put("DCLS_BECOMES", new SymbolState(17));
        state31.put("N_BECOMES", new SymbolState(18));
        state4.put("PLUS", new SymbolState(19));
        Map<String, SymbolState> state13 = new HashMap<String, SymbolState>();
        rules.put(13, state13);
        state13.put("DECIMAL_INTEGER_LITERAL", new SymbolState(20));
        state24.put("DCLS", new SymbolState(21));
        state6.put("EOF", new SymbolState(n_becomes,  0));
        state28.put("EOF", new SymbolState(dcls,  0));
        Map<String, SymbolState> state26 = new HashMap<String, SymbolState>();
        rules.put(26, state26);
        state26.put("EOF", new SymbolState(n_becomes,  5));
        Map<String, SymbolState> state21 = new HashMap<String, SymbolState>();
        rules.put(21, state21);
        state21.put("EOF", new SymbolState(dcls,  6));
        Map<String, SymbolState> state29 = new HashMap<String, SymbolState>();
        rules.put(29, state29);
        state29.put("ID", new SymbolState(22));
        state2.put("ID", new SymbolState(4));
        state4.put("MINUS", new SymbolState(23));
        Map<String, SymbolState> state30 = new HashMap<String, SymbolState>();
        rules.put(30, state30);
        state30.put("SEMI", new SymbolState(24));
        state6.put("ID", new SymbolState(4));
        state22.put("EOF", new SymbolState(n_becomes,  0));
        state9.put("EOF", new SymbolState(n_becomes,  0));
        state24.put("ID", new SymbolState(dcls,  0));
        Map<String, SymbolState> state17 = new HashMap<String, SymbolState>();
        rules.put(17, state17);
        state17.put("EOF", new SymbolState(25));
        state15.put("EOF", new SymbolState(n_becomes,  0));
        Map<String, SymbolState> state27 = new HashMap<String, SymbolState>();
        rules.put(27, state27);
        state27.put("N_BECOMES", new SymbolState(26));
        Map<String, SymbolState> state18 = new HashMap<String, SymbolState>();
        rules.put(18, state18);
        state18.put("EOF", new SymbolState(n_becomes,  5));
        state22.put("ID", new SymbolState(4));
        state27.put("EOF", new SymbolState(n_becomes,  0));
        state29.put("DECIMAL_INTEGER_LITERAL", new SymbolState(27));
        state9.put("ID", new SymbolState(4));
        Map<String, SymbolState> state20 = new HashMap<String, SymbolState>();
        rules.put(20, state20);
        state20.put("SEMI", new SymbolState(28));
        Map<String, SymbolState> state23 = new HashMap<String, SymbolState>();
        rules.put(23, state23);
        state23.put("EQ", new SymbolState(29));
        state0.put("INT", new SymbolState(14));
        state13.put("ID", new SymbolState(30));
        state5.put("ID", new SymbolState(31));
        state21.put("ID", new SymbolState(dcls,  6));
        Map<String, SymbolState> state25 = new HashMap<String, SymbolState>();
        rules.put(25, state25);
        state25.put("IF", new SymbolState(s,  2));
        state27.put("ID", new SymbolState(4));
        state0.put("ID", new SymbolState(dcls,  0));
        return rules;
    }
}
