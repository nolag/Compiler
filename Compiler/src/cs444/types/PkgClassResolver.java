package cs444.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.AModifiersOptSymbol;

public class PkgClassResolver {
    private final AInterfaceOrClassSymbol start;
    private final Set<String> imports = new HashSet<String>();
    private final Map<String, String> startImports = new HashMap<String, String>();

    public PkgClassResolver(AInterfaceOrClassSymbol start){
        this.start = start;

    }

    public AModifiersOptSymbol findDCl(String fullName)throws UndeclaredException {
        //TODO
        return null;
    }

    public AInterfaceOrClassSymbol getClass(String name) throws UndeclaredException {
        //TODO
        //if(imports.contains(name)) return PkgClassInfo.instance.getSymbol(name);
        return null;
    }
}
