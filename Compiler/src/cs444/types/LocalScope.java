package cs444.types;

import java.util.HashMap;
import java.util.Map;

import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.exceptions.DuplicateDeclarationException;

public class LocalScope {
    final LocalScope parent;
    private final Map<String, DclSymbol> environment = new HashMap<String, DclSymbol>();

    public LocalScope(){
        this(null);
    }

    public LocalScope(LocalScope parent) {
        this.parent = parent;
    }

    public void add(String name, DclSymbol node) throws DuplicateDeclarationException{
        if (this.isDeclared(name)) throw new DuplicateDeclarationException(name, "local scope");

        this.environment.put(name, node);
    }

    public boolean isDeclared(String name){
        if (environment.containsKey(name)) return true;
        if (parent == null) return false;
        return parent.isDeclared(name);
    }
}
