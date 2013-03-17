package cs444.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs444.CompilerException;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.exceptions.DuplicateDeclarationException;

public class LocalScope {
    public final LocalScope parent;
    public final boolean isStatic;
    private final Map<String, DclSymbol> environment = new HashMap<String, DclSymbol>();
    private Set<String> initializing = new HashSet<String>();

    public LocalScope(){
        this(null, false);
    }

    public LocalScope(LocalScope parent, boolean isStatic) {
        this.parent = parent;
        this.isStatic = isStatic;
    }

    public void add(String name, DclSymbol node) throws DuplicateDeclarationException{
        if (this.isDeclared(name)) throw new DuplicateDeclarationException(name, "local scope");

        this.environment.put(name, node);
        initializing.remove(name);
    }

    public boolean isDeclared(String name){
        if (environment.containsKey(name)) return true;
        if (parent == null) return false;
        return parent.isDeclared(name);
    }

    public void initializing(String varName){
        initializing.add(varName);
    }

    public DclSymbol find(String varName) throws CompilerException {
        if (initializing.contains(varName)){
            throw new CompilerException("Variable " + varName + " cannot be used in its initialization.");
        }

        DclSymbol dclNode;

        if ((dclNode = environment.get(varName)) != null) return dclNode;

        if (parent != null) return parent.find(varName);

        return null;
    }
}
