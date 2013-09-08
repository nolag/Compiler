package cs444.parser.symbols.ast;

import java.util.HashMap;
import java.util.Map;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.APkgClassResolver;


public class DclSymbol extends AModifiersOptSymbol{
    public final boolean isLocal;
    private final Map<Platform<?, ?>, Long> offsetsMap = new HashMap<>();

    public static DclSymbol getClassSymbol(final String fullName, final APkgClassResolver resolver){
        DclSymbol retVal = null;
        try{
            retVal = new DclSymbol(fullName, null, new TypeSymbol(fullName, false, true), null, false);
            retVal.type.setTypeDclNode(resolver);
        }catch (final Exception e){ }
        return retVal;
    }

    public DclSymbol(final String dclName, final ANonTerminal from, final TypeSymbol type,
            final ANonTerminal initVal, final boolean isLocal) throws IllegalModifierException, UnsupportedException {

        this(dclName, from, type, isLocal);
        if(initVal != null) children.addAll(initVal.children);
    }

    public DclSymbol(final String dclName, final ANonTerminal from, final TypeSymbol type, final boolean isLocal)
            throws IllegalModifierException, UnsupportedException {
        super("Dcl", dclName, from, type);
        this.isLocal = isLocal;
    }

    public void setOffset(final long offset, final Platform<?, ?> platform) {
        offsetsMap.put(platform, offset);
    }

    public long getOffset(final Platform<?, ?> platform) {
        final Long l = offsetsMap.get(platform);
        return null != l ? l : 0;
    }

    @Override
    public void validate() throws UnsupportedException {
        final ImplementationLevel lvl = getImplementationLevel();
        if(lvl == ImplementationLevel.ABSTRACT )throw new UnsupportedException("abstract fields");

        if(lvl == ImplementationLevel.FINAL ) throw new UnsupportedException("final fields");

        if(type.equals("void"))throw new UnsupportedException("void fields");

        if (this.isLocal && children.isEmpty()) throw new UnsupportedException("omitted local initializer");
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
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        this.type.accept(visitor);

        for (final ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    @Override
    public void accept(final CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
