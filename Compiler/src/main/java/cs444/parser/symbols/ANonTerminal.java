package cs444.parser.symbols;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.codegen.Platform;

public abstract class ANonTerminal implements ISymbol{
    public final List<ISymbol> children = new LinkedList<ISymbol>();
    public final String name;

    private final Map<Platform<?, ?>, Long> stackSizes = new HashMap<>();

    protected ANonTerminal(final String name){
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
        final StringBuilder rule = new StringBuilder(name).append(" -> ");

        for(final ISymbol child : children){
            rule.append(child.getName()).append(" ");
        }

        for(final ISymbol child : children){
            rule.append("\n").append(child.getRule());
        }

        return rule.toString();
    }

    public ISymbol firstOrDefault(final String ... names){
        for(final ISymbol child : children){
            for(final String name : names){
                if(child.getName().equalsIgnoreCase(name)) return child;
            }
        }
        return null;
    }

    public ISymbol lastOrDefault(final String ... names){
        ISymbol last = null;
        for(final ISymbol child : children){
            for(final String name : names){
                if(child.getName().equalsIgnoreCase(name)) last = child;
            }
        }
        return last;
    }

    public Iterable<ISymbol> getAll(final String ... names){
        final List<ISymbol> validChildren = new LinkedList<ISymbol>();
        for(final ISymbol child : children){
            for(final String name : names){
                if(child.getName().equalsIgnoreCase(name)) validChildren.add(child);
            }
        }
        return validChildren;
    }

    public void setStackSize(final Platform<?, ?> platform, final long stackSize) {
        stackSizes.put(platform, stackSize);
    }

    public long getStackSize(final Platform<?, ?> platform) {
        final Long l = stackSizes.get(platform);
        return l != null ? l : 0;
    }

    public abstract boolean isCollapsable();
}
