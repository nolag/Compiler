package cs444.types;

import cs444.CompilerException;
import cs444.codegen.Platform;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.exceptions.DuplicateDeclarationException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LocalScope {
    public final LocalScope parent;
    public final boolean isStatic;
    public final Map<Platform<?, ?>, Long> offsets;
    private final Map<String, DclSymbol> environment = new HashMap<String, DclSymbol>();
    private final Set<String> initializing = new HashSet<String>();

    public LocalScope(LocalScope parent, boolean isStatic, Map<Platform<?, ?>, Long> offsets) {
        this.parent = parent;
        this.isStatic = isStatic;
        this.offsets = new HashMap<>(offsets);
    }

    public void add(String name, DclSymbol node) throws DuplicateDeclarationException {
        if (isDeclared(name)) {
            throw new DuplicateDeclarationException(name, "local scope");
        }

        environment.put(name, node);
        initializing.remove(name);
    }

    public boolean isDeclared(String name) {
        if (environment.containsKey(name)) {
            return true;
        }
        if (parent == null) {
            return false;
        }
        return parent.isDeclared(name);
    }

    public void initializing(String varName) {
        initializing.add(varName);
    }

    public DclSymbol find(String varName) throws CompilerException {
        if (initializing.contains(varName)) {
            throw new CompilerException("Variable " + varName + " cannot be used in its initialization.");
        }

        DclSymbol dclNode;

        if ((dclNode = environment.get(varName)) != null) {
            return dclNode;
        }

        if (parent != null) {
            return parent.find(varName);
        }

        return null;
    }
}
