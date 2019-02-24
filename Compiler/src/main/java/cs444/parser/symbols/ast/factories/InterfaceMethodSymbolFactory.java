package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.InterfaceMethodSymbol;
import cs444.parser.symbols.ast.InterfaceSymbol;
import cs444.parser.symbols.ast.MethodHeader;
import cs444.parser.symbols.ast.MethodHeaderFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.LinkedList;
import java.util.List;

public class InterfaceMethodSymbolFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if (from instanceof InterfaceSymbol) {
            InterfaceSymbol parent = (InterfaceSymbol) from;

            List<ISymbol> remove = new LinkedList<ISymbol>();

            for (ISymbol child : parent.getAll("AbstractMethodDeclaration")) {
                remove.add(child);
                ANonTerminal intMethDecl = (ANonTerminal) child;

                MethodHeader methodHeader =
                        MethodHeaderFactory.buildForRegularMethod((ANonTerminal) intMethDecl.firstOrDefault(
                                "MethodHeader"));
                InterfaceMethodSymbol method = new InterfaceMethodSymbol(methodHeader, intMethDecl);
                method.validate();

                parent.children.add(method);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }
}
