package cs444.parser.symbols;

import java.util.HashSet;
import java.util.Set;

public class JoosNonTerminal extends NonTerminal{
    public static final Set<String> noCollapse = new HashSet<String>();

    static{
        noCollapse.add("N_MODIFIER_0");
    }

    public JoosNonTerminal(String name, ISymbol[] children) {
        super(name, children);
    }

    @Override
    public Set<String> noCollapse() {
        return noCollapse;
    }
}
