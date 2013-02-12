package cs444.parser.symbols;



public class NonTerminal extends ANonTerminal{
    public NonTerminal(String name, ISymbol [] children){
        super(name);

    	for(ISymbol child : children){
    	    if(!child.empty()) this.children.add(child);
    	}
    }

    @Override
    public boolean isCollapsable() {
        return true;
    }
}
