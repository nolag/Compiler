package cs444.types;

import cs444.CompilerException;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.parser.IASTBuilder;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ImplementationLevel;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ProtectionLevel;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.static_analysis.ConstantExprBuilder;
import cs444.types.exceptions.*;

import java.util.*;
import java.util.Map.Entry;

public class PkgClassResolver extends APkgClassResolver {
    public static final PkgClassResolver badResolve = new PkgClassResolver("!invalid");
    private static final Map<AInterfaceOrClassSymbol, PkgClassResolver> resolverMap =
            new HashMap<AInterfaceOrClassSymbol, PkgClassResolver>();
    //Used for testing so that when tests with nostd lib are run the resolver won't be different for primaitves
    private static final Map<String, PkgClassResolver> primResolvers = new HashMap<>();
    private final Map<String, PkgClassResolver> samepkgMap = new HashMap<String, PkgClassResolver>();
    private final Map<String, PkgClassResolver> staredMap = new HashMap<String, PkgClassResolver>();
    private final Set<String> imported = new HashSet<String>();
    protected AInterfaceOrClassSymbol start;

    private PkgClassResolver(String name) {
        //final String name, final String pkg, final boolean isFinal, final SizeHelper sizeHelper
        super(name, null, true);
        start = null;
        isBuilt = true;
    }

    private PkgClassResolver(AInterfaceOrClassSymbol start) throws UndeclaredException,
            DuplicateDeclarationException {

        super(start.dclName, getPkg(start), start.getImplementationLevel() == ImplementationLevel.FINAL);
        namedMap.put(start.dclName, this);
        this.start = start;
    }

    private static String getPkg(AInterfaceOrClassSymbol start) {
        String mypkg = DEFAULT_PKG;
        Iterator<NameSymbol> pkg = start.pkgImports.iterator();

        if (pkg.hasNext()) {
            NameSymbol first = pkg.next();
            if (first.type == NameSymbol.Type.PACKAGE) {
                mypkg = first.value;
            }
        }
        return mypkg;
    }

    public static PkgClassResolver getResolver(AInterfaceOrClassSymbol start)
            throws UndeclaredException, DuplicateDeclarationException {

        PkgClassResolver resolver = resolverMap.get(start);

        if (resolver == null) {
            resolver = new PkgClassResolver(start);
            resolverMap.put(start, resolver);
        }

        return resolver;
    }

    public static PkgClassResolver getPrimativeResolver(String name) {
        PkgClassResolver resolver = primResolvers.get(name);
        if (resolver != null) {
            return resolver;
        }
        resolver = new PkgClassResolver(name);
        primResolvers.put(name, resolver);
        return resolver;
    }

    //For testing only
    public static void reset() {
        resolverMap.clear();
    }

    private void addAll(String firstPart, Map<String, PkgClassResolver> entryMap) throws DuplicateDeclarationException {
        if (imported.contains(firstPart)) {
            return;
        }
        for (Entry<String, PkgClassResolver> entry : PkgClassInfo.instance.getNamespaceParts(firstPart)) {
            String ename = entry.getKey();
            if (namedMap.containsKey(ename)) {
                continue;
            }
            //According to trying in java, this is fine as long as you don't go to use it, so don't let them use it.
            //if(entryMap.containsKey(ename)) throw new DuplicateDeclarationException(ename, start.dclName);
            if (entryMap.containsKey(ename)) {
                entryMap.put(ename, badResolve);
            } else {
                entryMap.put(ename, entry.getValue());
            }
        }
        imported.add(firstPart);
    }

    @Override
    public APkgClassResolver getClass(String name, boolean die) throws UndeclaredException {
        APkgClassResolver retVal = null;
        if (namedMap.containsKey(name)) {
            retVal = namedMap.get(name);
        } else if (samepkgMap.containsKey(name)) {
            retVal = samepkgMap.get(name);
        } else if (staredMap.containsKey(name)) {
            retVal = staredMap.get(name);
        } else {
            retVal = PkgClassInfo.instance.getSymbol(name);
        }

        if ((retVal == null || retVal == badResolve) && die) {
            throw new UndeclaredException(name, start.dclName);
        }
        return retVal;
    }

    @Override
    public APkgClassResolver findClass(String name) throws UndeclaredException {
        String[] nameParts = name.split("\\.");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nameParts.length - 1; i++) {
            sb.append(nameParts[i]);
            if (namedMap.containsKey(sb.toString())) {
                throw new UndeclaredException(name, fullName);
            }
            if (samepkgMap.containsKey(sb.toString())) {
                throw new UndeclaredException(name, fullName);
            }
            if (staredMap.containsKey(sb.toString())) {
                throw new UndeclaredException(name, fullName);
            }

            sb.append('.');
        }

        return getClass(name, true);
    }

    public void verifyObject() throws CompilerException {
        PkgClassResolver obj = (PkgClassResolver) getClass(JoosNonTerminal.OBJECT, true);
        if (obj == this) {
            return;
        }
        obj.build();
        superClass = obj;

        for (AMethodSymbol methodSymbol : obj.start.getMethods()) {
            String uniqueName = generateUniqueName(methodSymbol, methodSymbol.dclName);
            AMethodSymbol has = methodMap.get(uniqueName);
            has = has == null ? smethodMap.get(uniqueName) : has;
            AMethodSymbol is = has == null ? methodSymbol : has;

            if (start.isClass()) {
                //If it has it move it to the front so that it's in the correct place for the super's this
                start.children.remove(has);
                start.children.add(0, is);
                methodMap.put(uniqueName, is);
            }

            if (has != null) {
                if (is.isStatic() != methodSymbol.isStatic()) {
                    throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is static and not " +
                            "static");
                }
                if (methodSymbol.getImplementationLevel() == ImplementationLevel.FINAL && has != null) {
                    throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is final, but overrided");
                }
                if (methodSymbol.getProtectionLevel() == ProtectionLevel.PUBLIC && is.getProtectionLevel() != ProtectionLevel.PUBLIC) {
                    throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is public, but " +
                            "overrided is not");
                }
                if (methodSymbol.getProtectionLevel() == ProtectionLevel.PROTECTED
                        && is.getProtectionLevel() != ProtectionLevel.PUBLIC && is.getProtectionLevel() != ProtectionLevel.PROTECTED) {
                    throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is protected, but " +
                            "overrided is not protected or public");
                }
                //covarient return types not allowed in JOOS, it was added in java 5
                if (methodSymbol.resolver.getClass(is.type.value, true) != obj.getClass(methodSymbol.type.value,
                        true)) {
                    throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "return types don't " +
                            "match");
                }
            }
        }
    }

    private void copyInfo(PkgClassResolver building, Set<PkgClassResolver> visited,
                          List<Set<PkgClassResolver>> resolvedSets, boolean mustBeInterface,
                          boolean mustBeClass) throws CompilerException {

        if (building.isFinal) {
            throw new IllegalExtendsException(start.superName);
        }
        Set<PkgClassResolver> cpySet = new HashSet<PkgClassResolver>(visited);
        building.build(cpySet, mustBeInterface, mustBeClass);
        resolvedSets.add(cpySet);

        //copy in reverse order so that when they are added to the start they are in order
        List<ISymbol> copyChildren = new LinkedList<ISymbol>(building.start.children);
        Collections.reverse(copyChildren);

        for (ISymbol child : copyChildren) {
            if (child instanceof DclSymbol) {
                DclSymbol dcl = (DclSymbol) child;
                if (dcl.getProtectionLevel() != ProtectionLevel.PRIVATE) {
                    DclSymbol field = fieldMap.get(dcl.dclName);
                    field = field == null ? sfieldMap.get(dcl.dclName) : field;
                    //If another lookup already exists, then this field is hidden.  If it is private, we don't get
                    // access to it.
                    Map<String, DclSymbol> addTo;
                    if (field == null) {
                        addTo = dcl.isStatic() ? sfieldMap : fieldMap;
                    } else {
                        addTo = dcl.isStatic() ? hsfieldMap : hfieldMap;
                    }
                    addTo.put(dcl.dclName, dcl);
                }
                start.children.add(0, dcl);
            } else if (child instanceof AMethodSymbol) {
                AMethodSymbol methodSymbol = (AMethodSymbol) child;
                String uniqueName = generateUniqueName(methodSymbol, methodSymbol.dclName);
                AMethodSymbol has = methodMap.get(uniqueName);
                has = has == null ? smethodMap.get(uniqueName) : has;
                AMethodSymbol is = has == null ? methodSymbol : has;

                //If it has it move it to the front so that it's in the correct place for the super's this
                start.children.remove(is);
                start.children.add(0, is);
                if (has != null) {
                    if (is.isStatic() != methodSymbol.isStatic()) {
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is static and not " +
                                "static");
                    }
                    if (methodSymbol.getImplementationLevel() == ImplementationLevel.FINAL && has != null) {
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is final, but " +
                                "overrided");
                    }
                    if (methodSymbol.getProtectionLevel() == ProtectionLevel.PUBLIC && is.getProtectionLevel() != ProtectionLevel.PUBLIC) {
                        if (has.getImplementationLevel() == ImplementationLevel.ABSTRACT && has.resolver != this && !building.start.isClass()) {
                            // replace "is" for methodSymbol
                            replaceMethod(methodSymbol, is);
                            continue;
                        } else {
                            throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is public, but " +
                                    "overrided is not");
                        }
                    }
                    if (methodSymbol.getProtectionLevel() == ProtectionLevel.PROTECTED
                            && is.getProtectionLevel() != ProtectionLevel.PUBLIC && is.getProtectionLevel() != ProtectionLevel.PROTECTED) {
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is protected, but " +
                                "overrided is not protected or public");
                    }
                    //covarient return types not allowed in JOOS, it was added in java 5
                    if (methodSymbol.resolver.getClass(is.type.value, true) != building.getClass(methodSymbol.type.value, true)) {
                        throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "return types don't " +
                                "match");
                    }
                    Map<String, AMethodSymbol> addTo = methodSymbol.isStatic() ? hsmethodMap : hmethodMap;
                    addTo.put(uniqueName, methodSymbol);
                } else {
                    if (methodSymbol.getImplementationLevel() == ImplementationLevel.ABSTRACT && start.getImplementationLevel() != ImplementationLevel.ABSTRACT) {
                        throw new UnimplementedException(fullName, methodSymbol.dclName);
                    }
                    //Can't call the private one
                    if (methodSymbol.getProtectionLevel() == ProtectionLevel.PRIVATE) {
                        continue;
                    }
                    Map<String, AMethodSymbol> addTo = methodSymbol.isStatic() ? smethodMap : methodMap;
                    addTo.put(uniqueName, methodSymbol);
                }
            }
        }
    }

    private void replaceMethod(AMethodSymbol newMethod, AMethodSymbol oldMethod)
            throws UndeclaredException {
        Map<String, AMethodSymbol> addNewTo = newMethod.isStatic() ? smethodMap : methodMap;
        Map<String, AMethodSymbol> addOldTo = oldMethod.isStatic() ? hsmethodMap : hmethodMap;
        Map<String, AMethodSymbol> remOldFrom = oldMethod.isStatic() ? methodMap : smethodMap;

        String uniqueName = generateUniqueName(oldMethod, oldMethod.dclName);
        start.children.remove(oldMethod);
        start.children.add(newMethod);
        remOldFrom.remove(uniqueName);
        addOldTo.put(uniqueName, oldMethod);
        addNewTo.put(uniqueName, newMethod);
    }

    @Override
    protected void build(Set<PkgClassResolver> visited, boolean mustBeInterface, boolean mustBeClass) throws CompilerException {
        if (visited.contains(this)) {
            throw new CircularDependancyException(start.dclName);
        }
        if (mustBeInterface && start.isClass()) {
            throw new UnsupportedException("Interface extending a class");
        }
        if (mustBeClass && !start.isClass()) {
            throw new UnsupportedException("Class extending interface");
        }

        visited.add(this);

        if (!isBuilt) {
            isBuilt = true;
            for (NameSymbol symbol : start.pkgImports) {
                NameSymbol name = symbol;

                switch (name.type) {
                    case IMPORT:
                        PkgClassResolver resolver =
                                (PkgClassResolver) PkgClassInfo.instance.getSymbol(name.value);
                        if (resolver == null) {
                            throw new UndeclaredException(name.value, start.dclName);
                        }

                        String typeName = name.value.substring(name.value.lastIndexOf(".") + 1
                        );

                        if (namedMap.containsKey(typeName) && namedMap.get(typeName) != resolver) {
                            throw new DuplicateDeclarationException(name.value, start.dclName);
                        }

                        namedMap.put(name.value, resolver);
                        namedMap.put(typeName, resolver);
                        break;
                    case STAR_IMPORT:
                        if (name.value.equals(JoosNonTerminal.LANG)) {
                            continue;
                        }
                        addAll(name.value, staredMap);
                        break;
                    default:
                        break;
                }
            }

            addAll(pkg, samepkgMap);
            addAll("java.lang", staredMap);

            for (AMethodSymbol methodSymbol : start.getMethods()) {
                methodSymbol.resolver = methodSymbol.type.isArray ? getArrayVersion() : this;
                String uniqueName = generateUniqueName(methodSymbol, methodSymbol.dclName);
                if (methodMap.containsKey(uniqueName)) {
                    throw new DuplicateDeclarationException(uniqueName, start.dclName);
                }
                if (smethodMap.containsKey(uniqueName)) {
                    throw new DuplicateDeclarationException(uniqueName, start.dclName);
                }

                Map<String, AMethodSymbol> addTo = methodSymbol.isStatic() ? smethodMap : methodMap;
                addTo.put(uniqueName, methodSymbol);
                APkgClassResolver mresolver = findClass(methodSymbol.type.value);
                if (methodSymbol.type.isArray) {
                    mresolver = mresolver.getArrayVersion();
                }
                methodSymbol.type.setTypeDclNode(mresolver);
                methodSymbol.dclInResolver = this;
            }

            for (DclSymbol fieldSymbol : start.getFields()) {
                if (fieldMap.containsKey(fieldSymbol.dclName) || sfieldMap.containsKey(fieldSymbol.dclName)) {
                    throw new UndeclaredException(fieldSymbol.dclName, fullName);
                }

                fieldSymbol.type.setTypeDclNode(fieldSymbol.type.isArray ? getArrayVersion() : this);
                Map<String, DclSymbol> addTo = fieldSymbol.isStatic() ? sfieldMap : fieldMap;
                addTo.put(fieldSymbol.dclName, fieldSymbol);
                APkgClassResolver fresolver = findClass(fieldSymbol.type.value);
                if (fieldSymbol.type.isArray) {
                    fresolver = fresolver.getArrayVersion();
                }
                fieldSymbol.type.setTypeDclNode(fresolver);
                addTo(fieldSymbol);
                fieldSymbol.dclInResolver = this;
            }

            for (ConstructorSymbol constructorSymbol : start.getConstructors()) {
                constructorSymbol.resolver = this;
                String uniqueName = generateUniqueName(constructorSymbol, "this");
                if (constructors.containsKey(uniqueName)) {
                    throw new DuplicateDeclarationException(uniqueName, start.dclName);
                }
                constructors.put(uniqueName, constructorSymbol);
                TypeSymbol voidType = TypeSymbol.getPrimative(JoosNonTerminal.VOID);

                if (voidType.getTypeDclNode() == null) {
                    voidType.setTypeDclNode(PkgClassInfo.instance.getSymbol("void"));
                }

                constructorSymbol.setType(voidType);

                if (constructorSymbol.params.isEmpty()) {
                    start.setDefaultConstructor(constructorSymbol);
                }
                constructorSymbol.dclInResolver = this;
            }

            PkgClassResolver building = null;

            List<Set<PkgClassResolver>> resolvedSets = new LinkedList<Set<PkgClassResolver>>();

            if (!fullName.equals(JoosNonTerminal.OBJECT)) {
                if (start.superName != null || start.getImplementationLevel() != ImplementationLevel.ABSTRACT) {
                    if (start.superName == null) {
                        start.superName = JoosNonTerminal.OBJECT;
                    }
                    building = (PkgClassResolver) findClass(start.superName);
                    copyInfo(building, visited, resolvedSets, false, true);

                    if (building.start.getDefaultConstructor() == null) {
                        throw new UnsupportedException("class without default constructor.  Explicit super call is " +
                                "not supported." + building.fullName);
                    }

                    assignableTo.addAll(building.assignableTo);
                    superClass = building;
                } else {
                    verifyObject();
                }
            }

            Set<String> alreadyImps = new HashSet<String>();

            for (String impl : start.impls) {
                building = (PkgClassResolver) findClass(impl);
                if (alreadyImps.contains(building.fullName)) {
                    throw new DuplicateDeclarationException(impl, fullName);
                }

                //Interfaces must be implemented, unless this is abstract
                if (start.getImplementationLevel() == ImplementationLevel.ABSTRACT) {
                    copyInfo(building, visited, resolvedSets, true, false);
                } else {
                    Set<PkgClassResolver> cpySet = new HashSet<PkgClassResolver>(visited);
                    building.build(cpySet, true, false);
                    resolvedSets.add(cpySet);
                    //we only have methods in interfaces in JOOS
                    for (AMethodSymbol methodSymbol : building.start.getMethods()) {
                        String uniqueName = generateUniqueName(methodSymbol, methodSymbol.dclName);
                        //No method can be static in an interface.
                        if (!methodMap.containsKey(uniqueName)) {
                            throw new UndeclaredException(uniqueName, fullName);
                        }
                        AMethodSymbol hasMethod = methodMap.get(uniqueName);
                        APkgClassResolver hasResolver = hasMethod.resolver;
                        if (hasResolver.findClass(hasMethod.type.value) != methodSymbol.resolver.findClass(methodSymbol.type.value)) {
                            throw new UndeclaredException(uniqueName, fullName);
                        }

                        //only public interfaces in JOOS
                        if (hasMethod.getProtectionLevel() != ProtectionLevel.PUBLIC) {
                            throw new IllegalMethodOverloadException(fullName, methodSymbol.dclName, "is public, but " +
                                    "overrided is not");
                        }
                    }
                }

                assignableTo.addAll(building.assignableTo);
                alreadyImps.add(building.fullName);
                implInterfs.add(building);
            }

            for (Set<PkgClassResolver> pkgSet : resolvedSets) {
                visited.addAll(pkgSet);
            }

            start.accept(new TypeResolverVisitor(this));
        } else {
            for (String s : assignableTo) {
                visited.add((PkgClassResolver) PkgClassInfo.instance.getSymbol(s));
            }
        }
    }

    @Override
    public void linkLocalNamesToDcl(Collection<Platform<?, ?>> platforms) throws CompilerException {
        if (start == null) {
            return;
        }
        for (AMethodSymbol method : start.getUninheritedMethods()) {
            method.resolveLocalVars(fullName, platforms);
        }

        for (ConstructorSymbol consturctor : start.getConstructors()) {
            consturctor.resolveLocalVars(fullName, platforms);
        }

        for (Platform<?, ?> platform : platforms) {
            long mySize = 0;
            for (DclSymbol dcl : fieldMap.values()) {
                mySize += dcl.getType().getTypeDclNode().getRefStackSize(platform.getSizeHelper());
            }
            start.setStackSize(platform, mySize);
        }
    }

    @Override
    public void checkFields(Collection<Platform<?, ?>> platforms) throws CompilerException {
        if (start == null) {
            return;
        }
        LocalDclLinker linker = new LocalDclLinker(fullName, true, platforms);
        for (DclSymbol dcl : getUninheritedNonStaticFields()) {
            dcl.accept(linker);
        }

        linker = new LocalDclLinker(fullName, false, platforms);
        for (DclSymbol dcl : getUninheritedStaticFields()) {
            dcl.accept(linker);
        }
    }

    @Override
    public void reduceToConstantExprs() throws CompilerException {
        if (start == null) {
            return;
        }
        IASTBuilder builder = new ConstantExprBuilder();
        start = (AInterfaceOrClassSymbol) builder.build(start);
    }

    @Override
    public void analyzeReachability() throws CompilerException {
        if (start == null) {
            return;
        }
        for (AMethodSymbol method : start.getUninheritedMethods()) {
            method.analyzeReachability(fullName);
        }

        for (ConstructorSymbol constructor : start.getConstructors()) {
            constructor.analyzeReachability(fullName);
        }
    }

    @Override
    public APkgClassResolver accessor() throws UndeclaredException {
        throw new UndeclaredException("Accessors", fullName);
    }

    @Override
    protected boolean isPrimative() {
        return JoosNonTerminal.primativeNumbers.contains(fullName) || JoosNonTerminal.otherPrimatives.contains(fullName);
    }

    @Override
    protected Iterable<DclSymbol> getDcls() {
        if (start != null) {
            return start.getFields();
        }
        return Collections.emptyList();
    }

    @Override
    protected boolean isAbstract() {
        if (start == null) {
            return false;
        }
        return start.getImplementationLevel() == ImplementationLevel.ABSTRACT;
    }

    @Override
    public void generateCode(CodeGenVisitor<?, ?> visitor) {
        if (!shouldGenCode()) {
            return;
        }

        for (AMethodSymbol methodSymbol : start.getUninheritedMethods()) {
            methodSymbol.accept(visitor);
        }
        for (ConstructorSymbol cs : start.getConstructors()) {
            cs.accept(visitor);
        }
    }

    @Override
    public boolean shouldGenCode() {
        return start != null;
    }

    @Override
    public void computeFieldOffsets(Platform<?, ?> platform) {
        if (shouldGenCode()) {
            start.computeFieldOffsets(platform);
        }
    }

    @Override
    public long getStackSize(Platform<?, ?> platform) {
        if (start != null) {
            return start.getStackSize(platform);
        }
        return platform.getSizeHelper().getBytePushSizeOfType(name);
    }

    @Override
    public long getRefStackSize(SizeHelper<?, ?> sizeHelper) {
        if (start != null) {
            return sizeHelper.getDefaultStackSize();
        }
        return sizeHelper.getBytePushSizeOfType(name);
    }

    @Override
    public long getRealSize(SizeHelper<?, ?> sizeHelper) {
        return sizeHelper.getByteSizeOfType(name);
    }

    public AInterfaceOrClassSymbol getStart() {
        return start;
    }

    @Override
    public Iterable<DclSymbol> getUninheritedStaticFields() {
        List<DclSymbol> fieldsDcls = new LinkedList<DclSymbol>();

        for (DclSymbol dcl : start.getFields()) {
            if (!dcl.isStatic() || dcl.dclInResolver != this) {
                continue;
            }

            fieldsDcls.add(dcl);
        }

        return fieldsDcls;
    }

    @Override
    public Iterable<DclSymbol> getUninheritedNonStaticFields() {
        List<DclSymbol> fieldsDcls = new LinkedList<DclSymbol>();

        for (DclSymbol dcl : start.getFields()) {
            if (dcl.isStatic() || dcl.dclInResolver != this) {
                continue;
            }

            fieldsDcls.add(dcl);
        }

        return fieldsDcls;
    }
}
