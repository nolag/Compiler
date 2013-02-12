package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.NonTerminal;

public class ListedSymbol extends ANonTerminal{

    public ListedSymbol(ANonTerminal from, int child){
        super(from.getName());
        ISymbol symbol = from;
        while(symbol.getName().equals(name)){
            if(from.children.size() == 1){
                if(child == 1)children.add(from.children.get(0));
                else children.add(0, from.children.get(0));
                return;
            }

            if(child == 1) children.add(from.children.get(0));
            else children.add(0, from.children.get(1));

            symbol = from.children.get(child);
            if(symbol.getClass().isAssignableFrom(ANonTerminal.class)) break;
            from = (NonTerminal) symbol;
        }
        if(child == 1) children.add(symbol);
        else children.add(0, symbol);
    }

    @Override
    public boolean isCollapsable() {
        return true;
    }
}
