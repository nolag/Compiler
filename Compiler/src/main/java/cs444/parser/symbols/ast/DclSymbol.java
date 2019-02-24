package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.APkgClassResolver;

import java.util.HashMap;
import java.util.Map;

public class DclSymbol extends AModifiersOptSymbol {
    public final boolean isLocal;
    private final Map<Platform<?, ?>, Long> offsetsMap = new HashMap<>();

    public DclSymbol(String dclName, ANonTerminal from, TypeSymbol type,
                     ANonTerminal initVal, boolean isLocal) throws IllegalModifierException,
            UnsupportedException {

        this(dclName, from, type, isLocal);
        if (initVal != null) {
            children.addAll(initVal.children);
        }
    }

    public DclSymbol(String dclName, ANonTerminal from, TypeSymbol type, boolean isLocal)
            throws IllegalModifierException, UnsupportedException {
        super("Dcl", dclName, from, type);
        this.isLocal = isLocal;
    }

    public static DclSymbol getClassSymbol(String fullName, APkgClassResolver resolver) {
        DclSymbol retVal = null;
        try {
            retVal = new DclSymbol(fullName, null, new TypeSymbol(fullName, false, true), null, false);
            retVal.type.setTypeDclNode(resolver);
        } catch (Exception e) {
        }
        return retVal;
    }

    public void setOffset(long offset, Platform<?, ?> platform) {
        offsetsMap.put(platform, offset);
    }

    public long getOffset(Platform<?, ?> platform) {
        Long l = offsetsMap.get(platform);
        return null != l ? l : 0;
    }

    @Override
    public void validate() throws UnsupportedException {
        ImplementationLevel lvl = getImplementationLevel();
        if (lvl == ImplementationLevel.ABSTRACT) {
            throw new UnsupportedException("abstract fields");
        }

        if (lvl == ImplementationLevel.FINAL) {
            throw new UnsupportedException("final fields");
        }

        if (isLocal && children.isEmpty()) {
            throw new UnsupportedException("omitted local initializer");
        }
        super.validate();
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        // We don't support package private members
        return isLocal ? ProtectionLevel.PRIVATE : ProtectionLevel.NOT_VALID;
    }

    @Override
    public ImplementationLevel defaultImplementationLevel() {
        return ImplementationLevel.NORMAL;
    }

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        type.accept(visitor);

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
