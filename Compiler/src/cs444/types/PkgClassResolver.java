package cs444.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.types.exceptions.CircularDependancyException;
import cs444.types.exceptions.DuplicateDeclearationException;
import cs444.types.exceptions.ImplicitStaticConversionException;
import cs444.types.exceptions.UndeclaredException;

public class PkgClassResolver {
    private final AInterfaceOrClassSymbol start;

    private final Map<String, MethodSymbol> methodMap = new HashMap<String, MethodSymbol>();
    private final Map<String, MethodSymbol> smethodMap = new HashMap<String, MethodSymbol>();
    private final Map<String, DclSymbol> fieldMap = new HashMap<String, DclSymbol>();
    private final Map<String, DclSymbol> sfieldMap = new HashMap<String, DclSymbol>();

    private final Map<String, PkgClassResolver> namedMap = new HashMap<String, PkgClassResolver>();
    private final Map<String, PkgClassResolver> samepkgMap = new HashMap<String, PkgClassResolver>();
    private final Map<String, PkgClassResolver> staredMap = new HashMap<String, PkgClassResolver>();

    private boolean isBuilt = false;

    public static final PkgClassResolver primResolver = new PkgClassResolver();

    private static final Map<AInterfaceOrClassSymbol, PkgClassResolver> resolverMap =
            new HashMap<AInterfaceOrClassSymbol, PkgClassResolver>();

    private static String generateUniqueName(String name, Iterable<String> types){
        StringBuilder sb = new StringBuilder(name + "-");

        for (String type : types) sb.append(type + "*");

        return sb.toString();
    }

    public static PkgClassResolver getResolver(AInterfaceOrClassSymbol start) throws UndeclaredException, DuplicateDeclearationException{
        PkgClassResolver resolver = resolverMap.get(start);

        if(resolver == null){
            resolver = new PkgClassResolver(start);
            resolverMap.put(start, resolver);
        }

        return resolver;
    }

    private PkgClassResolver() {
        this.start = null;
        isBuilt = true;
    }

    private PkgClassResolver(AInterfaceOrClassSymbol start) throws UndeclaredException, DuplicateDeclearationException{
        this.start = start;

        for(ISymbol symbol : start.children){
            if(!NameSymbol.class.isInstance(symbol)) continue;
            NameSymbol name = (NameSymbol) symbol;
            Map<String, PkgClassResolver> entryMap = staredMap;

            switch(name.type){
            case IMPORT:
                PkgClassResolver resolver = PkgClassInfo.instance.getSymbol(name.value);
                if(resolver == null) throw new UndeclaredException(name.value, start.dclName);
                String namedPart = name.value.substring(0, name.value.lastIndexOf(".") + 1);
                if(namedMap.containsKey(namedPart)) throw new DuplicateDeclearationException(namedPart, start.dclName);
                namedMap.put(namedPart, resolver);
                break;
            case PACKAGE:
                entryMap = samepkgMap;
            case STAR_IMPORT:
                String firstPart = name.value.substring(0, name.value.lastIndexOf("."));
                //note this is already done at the end;
                if(firstPart.equals("java.lang")) continue;
                addAll(firstPart, entryMap);
                break;
            default:
                //should not get here!
                break;
            }

            addAll("java.lang", staredMap);
        }

        for (MethodSymbol methodSymbol : start.getMethods()){
            List<String> types = new LinkedList<String>();
            for(DclSymbol dcl : methodSymbol.params) types.add(dcl.type.value);

            String uniqueName = generateUniqueName(methodSymbol.dclName, types);
            if(methodMap.containsKey(uniqueName)) throw new DuplicateDeclearationException(uniqueName, start.dclName);
            if(smethodMap.containsKey(uniqueName)) throw new DuplicateDeclearationException(uniqueName, start.dclName);

            final Map<String, MethodSymbol> addTo = methodSymbol.isStatic() ? methodMap : smethodMap;
            addTo.put(uniqueName, methodSymbol);

            getClass(methodSymbol.type.value, false);
        }

        for(DclSymbol fieldSymbol : start.getFields()){
            if(fieldMap.containsKey(fieldSymbol.dclName) || sfieldMap.containsKey(fieldSymbol.dclName))
                throw new UndeclaredException(fieldSymbol.dclName, start.dclName);

            final Map<String, DclSymbol> addTo = fieldSymbol.isStatic() ? sfieldMap : fieldMap;
            addTo.put(fieldSymbol.dclName, fieldSymbol);
            getClass(fieldSymbol.type.value, false);
        }
    }

    private void addAll(String firstPart, Map<String, PkgClassResolver> entryMap) throws DuplicateDeclearationException{
        for(Entry<String, PkgClassResolver> entry : PkgClassInfo.instance.getNamespaceParts(firstPart)){
            String ename = entry.getKey();
            entryMap.put(ename, entry.getValue());
            if(entryMap.containsKey(ename)) throw new DuplicateDeclearationException(ename, start.dclName);
        }
    }

    private DclSymbol getDcl(String name, boolean isStatic) throws UndeclaredException, ImplicitStaticConversionException{
        final Map<String, DclSymbol> getFrom = isStatic ? sfieldMap : fieldMap;
        final Map<String, DclSymbol> notFrom = isStatic ? fieldMap : sfieldMap;

        if(notFrom.get(name) != null) throw new ImplicitStaticConversionException(name);

        DclSymbol retVal = getFrom.get(name);

        return retVal;
    }

    public List<DclSymbol> findDcl(String name, boolean isStatic)throws UndeclaredException, ImplicitStaticConversionException {
        if(start == null) throw new UndeclaredException(name, "primatives");

        String [] nameParts = name.split("\\.");

        DclSymbol retVal;
        if(nameParts.length == 1){
            retVal = getDcl(name, isStatic);
            if(retVal == null) throw new UndeclaredException(name, start.dclName);
            return  Arrays.asList(new DclSymbol []{ retVal });
        }

        DclSymbol dcl = getDcl(nameParts[0], isStatic);
        List<DclSymbol> dclList = new LinkedList<DclSymbol>();

        PkgClassResolver pkgResolver = null;

        int i = 1;

        if(dcl != null){
            dclList.add(dcl);
            pkgResolver = getClass(dcl.type.value, true);
        }else{
            StringBuilder sb = new StringBuilder(nameParts[0]);
            pkgResolver = getClass(nameParts[0], false);

            //At least one must be a field
            for(; pkgResolver == null && i < nameParts.length - 2; i++){
                sb.append("." + nameParts[i]);
                pkgResolver = getClass(sb.toString(), false);
            }
        }

        if(pkgResolver == null) throw new UndeclaredException(name, start.dclName);

        for(; i < nameParts.length; i++){
            dcl = pkgResolver.getDcl(nameParts[i], isStatic);
            dclList.add(dcl);
            pkgResolver = getClass(dcl.type.value, true);
        }

        return dclList;
    }

    public MethodSymbol findMethod(String name, boolean isStatic, Iterable<String> paramTypes) throws UndeclaredException{
        if(start == null) throw new UndeclaredException(name, "primatives");
        final Map<String, MethodSymbol> getFrom = isStatic ? methodMap : smethodMap;
        MethodSymbol retVal = getFrom.get(generateUniqueName(name, paramTypes));
        if(retVal == null) throw new UndeclaredException(name, start.dclName);
        return retVal;
    }

    public PkgClassResolver getClass(String name, boolean die) throws UndeclaredException {
        if(name.equals("this")) return this;
        if(namedMap.containsKey(name)) return namedMap.get(name);
        if(samepkgMap.containsKey(name)) return samepkgMap.get(name);
        if(staredMap.containsKey(name)) return staredMap.get(name);

        PkgClassResolver fullResolve = PkgClassInfo.instance.getSymbol(name);
        if(fullResolve != null)return fullResolve;

        if(die) throw new UndeclaredException(name, start.dclName);
        return null;
    }

    private void build(Set<PkgClassResolver> visited) throws UndeclaredException, CircularDependancyException{
        if(visited.contains(this)) throw new CircularDependancyException(start.dclName);

        visited.add(this);

        if(isBuilt) return;
        isBuilt = true;

        final List<String> mustHave = new LinkedList<String>();
        LinkedList<ISymbol> inherits = new LinkedList<ISymbol>();

        PkgClassResolver building = null;

        if(start.superName != null){
            building = getClass(start.name, false);
            building.build(visited);
            for(ISymbol child : building.start.children){

                if(DclSymbol.class.isInstance(child)){
                    DclSymbol dcl = (DclSymbol) child;
                    //TODO for next assignment
                }else{
                    MethodSymbol method = (MethodSymbol) child;
                    //TODO for next assignment
                }
            }
        }

        //TODO take all of the super first, make sure all abstract are impl if not abs
        for(String impl : start.impls){
            building = getClass(impl, false);
            building.build();
        }
        //TODO take all interface stuff if it is an interface and if it's not then make sure it impls every one of them or is abs?

        //TODO build the methods and everything else in the tree (fields)
    }

    public void build() throws UndeclaredException, CircularDependancyException{
        build(new HashSet<PkgClassResolver>());
    }
}
