package cs444.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NameSymbol.Type;

public class PkgClassInfo {
    private final Map<String, Set<String>> nameSpaces = new HashMap<String, Set<String>>();
    private final Map<String, AInterfaceOrClassSymbol> symbolMap = new HashMap<String, AInterfaceOrClassSymbol>();

    public static final PkgClassInfo instance = new PkgClassInfo();

    private PkgClassInfo(){ }

    public void addClassOrInterface(AInterfaceOrClassSymbol symbol) throws DuplicateDeclearationException{
        if(nameSpaces.containsKey(symbol.dclName))throw new DuplicateDeclearationException(symbol.dclName, symbol.dclName);

        NameSymbol nameSymbol = (NameSymbol) symbol.firstOrDefault("Name");
        String name = nameSymbol.type == Type.PACKAGE ? nameSymbol.value : "";
        String [] nameParts = name.split(".");
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for(; i < nameParts.length - 2; i++){
            sb.append(nameParts[i]).append(".");
            String pkg = sb.toString();
            Set<String> names = nameSpaces.get(pkg);

            if(null == names){
                names = new HashSet<String>();
                nameSpaces.put(pkg, names);
            }

            names.add(pkg + nameParts[i + 1] + ".");
            if(symbolMap.containsKey(pkg)) throw new DuplicateDeclearationException(pkg, symbol.dclName);
        }

        symbolMap.put(symbol.dclName, symbol);
    }

    public AInterfaceOrClassSymbol getSymbol(String name){
        return symbolMap.get(name);
    }
}
