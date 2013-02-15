package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NameSymbol;
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

                NameSymbol name = methodHeader.getName();
                MethodSymbol method = new MethodSymbol(name.value, methDeclaration, methodHeader.getType(), methodBody);
                method.validate();

                parent.children.add(method);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }
}
