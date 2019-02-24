package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.MethodHeader;
import cs444.parser.symbols.ast.MethodHeaderFactory;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.LinkedList;
import java.util.List;

public class MethodSymbolFactory extends ASTSymbolFactory {

    private static ANonTerminal getModifiersParent(ANonTerminal methDeclaration) {
        return (ANonTerminal) methDeclaration.firstOrDefault("MethodHeader");
    }

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if (from instanceof ClassSymbol) {
            ClassSymbol parent = (ClassSymbol) from;

            List<ISymbol> remove = new LinkedList<ISymbol>();

            for (ISymbol child : parent.getAll("MethodDeclaration")) {
                remove.add(child);
                ANonTerminal methDeclaration = (ANonTerminal) child;

                MethodHeader methodHeader =
                        MethodHeaderFactory.buildForRegularMethod((ANonTerminal) methDeclaration.firstOrDefault(
                                "MethodHeader"));
                ANonTerminal methodBody = (ANonTerminal) methDeclaration.firstOrDefault("MethodBody");

                MethodSymbol method = new MethodSymbol(methodHeader, getModifiersParent(methDeclaration),
                        methodHeader.type, methodBody, parent);
                method.validate();

                parent.children.add(method);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }
}
