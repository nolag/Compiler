package cs444.parser.symbols.ast;

import java.util.List;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.types.LookupLink;

public class MethodInvokeSymbol extends ANonTerminal implements Typeable{
    public final boolean hasFirst;
    public final String methodName;
    public final String lookupFirst;

    private LookupLink link;

    public MethodInvokeSymbol(String name, List<ISymbol> params, ISymbol on) {
        super("Method Invoke");
        if(on != null)children.add(on);
        String [] parts = name.split("\\.");

        if(parts.length != 1){
            name = name.substring(0, name.lastIndexOf("."));
            lookupFirst = name;
        }else{
            lookupFirst = null;
        }

        children.addAll(params);
        hasFirst = on != null;

        methodName = parts[parts.length - 1];
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.prepare(this);
        int i = 0;
        if(hasFirst){
            children.get(0).accept(visitor);
            i++;
        }
        visitor.open(this);
        for(; i < children.size(); i++){
            ISymbol param = children.get(i);
            param.accept(visitor);
        }
        visitor.close(this);
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
