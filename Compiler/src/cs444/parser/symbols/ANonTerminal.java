package cs444.parser.symbols;

import java.util.LinkedList;
import java.util.List;

public abstract class ANonTerminal implements ISymbol{
    public final List<ISymbol> children = new LinkedList<ISymbol>();
    public final String name;

    protected ANonTerminal(String name){
        this.name = name;
    }

    public Iterable<ISymbol> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public boolean empty() {
        return children.isEmpty();
    }

    public String rule() {
        StringBuilder rule = new StringBuilder(name).append(" -> ");

        for(ISymbol child : children){
            rule.append(child.getName()).append(" ");
        }

        for(ISymbol child : children){
            rule.append("\n").append(child.rule());
        }

        return rule.toString();
    }

    public ISymbol firstOrDefault(String name){
        for(ISymbol child : children){
            if(child.getName().equalsIgnoreCase(name)) return child;
        }
        return null;
    }
}
