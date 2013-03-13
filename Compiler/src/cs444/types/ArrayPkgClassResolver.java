package cs444.types;

import java.util.Set;

import cs444.CompilerException;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.exceptions.UndeclaredException;

public class ArrayPkgClassResolver extends APkgClassResolver {
    private final APkgClassResolver resolver;

    public static String getArrayName(String name){
        return name + "[]";
    }

    protected ArrayPkgClassResolver(APkgClassResolver resolver) {
        super(getArrayName(resolver.fullName), resolver.pkg, true);
        this.resolver = resolver;
        build(null, false, false);
        for(String s : resolver.assignableTo){
            assignableTo.add(getArrayName(s));
        }

        PkgClassInfo.instance.symbolMap.put(fullName, this);
    }

    @Override
    public APkgClassResolver getClass(String name, boolean die) throws UndeclaredException {
        return resolver.getClass(name, die);
    }

    @Override
    public APkgClassResolver getSuper() throws UndeclaredException {
        throw new UndeclaredException("super", "Arrays");
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
        TypeSymbol intType = new TypeSymbol("int", false, false);
        try{
            DclSymbol length = new DclSymbol("length", null, intType, null, true);
            fieldMap.put("length", length);
        }catch (CompilerException ce){
            ce.printStackTrace();
        }
        try{
            PkgClassResolver obj = (PkgClassResolver) getClass(OBJECT, true);
            for(AMethodSymbol m : obj.start.getMethods()){
                String uniqueName;
                uniqueName = generateUniqueName(m, m.dclName);
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
}
