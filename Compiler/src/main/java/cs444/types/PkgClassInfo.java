package cs444.types;

import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.exceptions.ClashException;
import cs444.types.exceptions.DuplicateDeclarationException;
import cs444.types.exceptions.UndeclaredException;

import java.util.*;
import java.util.Map.Entry;

public class PkgClassInfo {
    public static final PkgClassInfo instance = new PkgClassInfo();
    public final Map<String, Map<String, PkgClassResolver>> nameSpaces = new HashMap<>();
    public final Map<String, APkgClassResolver> symbolMap = new HashMap<>();
    //Used because symbolMap may not be in order after hashing
    public final List<APkgClassResolver> pkgs = new LinkedList<>();

    private PkgClassInfo() {
        addInitialSymbols();
    }

    public void addClassOrInterface(AInterfaceOrClassSymbol symbol) throws DuplicateDeclarationException,
            UndeclaredException, ClashException {
        PkgClassResolver resolver = PkgClassResolver.getResolver(symbol);
        if (nameSpaces.containsKey(resolver.fullName)) {
            throw new ClashException("package " + resolver.fullName, "class " + resolver.fullName);
        }

        Map<String, PkgClassResolver> nameSpc = nameSpaces.get(resolver.pkg);
        if (nameSpc != null && nameSpc.containsKey(resolver.name)) {
            throw new DuplicateDeclarationException(resolver.fullName, resolver.fullName);
        }

        String pkg = resolver.pkg;

        Map<String, PkgClassResolver> pkgs = null;

        String[] innerPkgs = pkg.split("\\.");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < innerPkgs.length; i++) {
            String newNameSpace = sb + innerPkgs[i];
            pkgs = nameSpaces.get(newNameSpace);

            if (pkgs == null) {
                if (pkgClashWithType(sb.toString(), innerPkgs[i])) {
                    throw new ClashException("package " + newNameSpace, "class " + newNameSpace);
                }

                pkgs = new HashMap<String, PkgClassResolver>();
                nameSpaces.put(newNameSpace, pkgs);
            }

            sb.append(innerPkgs[i] + ".");
        }

        pkgs.put(resolver.name, resolver);
        putSymbol(resolver);

        if (JoosNonTerminal.nonPrimativeOperativeTypes.contains(resolver.fullName)) {
            TypeSymbol.getPrimative(resolver.fullName).setTypeDclNode(resolver);
        }
    }

    private boolean pkgClashWithType(String pkgNamePrefix, String pkgNameSuffix) {
        if (pkgNamePrefix.equals("")) {
            return false;
        }

        String outerPkgName = pkgNamePrefix.substring(0, pkgNamePrefix.length() - 1);
        Map<String, PkgClassResolver> nameSpc = nameSpaces.get(outerPkgName);
        return (nameSpc != null && nameSpc.containsKey(pkgNameSuffix));
    }

    public APkgClassResolver getSymbol(String name) {
        return symbolMap.get(name);
    }

    public void putSymbol(APkgClassResolver resolver) {
        symbolMap.put(resolver.fullName, resolver);
        pkgs.add(resolver);
    }

    public Collection<APkgClassResolver> getSymbols() {
        return pkgs;
    }

    public Iterable<Entry<String, PkgClassResolver>> getNamespaceParts(String nameSpace) {
        return nameSpaces.get(nameSpace).entrySet();
    }

    private void addInitialSymbols() {
        for (String type : JoosNonTerminal.primativeNumbers) {
            APkgClassResolver resolver = PkgClassResolver.getPrimativeResolver(type);
            putSymbol(resolver);
            TypeSymbol.getPrimative(type).setTypeDclNode(resolver);
        }

        for (String type : JoosNonTerminal.otherPrimatives) {
            APkgClassResolver resolver = PkgClassResolver.getPrimativeResolver(type);
            putSymbol(resolver);
            TypeSymbol.getPrimative(type).setTypeDclNode(resolver);
        }
    }

    // NOTE: this method is for tests only
    public void clear(Map<String, Map<String, PkgClassResolver>> nameSpaces,
                      Map<String, APkgClassResolver> symbolMap, List<APkgClassResolver> pkgs) {

        this.nameSpaces.clear();
        //because each entry is a map, we need to clone the maps or they will have entries put into them.
        for (Entry<String, Map<String, PkgClassResolver>> entry : nameSpaces.entrySet()) {
            Map<String, PkgClassResolver> resolverClone = new HashMap<>(entry.getValue());
            this.nameSpaces.put(entry.getKey(), resolverClone);
        }

        this.symbolMap.clear();
        this.symbolMap.putAll(symbolMap);
        this.pkgs.clear();
        this.pkgs.addAll(pkgs);
    }

    public void clear() {
        nameSpaces.clear();
        symbolMap.clear();
        pkgs.clear();
        addInitialSymbols();
    }
}
