package cs444.parser.symbols.ast;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ConstructorSymbol extends AModifiersOptSymbol{
    //TODO eventually make a class for arguments
    public final Iterable<ISymbol> args;

    public ConstructorSymbol(String dclName, ANonTerminal from, ANonTerminal body, Iterable<ISymbol> args)
            throws IllegalModifierException, UnsupportedException {

        super("ConstructorDeclaration", dclName, from, null);

        this.args = args;
        if(body != null) children.addAll(body.children);
    }

    @Override
    public void validate() throws UnsupportedException {
        if(getImplementationLevel() != ImplementationLevel.NORMAL) throw new UnsupportedException("Unimlemented and final constructors");
        if(isNative())throw new UnsupportedException("native constructors");

        super.validate();
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        return ProtectionLevel.NOT_VALID;
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

        for (ISymbol param : this.args) {
            param.accept(visitor);
        }

        for (ISymbol child : children) {
            child.accept(visitor);
        }

        visitor.close(this);
    }
}
