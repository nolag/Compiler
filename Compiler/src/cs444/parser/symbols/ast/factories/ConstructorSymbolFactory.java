package cs444.parser.symbols.ast.factories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class ConstructorSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException{
        if(ClassSymbol.class.isInstance(from)){
            ClassSymbol parent = (ClassSymbol) from;

            List<ISymbol> remove = new LinkedList<ISymbol>();

            Iterable<ISymbol> constructors = parent.getAll("ConstructorDeclaration");

            if(!constructors.iterator().hasNext()) throw new UnsupportedException("classes with no explicit constructor");

            for(ISymbol symbol : constructors){
                remove.add(symbol);
                ANonTerminal constructorDcl = (ANonTerminal) symbol;
                ANonTerminal dcl = (ANonTerminal) constructorDcl.firstOrDefault("ConstructorDeclarator");
                ANonTerminal body = (ANonTerminal) constructorDcl.firstOrDefault("ConstructorBody");

                String name = ((NameSymbol)dcl.firstOrDefault("Name")).value;
                if(!name.equals(parent.dclName)) throw new UnsupportedException("construcotrs with a different name than their parent");

                ANonTerminal params = (ANonTerminal) constructorDcl.firstOrDefault("FormalParameterList");

                List<ISymbol> children = Collections.emptyList();
                if(null != body) children = body.children;

                Iterable<ISymbol> paramIter = Collections.emptyList();;
                if(null != params) paramIter = params.children;

                //ANonTerminal from, List<ISymbol> children, Iterable<ISymbol> args, Iterable<ISymbol> body)

                ConstructorSymbol constructor = new ConstructorSymbol(name, constructorDcl, children, paramIter);
                constructor.validate();
                parent.children.add(constructor);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }

}
