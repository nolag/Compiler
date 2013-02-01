package cs444.parser.symbols;



public class NonTerminal extends ANonTerminal{
    private final String name;

    public NonTerminal(String name, ISymbol [] children){
    	for(ISymbol child : children){
    	    if(!child.empty()) this.children.add(child);
    	}
    	this.name = name;
    }

	public String rule() {

	    if(empty()) return "";

	    StringBuilder rule = new StringBuilder(name).append(" -> ");

		for(ISymbol child : children){
		    if(child.empty()) continue;
		    rule.append(child.getName()).append(" ");
		}

		for(ISymbol child : children){
			if(child.empty()) continue;
			rule.append("\n").append(child.rule());
		}

		return rule.toString();
	}

	public String getName() {
		return name;
	}

    public boolean empty() {
        return children.isEmpty();
    }

    public Iterable<ISymbol> getChildren() {
        return children;
    }
}
