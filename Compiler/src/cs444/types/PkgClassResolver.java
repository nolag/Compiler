package cs444.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ImplementationLevel;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ProtectionLevel;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.exceptions.CircularDependancyException;
import cs444.types.exceptions.DuplicateDeclearationException;
import cs444.types.exceptions.IllegalMethodOverloadException;
import cs444.types.exceptions.ImplicitStaticConversionException;
import cs444.types.exceptions.UndeclaredException;

public class PkgClassResolver {
    private final AInterfaceOrClassSymbol start;

    private final Map<String, MethodSymbol> methodMap = new HashMap<String, MethodSymbol>();
    private final Map<String, MethodSymbol> smethodMap = new HashMap<String, MethodSymbol>();
    private final Map<String, DclSymbol> fieldMap = new HashMap<String, DclSymbol>();
    private final Map<String, DclSymbol> sfieldMap = new HashMap<String, DclSymbol>();

    private final Map<String, DclSymbol> hfieldMap = new HashMap<String, DclSymbol>();
    private final Map<String, DclSymbol> hsfieldMap = new HashMap<String, DclSymbol>();

    private final Map<String, PkgClassResolver> namedMap = new HashMap<String, PkgClassResolver>();
    private final Map<String, PkgClassResolver> samepkgMap = new HashMap<String, PkgClassResolver>();
    private final Map<String, PkgClassResolver> staredMap = new HashMap<String, PkgClassResolver>();
    private final Set<String> assignableTo = new HashSet<String>();

    private final String fullName;

    private boolean isBuilt = false;


    public static final PkgClassResolver primResolver = new PkgClassResolver();

    private static final Map<AInterfaceOrClassSymbol, PkgClassResolver> resolverMap =
            new HashMap<AInterfaceOrClassSymbol, PkgClassResolver>();

    private static String generateUniqueName(String name, Iterable<String> types){
        StringBuilder sb = new StringBuilder(name + "-");

        for (String type : types) sb.append(type + "*");

        return sb.toString();
    }

    private static String generateUniqueName(MethodSymbol methodSymbol){
        List<String> types = new LinkedList<String>();
        for(DclSymbol param : methodSymbol.params) types.add(param.type.value);
        return generateUniqueName(methodSymbol.dclName, types);
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
        fullName = "Primative";
    }

    private PkgClassResolver(AInterfaceOrClassSymbol start) throws UndeclaredException, DuplicateDeclearationException{
        this.start = start;
        String myName = start.dclName;

        for(ISymbol symbol : start.children){
            if(!NameSymbol.class.isInstance(symbol)) continue;
            NameSymbol name = (NameSymbol) symbol;

            switch(name.type){
            case IMPORT:
                PkgClassResolver resolver = PkgClassInfo.instance.getSymbol(name.value);
                if(resolver == null) throw new UndeclaredException(name.value, start.dclName);
                String namedPart = name.value.substring(0, name.value.lastIndexOf(".") + 1);
                if(namedMap.containsKey(namedPart)) throw new DuplicateDeclearationException(namedPart, start.dclName);
                namedMap.put(namedPart, resolver);
                break;
            case PACKAGE:
                addAll(name.value, samepkgMap);
                myName = name.value + "." + myName;
                break;
            case STAR_IMPORT:
                String firstPart = name.value.substring(0, name.value.lastIndexOf("."));
                //note this is already done at the end;
                if(firstPart.equals("java.lang")) continue;
                addAll(firstPart, staredMap);
                break;
            default:
                //should not get here!
                break;
            }

            addAll("java.lang", staredMap);
        }

        fullName = myName;

        for (MethodSymbol methodSymbol : start.getMethods()){
            String uniqueName = generateUniqueName(methodSymbol);
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

    private DclSymbol getDcl(String name, boolean isStatic, PkgClassResolver pkgClass)
            throws UndeclaredException, ImplicitStaticConversionException{

        Map<String, DclSymbol> getFrom = isStatic ? sfieldMap : fieldMap;

        DclSymbol retVal = getFrom.get(name);

        //If it is not assignable to this and it's protected see if there is a hidden one.
        if(retVal.getProtectionLevel() == ProtectionLevel.PROTECTED && !pkgClass.assignableTo.contains(fullName)){
            getFrom = isStatic ? hsfieldMap : hfieldMap;
            retVal = getFrom.get(name);
        }

        return retVal;
    }

    public List<DclSymbol> findDcl(String name, boolean isStatic, PkgClassResolver pkgClass)
            throws UndeclaredException, ImplicitStaticConversionException {

        if(start == null) throw new UndeclaredException(name, "primatives");

        String [] nameParts = name.split("\\.");

        DclSymbol retVal;
        if(nameParts.length == 1){
            retVal = getDcl(name, isStatic, pkgClass);
            if(retVal == null) throw new UndeclaredException(name, start.dclName);
            return  Arrays.asList(new DclSymbol []{ retVal });
        }

        DclSymbol dcl = getDcl(nameParts[0], isStatic, pkgClass);
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
            dcl = pkgResolver.getDcl(nameParts[i], isStatic, pkgClass);
            dclList.add(dcl);
            pkgResolver = getClass(dcl.type.value, true);
        }

        return dclList;
    }

    public MethodSymbol findMethod(String name, boolean isStatic, Iterable<String> paramTypes, PkgClassResolver pkgClass) throws UndeclaredException{
        if(start == null) throw new UndeclaredException(name, "primatives");
        final Map<String, MethodSymbol> getFrom = isStatic ? methodMap : smethodMap;
        MethodSymbol retVal = getFrom.get(generateUniqueName(name, paramTypes));

        if(retVal == null) throw new UndeclaredException(name, start.dclName);

        if(retVal.getProtectionLevel() == ProtectionLevel.PROTECTED && !pkgClass.assignableTo.contains(fullName))
            throw new UndeclaredException(name, start.dclName);

        return retVal;
    }

    public PkgClassResolver getClass(String name, boolean die) throws UndeclaredException {
        if(namedMap.containsKey(name)) return namedMap.get(name);
        if(samepkgMap.containsKey(name)) return samepkgMap.get(name);
        if(staredMap.containsKey(name)) return staredMap.get(name);

        PkgClassResolver fullResolve = PkgClassInfo.instance.getSymbol(name);
        if(fullResolve != null)return fullResolve;

        if(die) throw new UndeclaredException(name, start.dclName);
        return null;
    }

    private void copyInfo(PkgClassResolver building, Set<PkgClassResolver> visited, List<Set<PkgClassResolver>> resolvedSets, boolean inter)
            throws IllegalMethodOverloadException, UndeclaredException, CircularDependancyException, UnsupportedException{

        Set<PkgClassResolver> cpySet = new HashSet<PkgClassResolver>(visited);
        building.build(cpySet, inter);
        resolvedSets.add(cpySet);

        //copy in reverse order so that when they are added to the start they are in order
        List<ISymbol> copyChildren = new LinkedList<ISymbol>(building.start.children);
        Collections.reverse(copyChildren);

        for(ISymbol child : copyChildren){
            if(DclSymbol.class.isInstance(child)){
                DclSymbol dcl = (DclSymbol) child;
                DclSymbol field = fieldMap.get(dcl.dclName);
                field = field == null ? sfieldMap.get(dcl.dclName) : field;
                if(field == null){
                    Map<String, DclSymbol> addTo = dcl.isStatic() ? fieldMap : sfieldMap;
                    addTo.put(dcl.dclName, dcl);
                }else if(field.getProtectionLevel() != ProtectionLevel.PUBLIC && dcl.getProtectionLevel() == ProtectionLevel.PUBLIC){
                    Map<String, DclSymbol> addTo = dcl.isStatic() ? hfieldMap : hsfieldMap;
                    addTo.put(dcl.dclName, dcl);
                }
                start.children.add(0, dcl);
            }else{
                MethodSymbol methodSymbol = (MethodSymbol) child;
                String uniqueName = generateUniqueName(methodSymbol);
                MethodSymbol has = methodMap.get(uniqueName);
                has = has == null ? smethodMap.get(uniqueName) : has;
                MethodSymbol is = has == null ? methodSymbol : has;
                //If it has it move it to the front so that it's in the correct place for the super's this
                start.children.remove(is);
                start.children.add(0, is);
                if(has != null){
                    if(is.isStatic() != methodSymbol.isStatic())
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is static and not static");
                    if(methodSymbol.getImplementationLevel() == ImplementationLevel.FINAL && has != null)
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is final, but overrided");
                    if(methodSymbol.getProtectionLevel() == ProtectionLevel.PUBLIC && is.getProtectionLevel() != ProtectionLevel.PUBLIC)
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is public, but overrided is not");
                    if(methodSymbol.getProtectionLevel() == ProtectionLevel.PROTECTED
                            && is.getProtectionLevel() != ProtectionLevel.PUBLIC && is.getProtectionLevel() != ProtectionLevel.PROTECTED)
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is protected, but overrided is not protected or public");
                    //covarient return types not allowed in JOOS, it was added in java 5
                    if(!methodSymbol.type.value.equals(is.type.value))
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "return types don't match");
                }
            }
        }
    }

    private void build(Set<PkgClassResolver> visited, boolean mustBeInterface)
            throws UndeclaredException, CircularDependancyException, IllegalMethodOverloadException, UnsupportedException{


        if(visited.contains(this)) throw new CircularDependancyException(start.dclName);
        if(mustBeInterface && start.isClass()) throw new UnsupportedException("Interface extending a class");
        visited.add(this);

        if(!isBuilt){
            mustBeInterface |= !start.isClass();

            PkgClassResolver building = null;

            List<Set<PkgClassResolver>> resolvedSets = new LinkedList<Set<PkgClassResolver>>();

            if(start.superName != null){
                building = getClass(start.name, false);
                copyInfo(building, visited, resolvedSets, mustBeInterface);
            }

            for(String impl : start.impls){
                building = getClass(impl, false);
                copyInfo(building, visited, resolvedSets, mustBeInterface);
            }

            for(Set<PkgClassResolver> pkgSet : resolvedSets) visited.addAll(pkgSet);

            start.validate();

            for(PkgClassResolver resolver : visited) assignableTo.add(resolver.fullName);
            //Java specific
            assignableTo.add("java.lang.Object");
        }else{
            for(String s : assignableTo) visited.add(PkgClassInfo.instance.getSymbol(s));
            isBuilt = true;
        }
    }

    public void build() throws UndeclaredException, CircularDependancyException, IllegalMethodOverloadException, UnsupportedException{
        build(new HashSet<PkgClassResolver>(), false);
    }

    public PkgClassResolver getSuper() throws UndeclaredException{
        return getClass(start.superName, true);
    }
}
