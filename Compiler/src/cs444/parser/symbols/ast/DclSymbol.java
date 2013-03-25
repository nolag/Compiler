package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.APkgClassResolver;


public class DclSymbol extends AModifiersOptSymbol{
    private final boolean isLocal;
    public boolean isFinal;
    private int offset;

    public static DclSymbol getClassSymbol(String fullName, APkgClassResolver resolver){
        DclSymbol retVal = null;
        try{
            retVal = new DclSymbol(fullName, null, new TypeSymbol(fullName, false, true), null, false);
            retVal.type.setTypeDclNode(resolver);
        }catch (Exception e){ }
        return retVal;
    }

    public DclSymbol(String dclName, ANonTerminal from, TypeSymbol type, ANonTerminal initVal, boolean isLocal)
            throws IllegalModifierException, UnsupportedException {
        this(dclName, from, type, isLocal);
        if(initVal != null) children.addAll(initVal.children);
    }

    public DclSymbol(String dclName, ANonTerminal from, TypeSymbol type, boolean isLocal, boolean isFinal)
            throws IllegalModifierException, UnsupportedException {
        this(dclName, from, type, isLocal);
        this.isFinal = isFinal;
    }

    public DclSymbol(String dclName, ANonTerminal from, TypeSymbol type, boolean isLocal)
            throws IllegalModifierException, UnsupportedException {
        super("Dcl", dclName, from, type);
        this.isLocal = isLocal;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }

    public int getOffset(){
        return offset;
    }

    @Override
    public void validate() throws UnsupportedException {
        ImplementationLevel lvl = getImplementationLevel();
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
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.open(this);

        this.type.accept(visitor);

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }

    @Override
    public void accept(ICodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
