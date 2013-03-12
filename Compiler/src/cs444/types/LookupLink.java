package cs444.types;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;

public class LookupLink{
    public final Iterable<Typeable> dcls;
    private final List<Typeable> list;
    //for faster lookup
    public final Typeable lastDcl;

    public LookupLink(List<Typeable> dcls){
        this.dcls = dcls;
        list = dcls;
        lastDcl = dcls.isEmpty() ? null : dcls.get(dcls.size() - 1);
    }

    public TypeSymbol getType(){
        return lastDcl.getType();
    }

    public LookupLink addWith(Typeable newElement){
        List<Typeable> copy = new LinkedList<Typeable>(list);
        copy.add(newElement);
        return new LookupLink(copy);
    }
}
