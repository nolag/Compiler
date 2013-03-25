package cs444.parser.symbols.ast;

import java.util.List;

import cs444.codegen.ICodeGenVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ClassSymbol extends AInterfaceOrClassSymbol{


    public ClassSymbol(String className, ANonTerminal from, Iterable<String> impls, List<ISymbol> body,
            String superName, Iterable<NameSymbol> pkgImports) throws IllegalModifierException, UnsupportedException {
        super("ClassDeclaration", className, from, impls, body, superName, pkgImports);
    }

    @Override
    public void validate() throws UnsupportedException {
        switch(getProtectionLevel()){
        case PRIVATE:
            throw new UnsupportedException("private classes");
        case NOT_VALID:
            throw new UnsupportedException("package declared classes");
        default:
            break;
        }
        if(isNative())throw new UnsupportedException("native constructors");
        //Since we don't have nested classes, the class can't be static
        if(isStatic()) throw new UnsupportedException("static classes");
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        //We don't support package protected classes.
        return ProtectionLevel.NOT_VALID;
    }

    @Override
    public ImplementationLevel defaultImplementationLevel() {
        return ImplementationLevel.NORMAL;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public void accept(ICodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
