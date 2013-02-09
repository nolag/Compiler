package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.exceptions.UnsupportedException;


public class ClassSymbol extends AInterfaceOrClass{
    public final String className;
    public final String superClass;
    public final Iterable<String> impls;

    public ClassSymbol(String className, String superClass, Iterable<String> impls, ANonTerminal body) {
        super("ClassDeclaration");
        this.className = className;
        this.superClass = superClass;
        this.impls = impls;
        children.add(body);
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
}
