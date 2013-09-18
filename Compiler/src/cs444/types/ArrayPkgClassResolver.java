package cs444.types;

import java.util.*;

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

public class ArrayPkgClassResolver extends APkgClassResolver {
    public final APkgClassResolver resolver;
    public boolean isBuilt = false;

    public static String getArrayName(final String name){
        return name + "__array";
    }

    public ArrayPkgClassResolver(final APkgClassResolver resolver) {
        super(getArrayName(resolver.name), resolver.pkg, true);
        this.resolver = resolver;
        build(null, false, false);
        for(final String s : resolver.assignableTo){
        	//Array of primative is not assignable to array of object.
        	if(!((JoosNonTerminal.primativeNumbers.contains(resolver.name) ||
        			JoosNonTerminal.otherPrimatives.contains(resolver.name)) &&
        			JoosNonTerminal.OBJECT.equals(s)))
        		assignableTo.add(getArrayName(s));
        }

        PkgClassInfo.instance.symbolMap.put(fullName, this);

        try{
            final TypeSymbol ts = TypeSymbol.getPrimative(JoosNonTerminal.VOID);
            final NameSymbol name = new NameSymbol(JoosNonTerminal.THIS, Type.ID_SYMBOL);
            final String [] indexTypes = { JoosNonTerminal.INTEGER, JoosNonTerminal.CHAR, JoosNonTerminal.BYTE, JoosNonTerminal.SHORT };

            for (final String indexType : indexTypes) addArrayConstructorFor(indexType, ts, name, null);
        }catch (final Exception e){
            e.printStackTrace();
        }

        for(final String s : JoosNonTerminal.arraysExtend){
            assignableTo.add(s);
            final APkgClassResolver implInterf = PkgClassInfo.instance.getSymbol(s);
            // check for null for the tests that doesn't include StdLib and use arrays
            if (implInterf != null) implInterfs.add((PkgClassResolver) implInterf);
        }
        superClass = PkgClassInfo.instance.getSymbol(JoosNonTerminal.OBJECT);
    }

    private void addArrayConstructorFor(final String indType, final TypeSymbol ts, final NameSymbol name, final Platform<?, ?> platform)
            throws IllegalModifierException, UnsupportedException, UndeclaredException {

        List<DclSymbol> dcls = new LinkedList<DclSymbol>();
        final TypeSymbol t = TypeSymbol.getPrimative(indType);
        final DclSymbol dcl = new DclSymbol("i", null, t, true);
        dcl.dclInResolver = this;
        dcls = new LinkedList<DclSymbol>();
        dcls.add(dcl);
        final MethodHeader header = new MethodHeader(name, ts, dcls);

        //ANonTerminal from, ANonTerminal body
        final ConstructorSymbol cs = new ConstructorSymbol(header, null, null);
        cs.forcePublic();
        cs.resolver = this;
        cs.dclInResolver = this;
        final String uniqueName = generateUniqueName(cs, JoosNonTerminal.THIS);
        constructors.put(uniqueName, cs);
    }

    @Override
    public APkgClassResolver getClass(final String name, final boolean die) throws UndeclaredException {
        return resolver.getClass(name, die);
    }

    @Override
    public APkgClassResolver accessor() throws CompilerException {
        return resolver;
    }

    @Override
    protected void build(final Set<PkgClassResolver> visited, final boolean mustBeInterface, final boolean mustBeClass){
        if(!isBuilt){
            isBuilt = true;
            final APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(JoosNonTerminal.OBJECT);
            smethodMap.putAll(resolver.smethodMap);
            methodMap.putAll(resolver.methodMap);
            sfieldMap.putAll(resolver.sfieldMap);
            fieldMap.putAll(resolver.fieldMap);
            final TypeSymbol intType = TypeSymbol.getPrimative(JoosNonTerminal.INTEGER);

            try{
                final DclSymbol length = new DclSymbol("length", null, intType, null, false);
                length.forcePublic();
                length.forceFinal();
                fieldMap.put("length", length);
            }catch (final CompilerException ce){
                ce.printStackTrace();
            }
            try{
                final PkgClassResolver obj = (PkgClassResolver) getClass(JoosNonTerminal.OBJECT, true);
                if(!obj.isBuilt) obj.build();
                for(final AMethodSymbol m : obj.start.getMethods()){
                    final String uniqueName = generateUniqueName(m, m.dclName);
                    if(m.isStatic()) smethodMap.put(uniqueName, m);
                    else methodMap.put(uniqueName, m);
                }
            }catch(final Exception e){ }
        }
    }

    @Override
    public APkgClassResolver findClass(final String name) throws UndeclaredException {
        return resolver.findClass(name);
    }

    @Override
    public void linkLocalNamesToDcl(final Collection<Platform<?, ?>> platforms) throws CompilerException {
        for(final ConstructorSymbol cs : constructors.values()){
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
    public void generateCode(final CodeGenVisitor<?, ?> visitor) {
        for(final ConstructorSymbol cs : constructors.values()) cs.accept(visitor);
    }

    @Override
    public boolean shouldGenCode() {
        return true;
    }

    @Override
    public void computeFieldOffsets(final Platform<?, ?> platform){
        fieldMap.get(JoosNonTerminal.LENGTH).setOffset(platform.getObjectLayout().objSize(), platform);
    }

    @Override
    public long getStackSize(final Platform<?, ?> platform) {
        return platform.getSizeHelper().getBytePushSizeOfType("int");
    }

    @Override
    public long getRefStackSize(final SizeHelper<?, ?> sizeHelper) {
        return sizeHelper.getDefaultStackSize();
    }

    @Override
    public long getRealSize(final SizeHelper<?, ?> sizeHelper) {
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
    public void checkFields(final Collection<Platform<?, ?>> platforms) throws CompilerException{
        final LocalDclLinker linker = new LocalDclLinker(fullName, true, platforms);
        for(final DclSymbol dcl : getDcls()){
            dcl.accept(linker);
        }
    }
}
