package cs444.parser.symbols.ast;

import java.util.HashMap;
import java.util.Map;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.types.APkgClassResolver;

public class TypeSymbol extends ATerminal implements Typeable{
    public boolean isArray;
    public final boolean isClass;
    private APkgClassResolver typeResolver;

    private static final Map<String, TypeSymbol> builtIn = new HashMap<String, TypeSymbol>();

    public TypeSymbol(String value, boolean isArray, boolean isClass) {
        super("Type", value);
        this.isArray = isArray;
        this.isClass = isClass;
    }

    public APkgClassResolver getTypeDclNode() {
        return typeResolver;
    }

    public void setTypeDclNode(APkgClassResolver typeDclNode) {
        this.typeResolver = typeDclNode;
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
    }

    @Override
    public TypeSymbol getType() {
        return this;
    }

    @Override
    public void setType(TypeSymbol type) { }

    public static TypeSymbol getPrimative(String name){
        TypeSymbol retVal = builtIn.get(name);
        if(null == retVal){
            retVal = new TypeSymbol(name, false, false);
            builtIn.put(name, retVal);
        }
        return retVal;
    }

    public TypeSymbol getNonClassVersion(){
        TypeSymbol newType = new TypeSymbol(value, isArray, false);
        newType.typeResolver = typeResolver;
        return newType;
    }
}
