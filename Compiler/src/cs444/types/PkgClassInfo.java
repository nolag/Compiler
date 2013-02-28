package cs444.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.types.exceptions.DuplicateDeclearationException;
import cs444.types.exceptions.UndeclaredException;

public class PkgClassInfo {
    private final Map<String, Map<String, PkgClassResolver>> nameSpaces = new HashMap<String, Map<String, PkgClassResolver>>();
    private final Map<String, PkgClassResolver> symbolMap = new HashMap<String, PkgClassResolver>();

    public static final PkgClassInfo instance = new PkgClassInfo();

    private PkgClassInfo(){
        addInitialSymbols();
    }

    public void addClassOrInterface(AInterfaceOrClassSymbol symbol) throws DuplicateDeclearationException, UndeclaredException{
        PkgClassResolver resolver = PkgClassResolver.getResolver(symbol);

        if(nameSpaces.containsKey(resolver.fullName))throw new DuplicateDeclearationException(resolver.fullName, resolver.fullName);

        String pkg = resolver.pkg;

        Map<String, PkgClassResolver> pkgs = null;

        String [] innerPkgs = pkg.split("\\.");
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < innerPkgs.length; i++){
            sb.append(innerPkgs[i]);

            pkgs = nameSpaces.get(sb.toString());

            if(pkgs == null){
                pkgs = new HashMap<String, PkgClassResolver>();
                nameSpaces.put(sb.toString(), pkgs);
            }

            sb.append(".");
        }

        pkgs.put(resolver.name, resolver);
        symbolMap.put(resolver.fullName, resolver);
    }

    public PkgClassResolver getSymbol(String name){
        return symbolMap.get(name);
    }

    public Iterable<PkgClassResolver> getSymbols(){
        return symbolMap.values();
    }

    public Iterable<Entry<String, PkgClassResolver>> getNamespaceParts(String nameSpace){
        return nameSpaces.get(nameSpace).entrySet();
    }

    private void addInitialSymbols() {
        symbolMap.put("boolean", PkgClassResolver.primResolver);
        symbolMap.put("byte", PkgClassResolver.primResolver);
        symbolMap.put("short", PkgClassResolver.primResolver);
        symbolMap.put("int", PkgClassResolver.primResolver);
        symbolMap.put("char", PkgClassResolver.primResolver);
    }
}
