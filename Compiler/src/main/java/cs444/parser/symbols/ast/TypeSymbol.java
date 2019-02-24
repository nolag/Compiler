package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ATerminal;
import cs444.types.APkgClassResolver;

import java.util.HashMap;
import java.util.Map;

public class TypeSymbol extends ATerminal implements Typeable {
    private static final Map<String, TypeSymbol> builtIn = new HashMap<String, TypeSymbol>();
    public final boolean isClass;
    public boolean isArray;
    public boolean isFinal;
    private APkgClassResolver typeResolver;

    public TypeSymbol(String value, boolean isArray, boolean isClass) {
        super("Type", value);
        this.isArray = isArray;
        this.isClass = isClass;
    }

    public static TypeSymbol getPrimative(String name) {
        TypeSymbol retVal = builtIn.get(name);
        if (null == retVal) {
            retVal = new TypeSymbol(name, false, false);
            builtIn.put(name, retVal);
        }
        return retVal;
    }

    public APkgClassResolver getTypeDclNode() {
        return typeResolver;
    }

    public void setTypeDclNode(APkgClassResolver typeDclNode) {
        typeResolver = typeDclNode;
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

    public TypeSymbol getNonClassVersion() {
        TypeSymbol newType = new TypeSymbol(value, isArray, false);
        newType.typeResolver = typeResolver;
        return newType;
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
