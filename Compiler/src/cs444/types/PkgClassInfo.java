package cs444.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NameSymbol.Type;
import cs444.types.exceptions.DuplicateDeclearationException;
import cs444.types.exceptions.UndeclaredException;

public class PkgClassInfo {
    private final Map<String, Map<String, PkgClassResolver>> nameSpaces = new HashMap<String, Map<String, PkgClassResolver>>();
    private final Map<String, PkgClassResolver> symbolMap = new HashMap<String, PkgClassResolver>();

    public static final PkgClassInfo instance = new PkgClassInfo();

    private PkgClassInfo(){
        symbolMap.put("boolean", PkgClassResolver.primResolver);
        symbolMap.put("byte", PkgClassResolver.primResolver);
        symbolMap.put("short", PkgClassResolver.primResolver);
        symbolMap.put("int", PkgClassResolver.primResolver);
        symbolMap.put("char", PkgClassResolver.primResolver);
    }

    public void addClassOrInterface(AInterfaceOrClassSymbol symbol) throws DuplicateDeclearationException, UndeclaredException{
        if(nameSpaces.containsKey(symbol.dclName))throw new DuplicateDeclearationException(symbol.dclName, symbol.dclName);

        NameSymbol nameSymbol = (NameSymbol) symbol.firstOrDefault("Name");
        String pkg = nameSymbol.type == Type.PACKAGE ? nameSymbol.value + "." : "";

        Map<String, PkgClassResolver> pkgs = nameSpaces.get(pkg);

        if(pkgs == null){
            pkgs = new HashMap<String, PkgClassResolver>();
            nameSpaces.put(pkg, pkgs);
        }

        PkgClassResolver reslover = PkgClassResolver.getResolver(symbol);

        pkgs.put(pkg, reslover);
        symbolMap.put(symbol.dclName, reslover);
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
}
