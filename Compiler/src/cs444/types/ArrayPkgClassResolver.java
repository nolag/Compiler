package cs444.types;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cs444.CompilerException;
import cs444.codegen.ICodeGenVisitor;
import cs444.codegen.SizeHelper;
import cs444.lexer.Token;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodHeader;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NameSymbol.Type;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.exceptions.UndeclaredException;

public class ArrayPkgClassResolver extends APkgClassResolver {
    private final APkgClassResolver resolver;
    private DclSymbol length;

    public static String getArrayName(String name){
        return name + "__array";
    }

    private void addLenght(){
        try {
            if(length == null){
                JoosNonTerminal mods = new JoosNonTerminal("Modifiers", new ISymbol [] { new Terminal(new Token(Token.Type.PUBLIC, "public"))});
                length = new DclSymbol(JoosNonTerminal.LENGTH, mods, TypeSymbol.getPrimative(JoosNonTerminal.INTEGER), false, true);
                length.forcePublic();
            }
            fieldMap.put(JoosNonTerminal.LENGTH, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayPkgClassResolver(APkgClassResolver resolver) {
        super(getArrayName(resolver.name), resolver.pkg, true);
        this.resolver = resolver;
        build(null, false, false);
        for(String s : resolver.assignableTo) assignableTo.add(getArrayName(s));

        PkgClassInfo.instance.symbolMap.put(fullName, this);

        try{
            TypeSymbol ts = TypeSymbol.getPrimative(JoosNonTerminal.VOID);
            NameSymbol name = new NameSymbol(JoosNonTerminal.THIS, Type.ID_SYMBOL);

            String [] indexTypes = { JoosNonTerminal.INTEGER, JoosNonTerminal.CHAR, JoosNonTerminal.BYTE, JoosNonTerminal.SHORT };

            for (String indexType : indexTypes) {
                addArrayConstructorFor(indexType, ts, name);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        addLenght();

        for(String s : JoosNonTerminal.arraysExtend){
            assignableTo.add(s);
            APkgClassResolver implInterf = PkgClassInfo.instance.getSymbol(s);
            // check for null for the tests that doesn't include StdLib and use arrays
            if (implInterf != null) implInterfs.add((PkgClassResolver) implInterf);
        }
        superClass = PkgClassInfo.instance.getSymbol(OBJECT);
    }

    private void addArrayConstructorFor(String indType, TypeSymbol ts, NameSymbol name)
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
        //set the size
        try{
            cs.accept(new LocalDclLinker(fullName));
        }catch(Exception e){
            //never should get here
            e.printStackTrace();
        }
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
    protected void build(Set<PkgClassResolver> visited, boolean mustBeInterface, boolean mustBeClass){
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(OBJECT);
        smethodMap.putAll(resolver.smethodMap);
        methodMap.putAll(resolver.methodMap);
        sfieldMap.putAll(resolver.sfieldMap);
        fieldMap.putAll(resolver.fieldMap);
        TypeSymbol intType = TypeSymbol.getPrimative(JoosNonTerminal.INTEGER);

        try{
            DclSymbol length = new DclSymbol("length", null, intType, null, false);
            length.forcePublic();
            fieldMap.put("length", length);
        }catch (CompilerException ce){
            ce.printStackTrace();
        }
        try{
            PkgClassResolver obj = (PkgClassResolver) getClass(OBJECT, true);
            if(!obj.isBuilt) obj.build();
            for(AMethodSymbol m : obj.start.getMethods()){
                String uniqueName = generateUniqueName(m, m.dclName);
                if(m.isStatic()) smethodMap.put(uniqueName, m);
                else methodMap.put(uniqueName, m);
            }
        }catch(Exception e){ }
    }

    @Override
    public APkgClassResolver findClass(String name) throws UndeclaredException {
        return resolver.findClass(name);
    }

    @Override
    public void linkLocalNamesToDcl() throws CompilerException { }

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
        return null;
    }

    @Override
    protected boolean isAbstract() {
        return resolver.isAbstract();
    }

    @Override
    public void generateCode(ICodeGenVisitor visitor) {
        for(ConstructorSymbol cs : constructors.values()) cs.accept(visitor);
    }

    @Override
    public boolean shouldGenCode() {
        return true;
    }

    @Override
    public void computeFieldOffsets() {
        fieldMap.get(JoosNonTerminal.LENGTH).setOffset(2 * SizeHelper.DEFAULT_STACK_SIZE);
    }

    @Override
    public long getObjectSize() {
        return SizeHelper.DEFAULT_STACK_SIZE;
    }

    @Override
    public Iterable<DclSymbol> getUninheritedStaticFields() {
        return Collections.emptySet();
    }

    @Override
    public Iterable<DclSymbol> getUninheritedNonStaticFields() {
        return fieldMap.values();
    }
}
