package cs444.types;

import cs444.CompilerException;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.NameSymbol.Type;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.exceptions.UndeclaredException;

import java.util.*;

public class ArrayPkgClassResolver extends APkgClassResolver {
    public final APkgClassResolver resolver;

    public ArrayPkgClassResolver(APkgClassResolver resolver) {
        super(getArrayName(resolver.name), resolver.pkg, true);
        this.resolver = resolver;
        build(null, false, false);
        for (String s : resolver.assignableTo) {
            //Array of primative is not assignable to array of object.
            if (!((JoosNonTerminal.primativeNumbers.contains(resolver.name) ||
                    JoosNonTerminal.otherPrimatives.contains(resolver.name)) &&
                    JoosNonTerminal.OBJECT.equals(s))) {
                assignableTo.add(getArrayName(s));
            }
        }

        PkgClassInfo.instance.symbolMap.put(fullName, this);

        try {
            TypeSymbol ts = TypeSymbol.getPrimative(JoosNonTerminal.VOID);
            NameSymbol name = new NameSymbol(JoosNonTerminal.THIS, Type.ID_SYMBOL);
            String[] indexTypes = {JoosNonTerminal.INTEGER, JoosNonTerminal.CHAR, JoosNonTerminal.BYTE,
                    JoosNonTerminal.SHORT};

            for (String indexType : indexTypes) {
                addArrayConstructorFor(indexType, ts, name, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s : JoosNonTerminal.arraysExtend) {
            assignableTo.add(s);
            APkgClassResolver implInterf = PkgClassInfo.instance.getSymbol(s);
            // check for null for the tests that doesn't include StdLib and use arrays
            if (implInterf != null) {
                implInterfs.add((PkgClassResolver) implInterf);
            }
        }
        superClass = PkgClassInfo.instance.getSymbol(JoosNonTerminal.OBJECT);
    }

    public static String getArrayName(String name) {
        return name + "__array";
    }

    private void addArrayConstructorFor(String indType, TypeSymbol ts, NameSymbol name,
                                        Platform<?, ?> platform)
            throws IllegalModifierException, UnsupportedException, UndeclaredException {

        List<DclSymbol> dcls = new LinkedList<DclSymbol>();
        TypeSymbol t = TypeSymbol.getPrimative(indType);
        DclSymbol dcl = new DclSymbol("i", null, t, true);
        dcl.dclInResolver = this;
        dcls = new LinkedList<DclSymbol>();
        dcls.add(dcl);
        MethodHeader header = new MethodHeader(name, ts, dcls);

        //ANonTerminal from, ANonTerminal body
        ConstructorSymbol cs = new ConstructorSymbol(header, null, null);
        cs.forcePublic();
        cs.resolver = this;
        cs.dclInResolver = this;
        String uniqueName = generateUniqueName(cs, JoosNonTerminal.THIS);
        constructors.put(uniqueName, cs);
    }

    @Override
    public APkgClassResolver getClass(String name, boolean die) throws UndeclaredException {
        return resolver.getClass(name, die);
    }

    @Override
    public APkgClassResolver accessor() throws CompilerException {
        return resolver;
    }

    @Override
    protected void build(Set<PkgClassResolver> visited, boolean mustBeInterface,
                         boolean mustBeClass) {
        if (!isBuilt) {
            isBuilt = true;
            APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(JoosNonTerminal.OBJECT);
            smethodMap.putAll(resolver.smethodMap);
            methodMap.putAll(resolver.methodMap);
            sfieldMap.putAll(resolver.sfieldMap);
            fieldMap.putAll(resolver.fieldMap);
            TypeSymbol intType = TypeSymbol.getPrimative(JoosNonTerminal.INTEGER);

            try {
                DclSymbol length = new DclSymbol("length", null, intType, null, false);
                length.forcePublic();
                length.forceFinal();
                fieldMap.put("length", length);
            } catch (CompilerException ce) {
                ce.printStackTrace();
            }
            try {
                PkgClassResolver obj = (PkgClassResolver) getClass(JoosNonTerminal.OBJECT, true);
                if (!obj.isBuilt) {
                    obj.build();
                }
                for (AMethodSymbol m : obj.start.getMethods()) {
                    String uniqueName = generateUniqueName(m, m.dclName);
                    if (m.isStatic()) {
                        smethodMap.put(uniqueName, m);
                    } else {
                        methodMap.put(uniqueName, m);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public APkgClassResolver findClass(String name) throws UndeclaredException {
        return resolver.findClass(name);
    }

    @Override
    public void linkLocalNamesToDcl(Collection<Platform<?, ?>> platforms) throws CompilerException {
        for (ConstructorSymbol cs : constructors.values()) {
            cs.resolveLocalVars(fullName, platforms);
        }
    }

    @Override
    public void analyzeReachability() throws CompilerException { }

    @Override
    public void reduceToConstantExprs() {}

    @Override
    protected boolean isPrimative() {
        return false;
    }

    @Override
    protected Iterable<DclSymbol> getDcls() {
        return fieldMap.values();
    }

    @Override
    protected boolean isAbstract() {
        return resolver.isAbstract();
    }

    @Override
    public void generateCode(CodeGenVisitor<?, ?> visitor) {
        for (ConstructorSymbol cs : constructors.values()) {
            cs.accept(visitor);
        }
    }

    @Override
    public boolean shouldGenCode() {
        return true;
    }

    @Override
    public void computeFieldOffsets(Platform<?, ?> platform) {
        fieldMap.get(JoosNonTerminal.LENGTH).setOffset(platform.getObjectLayout().objSize(), platform);
    }

    @Override
    public long getStackSize(Platform<?, ?> platform) {
        return platform.getSizeHelper().getBytePushSizeOfType("int");
    }

    @Override
    public long getRefStackSize(SizeHelper<?, ?> sizeHelper) {
        return sizeHelper.getDefaultStackSize();
    }

    @Override
    public long getRealSize(SizeHelper<?, ?> sizeHelper) {
        return sizeHelper.getDefaultStackSize();
    }

    @Override
    public Iterable<DclSymbol> getUninheritedStaticFields() {
        return Collections.emptySet();
    }

    @Override
    public Iterable<DclSymbol> getUninheritedNonStaticFields() {
        return fieldMap.values();
    }

    @Override
    public void checkFields(Collection<Platform<?, ?>> platforms) throws CompilerException {
        LocalDclLinker linker = new LocalDclLinker(fullName, true, platforms);
        for (DclSymbol dcl : getDcls()) {
            dcl.accept(linker);
        }
    }
}
