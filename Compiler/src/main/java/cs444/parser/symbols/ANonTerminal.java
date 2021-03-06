package cs444.parser.symbols;

import cs444.codegen.Platform;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ANonTerminal implements ISymbol {
    public final List<ISymbol> children = new LinkedList<ISymbol>();
    public final String name;

    private final Map<Platform<?, ?>, Long> stackSizes = new HashMap<>();

    protected ANonTerminal(String name) {
        this.name = name;
    }

    public List<ISymbol> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRule() {
        StringBuilder rule = new StringBuilder(name).append(" -> ");

        for (ISymbol child : children) {
            rule.append(child.getName()).append(" ");
        }

        for (ISymbol child : children) {
            rule.append("\n").append(child.getRule());
        }

        return rule.toString();
    }

    public ISymbol firstOrDefault(String... names) {
        for (ISymbol child : children) {
            for (String name : names) {
                if (child.getName().equalsIgnoreCase(name)) {
                    return child;
                }
            }
        }
        return null;
    }

    public ISymbol lastOrDefault(String... names) {
        ISymbol last = null;
        for (ISymbol child : children) {
            for (String name : names) {
                if (child.getName().equalsIgnoreCase(name)) {
                    last = child;
                }
            }
        }
        return last;
    }

    public Iterable<ISymbol> getAll(String... names) {
        List<ISymbol> validChildren = new LinkedList<ISymbol>();
        for (ISymbol child : children) {
            for (String name : names) {
                if (child.getName().equalsIgnoreCase(name)) {
                    validChildren.add(child);
                }
            }
        }
        return validChildren;
    }

    public void setStackSize(Platform<?, ?> platform, long stackSize) {
        stackSizes.put(platform, stackSize);
    }

    public long getStackSize(Platform<?, ?> platform) {
        Long l = stackSizes.get(platform);
        return l != null ? l : 0;
    }

    public abstract boolean isCollapsable();
}
