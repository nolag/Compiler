package cs444.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodSymbol;

public class PkgClassResolver {
    private final AInterfaceOrClassSymbol start;
    private final Set<String> imports = new HashSet<String>();
    private final Map<String, String> startImports = new HashMap<String, String>();

    public PkgClassResolver(AInterfaceOrClassSymbol start){
        this.start = start;
    }

    public DclSymbol findDCl(String fullName)throws UndeclaredException {
        //TODO
        return null;
    }

    public MethodSymbol [] findMethod(String fullName) throws UndeclaredException{
        //TODO
        return null;
    }

    public AInterfaceOrClassSymbol getClass(String name) throws UndeclaredException {
        AInterfaceOrClassSymbol ret = null;

        if(imports.contains(name)){
            ret = PkgClassInfo.instance.getSymbol(name);
        }
        else{
            //TODO
        }

        if(ret == null) throw new UndeclaredException(name, start.dclName);
        return ret;
    }
}
