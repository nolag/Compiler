package cs444.parser.symbols.ast;

public abstract class AInterfaceOrClassSymbol extends AModifiersOptSymbol{

    protected AInterfaceOrClassSymbol(String ruleName, String dclName) {
        super(ruleName, dclName);
    }

    @Override
    public boolean isCollapsable(){
        return false;
    }

    public abstract boolean isClass();
}
