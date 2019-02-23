package cs444.parser.symbols.ast;

import java.util.List;

import cs444.codegen.CodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class InterfaceSymbol extends AInterfaceOrClassSymbol{

    public InterfaceSymbol(String interfaceName, ANonTerminal from, Iterable<String> impls, List<ISymbol> body, Iterable<NameSymbol> pkgImports)
            throws IllegalModifierException, UnsupportedException {
        super("InterfaceDeclaration", interfaceName, from, impls, body, null, pkgImports);
    }

    @Override
    public void validate() throws UnsupportedException {
        switch(getProtectionLevel()){
        case PRIVATE:
            throw new UnsupportedException("private interfaces");
        case NOT_VALID:
            throw new UnsupportedException("package declared interfaces");
        default:
            break;
        }
        //Since we don't have nested classes, the class can't be static
        if(isStatic()) throw new UnsupportedException("static interfacse");
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        //We don't support package private interfaces.
        return ProtectionLevel.NOT_VALID;
    }

    @Override
    public ImplementationLevel defaultImplementationLevel() {
        return ImplementationLevel.ABSTRACT;
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public void accept(CodeGenVisitor<?, ?> visitor) {
        visitor.visit(this);
    }
}
