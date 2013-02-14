package cs444.parser.symbols;


public abstract class ATerminal implements ISymbol{
    private final String rule;
    private final String name;
    public final String value;

    protected ATerminal(String name, String value){
        rule = name + " -> " + value;
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public String getRule() {
        return rule;
    }
}
