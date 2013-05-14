package cs444.types;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.exceptions.ImplicitStaticConversionException;

public class LookupLink{
    public final Iterable<Typeable> dcls;
    private final List<Typeable> list;
    //for faster lookup
    public final Typeable lastDcl;

    public LookupLink(final List<Typeable> dcls){
        this.dcls = dcls;
        list = dcls;
        //Not sure how that happens.  Need to look into it.
        while(list.size() > 1 && list.get(1).getType().isClass) list.remove(1);
        lastDcl = dcls.isEmpty() ? null : dcls.get(dcls.size() - 1);
    }

    public TypeSymbol getType(){
        return lastDcl.getType();
    }

    public LookupLink addWith(final Typeable newElement){
        final List<Typeable> copy = new LinkedList<Typeable>(list);
        copy.add(newElement);
        return new LookupLink(copy);
    }

    private ISymbol getAsSymbol(final ISymbol is){
        if(is instanceof DclSymbol){
            final DclSymbol dcl = (DclSymbol) is;
            if(dcl.getType().isClass) return null;
            return new SimpleNameSymbol(dcl);
        }

        if(is instanceof AMethodSymbol){
            //Don't add it to the list, it will be included with the method invoke itself.
            return null;
        }

        return is;
    }

    public ISymbol convert() throws ImplicitStaticConversionException {
        ISymbol on = getAsSymbol(list.get(list.size() - 1));
        for(int i = list.size() - 2; i >= 0; i--){
            final ISymbol from = getAsSymbol(list.get(i));
            if(on == null) on = from;
            else if(from != null) on = new FieldAccessSymbol(from, on);
        }

        if(list.get(0) instanceof Typeable){
            Typeable typeable = list.get(0);
            if(list.size() == 1 && typeable.getType().isClass) throw new ImplicitStaticConversionException("Class Only");
            if(typeable.getType().isClass) typeable = list.get(1);
            if(typeable instanceof DclSymbol){
                final DclSymbol dcl =  (DclSymbol) typeable;
                if(!(dcl.isLocal || dcl.isStatic())) on = new FieldAccessSymbol(new ThisSymbol(true), on);
            }else if(typeable instanceof MethodSymbol){
                final MethodSymbol method = (MethodSymbol) typeable;
                if(!method.isStatic()){
                    final ThisSymbol thisSymbol = new ThisSymbol(true);
                    if(on == null) on = thisSymbol;
                    on = new FieldAccessSymbol(thisSymbol, on);
                }
            }
        }
        return on;
    }
}
