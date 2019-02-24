package cs444.parser.symbols;

import java.util.Set;

public abstract class NonTerminal extends ANonTerminal {
    public NonTerminal(String name, ISymbol[] children) {
        super(name);

        for (ISymbol child : children) {
            if (!child.empty()) {
                this.children.add(child);
            }
        }
    }

    public abstract Set<String> noCollapse();

    @Override
    public boolean isCollapsable() {
        return !noCollapse().contains(name.toUpperCase());
    }
}
