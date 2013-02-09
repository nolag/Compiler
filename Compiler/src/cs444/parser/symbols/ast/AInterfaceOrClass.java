package cs444.parser.symbols.ast;

public abstract class AInterfaceOrClass extends AModifiersOptSymbol{

    protected AInterfaceOrClass(String name) {
        super(name);
    }

    public abstract boolean isClass();

}
