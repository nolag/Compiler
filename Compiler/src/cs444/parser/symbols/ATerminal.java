package cs444.parser.symbols;


public abstract class ATerminal implements ISymbol{
    private final String rule;
    private final String name;
    public final String lexeme;

    protected ATerminal(String name, String lexeme){
        rule = name + " -> " + lexeme;
        this.name = name;
        this.lexeme = lexeme;
    }

    public String getName(){
        return name;
    }

    public String getRule() {
        return rule;
    }
}
