package cs444.parser.symbols.ast;

import java.util.Collections;
import java.util.List;

import cs444.codegen.ISymbolChoiceVisitor;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;


public class EmptyClassSymbol extends AInterfaceOrClassSymbol{
    private static List<String> empty = Collections.emptyList();
    private static List<ISymbol> body = Collections.emptyList();

    public EmptyClassSymbol(ANonTerminal from, Iterable<NameSymbol> pkgImports)
            throws IllegalModifierException, UnsupportedException {
        super("EmptyClass", "", from, empty, body, null, pkgImports);
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public ProtectionLevel defaultProtectionLevel() {
        return ProtectionLevel.PUBLIC;
    }

    @Override
    public ImplementationLevel defaultImplementationLevel() {
        return ImplementationLevel.NORMAL;
    }

    @Override
    public void accept(ISymbolChoiceVisitor visitor) {
        visitor.visit(this);
    }

}
