package cs444.parser.symbols;

import java.util.LinkedList;
import java.util.List;

public abstract class ANonTerminal implements ISymbol{
    public final List<ISymbol> children = new LinkedList<ISymbol>();
    public final String name;

    protected ANonTerminal(String name){
        this.name = name;
    }

    public List<ISymbol> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getRule() {
        StringBuilder rule = new StringBuilder(name).append(" -> ");

        for(ISymbol child : children){
            rule.append(child.getName()).append(" ");
        }

        for(ISymbol child : children){
            rule.append("\n").append(child.getRule());
        }

        return rule.toString();
    }

    public ISymbol firstOrDefault(String ... names){
        for(ISymbol child : children){
            for(String name : names){
                if(child.getName().equalsIgnoreCase(name)) return child;
            }
        }
        return null;
    }

    public Iterable<ISymbol> getAll(String ... names){
        List<ISymbol> validChildren = new LinkedList<ISymbol>();
        for(ISymbol child : children){
            for(String name : names){
                if(child.getName().equalsIgnoreCase(name)) validChildren.add(child);
            }
        }
        return validChildren;
    }

    public abstract boolean isCollapsable();
}
