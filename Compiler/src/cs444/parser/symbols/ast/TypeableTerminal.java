package cs444.parser.symbols.ast;

import cs444.parser.symbols.ATerminal;

public abstract class TypeableTerminal extends ATerminal implements Typeable{
    private TypeSymbol type;

    protected TypeableTerminal(String name, String value) {
        super(name, value);
    }

    @Override
    public TypeSymbol getType(){
        return type;
    }

    @Override
    public void setType(TypeSymbol type){
        this.type = type;
    }
}
