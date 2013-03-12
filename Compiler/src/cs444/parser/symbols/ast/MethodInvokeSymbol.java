package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.types.LookupLink;

public class MethodInvokeSymbol extends ANonTerminal implements Typeable{
    public final Iterable<ISymbol> params;
    public final String methodName;
    public final String lookupFirst;

    private LookupLink link;

    public MethodInvokeSymbol(String name, Iterable<ISymbol> params, ISymbol on) {
        super("Method Invoke");
        if(on != null)children.add(on);
        String [] parts = name.split("\\.");

        if(parts.length != 1){
            name = name.substring(0, name.lastIndexOf("."));
            lookupFirst = name;
        }else{
            lookupFirst = null;
        }
        this.params = params;

        methodName = parts[parts.length - 1];
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        // TODO: uncomment this later
//        visitor.prepare(this);
//        for(ISymbol child : children) child.accept(visitor);
//        visitor.open(this);
//        for(ISymbol param : params) param.accept(visitor);
//        visitor.close(this);
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    public void setLookup(LookupLink link){
        this.link = link;
    }

    public LookupLink getLookup(){
        return link;
    }

    @Override
    public TypeSymbol getType() {
        return link.getType();
    }

    @Override
    public void setType(TypeSymbol type) { }
}
