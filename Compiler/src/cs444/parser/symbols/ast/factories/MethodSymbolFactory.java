package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
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
                ANonTerminal methodHeader = (ANonTerminal) methDeclaration.firstOrDefault("MethodHeader");

                TypeSymbol type = (TypeSymbol) methodHeader.firstOrDefault("Type");

                // TODO: if type is null, method may used a void. check for that.
                // TODO: also fetch abstract methods

                NameSymbol name = getName(methodHeader);

                // TODO: add rest of tokens to body;
                ANonTerminal methodBody = null; // = (ANonTerminal) methDeclaration.firstOrDefault("MethodBody");

                MethodSymbol method =  new MethodSymbol(name.value, methDeclaration, type, methodBody);

                parent.children.add(method);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }

    private NameSymbol getName(ANonTerminal methodHeader) {
        NameSymbol name = (NameSymbol)methodHeader.firstOrDefault("Name");
        if(name == null){ // find it?
            // not found because methodDecl was not collapsed
            ANonTerminal methodDecl = (ANonTerminal)methodHeader.firstOrDefault("MethodDeclarator");
            name = (NameSymbol) methodDecl.firstOrDefault("Name");
        }
        return name;
    }
}
