package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.MethodHeader;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class MethodSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if(ClassSymbol.class.isInstance(from)){
            ClassSymbol parent = (ClassSymbol) from;

            List<ISymbol> remove = new LinkedList<ISymbol>();

            for(ISymbol child : parent.getAll("MethodDeclaration")){
                remove.add(child);
                ANonTerminal methDeclaration = (ANonTerminal) child;

                MethodHeader methodHeader = new MethodHeader((ANonTerminal) methDeclaration.firstOrDefault("MethodHeader"));
                ANonTerminal methodBody = (ANonTerminal) methDeclaration.firstOrDefault("MethodBody");

                MethodSymbol method = new MethodSymbol(methodHeader.name.value, getModifiersParent(methDeclaration), methodHeader.type, 
                        methodBody, methodHeader.dcls);
                method.validate();

                parent.children.add(method);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }

    private static ANonTerminal getModifiersParent(ANonTerminal methDeclaration) {
        return (ANonTerminal)methDeclaration.firstOrDefault("MethodHeader");
    }
}
