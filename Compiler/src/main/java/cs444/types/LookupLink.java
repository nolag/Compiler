package cs444.types;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.exceptions.StaticToNonStaticConversion;

import java.util.LinkedList;
import java.util.List;

public class LookupLink {
    public final Iterable<Typeable> dcls;
    //for faster lookup
    public final Typeable lastDcl;
    private final List<Typeable> list;

    public LookupLink(List<Typeable> dcls) {
        this.dcls = dcls;
        list = dcls;
        //Not sure how that happens.  Need to look into it.
        while (list.size() > 1 && list.get(1).getType().isClass) {
            list.remove(1);
        }
        lastDcl = dcls.isEmpty() ? null : dcls.get(dcls.size() - 1);
    }

    public TypeSymbol getType() {
        return lastDcl.getType();
    }

    public LookupLink addWith(Typeable newElement) {
        List<Typeable> copy = new LinkedList<Typeable>(list);
        copy.add(newElement);
        return new LookupLink(copy);
    }

    private ISymbol getAsSymbol(ISymbol is) {
        if (is instanceof DclSymbol) {
            DclSymbol dcl = (DclSymbol) is;
            if (dcl.getType().isClass) {
                return null;
            }
            return new SimpleNameSymbol(dcl);
        }

        if (is instanceof AMethodSymbol) {
            //Don't add it to the list, it will be included with the method invoke itself.
            return null;
        }

        return is;
    }

    public ISymbol convert() throws StaticToNonStaticConversion {
        ISymbol on = getAsSymbol(list.get(list.size() - 1));
        for (int i = list.size() - 2; i >= 0; i--) {
            ISymbol from = getAsSymbol(list.get(i));
            if (on == null) {
                on = from;
            } else if (from != null) {
                on = new FieldAccessSymbol(from, on);
            }
        }

        if (list.get(0) instanceof Typeable) {
            Typeable typeable = list.get(0);
            if (list.size() == 1 && typeable.getType().isClass) {
                throw new StaticToNonStaticConversion("Class Only");
            }
            if (typeable.getType().isClass) {
                typeable = list.get(1);
            }
            if (typeable instanceof DclSymbol) {
                DclSymbol dcl = (DclSymbol) typeable;
                if (!(dcl.isLocal || dcl.isStatic())) {
                    on = new FieldAccessSymbol(new ThisSymbol(true), on);
                }
            } else if (typeable instanceof MethodSymbol) {
                MethodSymbol method = (MethodSymbol) typeable;
                if (!method.isStatic()) {
                    ThisSymbol thisSymbol = new ThisSymbol(true);
                    if (on == null) {
                        on = thisSymbol;
                    }
                    on = new FieldAccessSymbol(thisSymbol, on);
                }
            }
        }
        return on;
    }
}
