package cs444.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.exceptions.ClashException;
import cs444.types.exceptions.DuplicateDeclarationException;
import cs444.types.exceptions.UndeclaredException;

public class PkgClassInfo {
    private final Map<String, Map<String, PkgClassResolver>> nameSpaces = new HashMap<String, Map<String, PkgClassResolver>>();
    public final Map<String, APkgClassResolver> symbolMap = new HashMap<String, APkgClassResolver>();

    //Used because symbolMap may not be in order after hashing
    public final List<APkgClassResolver> pkgs = new LinkedList<APkgClassResolver>();

    public static final PkgClassInfo instance = new PkgClassInfo();

    private PkgClassInfo(){
        addInitialSymbols();
    }

    public void addClassOrInterface(final AInterfaceOrClassSymbol symbol) throws DuplicateDeclarationException, UndeclaredException, ClashException{
        final PkgClassResolver resolver = PkgClassResolver.getResolver(symbol);
        if(nameSpaces.containsKey(resolver.fullName)){
            throw new ClashException("package " + resolver.fullName, "class " + resolver.fullName);
        }

        final Map<String, PkgClassResolver> nameSpc = nameSpaces.get(resolver.pkg);
        if(nameSpc != null && nameSpc.containsKey(resolver.name)) throw new DuplicateDeclarationException(resolver.fullName, resolver.fullName);

        final String pkg = resolver.pkg;

        Map<String, PkgClassResolver> pkgs = null;

        final String [] innerPkgs = pkg.split("\\.");
        final StringBuilder sb = new StringBuilder();

        for(int i = 0; i < innerPkgs.length; i++){
            final String newNameSpace = sb.toString() + innerPkgs[i];
            pkgs = nameSpaces.get(newNameSpace);

            if(pkgs == null){
                if(pkgClashWithType(sb.toString(), innerPkgs[i])){
                    throw new ClashException("package " + newNameSpace, "class " + newNameSpace);
                }

                pkgs = new HashMap<String, PkgClassResolver>();
                nameSpaces.put(newNameSpace, pkgs);
            }

            sb.append(innerPkgs[i] + ".");
        }

        pkgs.put(resolver.name, resolver);
        putSymbol(resolver);


        if(JoosNonTerminal.nonPrimativeOperativeTypes.contains(resolver.fullName)){
            TypeSymbol.getPrimative(resolver.fullName).setTypeDclNode(resolver);
        }
    }

    private boolean pkgClashWithType(final String pkgNamePrefix, final String pkgNameSuffix) {
        if (pkgNamePrefix.equals("")) return false;

        final String outerPkgName = pkgNamePrefix.substring(0, pkgNamePrefix.length()-1);
        final Map<String, PkgClassResolver> nameSpc = nameSpaces.get(outerPkgName);
        return (nameSpc != null && nameSpc.containsKey(pkgNameSuffix));
    }

    public APkgClassResolver getSymbol(final String name){
        return symbolMap.get(name);
    }

    public void putSymbol(final APkgClassResolver resolver){
        symbolMap.put(resolver.fullName, resolver);
        pkgs.add(resolver);
    }

    public Collection<APkgClassResolver> getSymbols(){
        return pkgs;
    }

    public Iterable<Entry<String, PkgClassResolver>> getNamespaceParts(final String nameSpace){
        return nameSpaces.get(nameSpace).entrySet();
    }

    private void addInitialSymbols() {
        for(final String type : JoosNonTerminal.primativeNumbers){
            final APkgClassResolver resolver = PkgClassResolver.getPrimativeResolver(type);
            putSymbol(resolver);
            TypeSymbol.getPrimative(type).setTypeDclNode(resolver);
        }

        for(final String type : JoosNonTerminal.otherPrimatives){
            final APkgClassResolver resolver = PkgClassResolver.getPrimativeResolver(type);
            putSymbol(resolver);
            TypeSymbol.getPrimative(type).setTypeDclNode(resolver);
        }
    }

    // NOTE: this method is for tests only
    public void clear(){
        nameSpaces.clear();
        symbolMap.clear();
        pkgs.clear();
        addInitialSymbols();
    }
}
