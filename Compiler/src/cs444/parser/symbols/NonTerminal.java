package cs444.parser.symbols;


public class NonTerminal implements ISymbol{

    private final ISymbol[] children;
    private final String name;

    public NonTerminal(String name, ISymbol [] children){
    	this.children = children;
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
        if(children.length == 0) return true;
        for(ISymbol child : children){
            if(!child.empty()) return false;
        }
        return true;
    }
}
