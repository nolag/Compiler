package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.LinkedList;
import java.util.List;

public class ConstructorSymbolFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if (from instanceof ClassSymbol) {
            ClassSymbol parent = (ClassSymbol) from;

            List<ISymbol> remove = new LinkedList<ISymbol>();

            Iterable<ISymbol> constructors = parent.getAll("ConstructorDeclaration");

            if (!constructors.iterator().hasNext()) {
                throw new UnsupportedException("classes with no explicit constructor");
            }

            for (ISymbol symbol : constructors) {
                remove.add(symbol);
                ANonTerminal constructorDcl = (ANonTerminal) symbol;
                MethodHeader header =
                        MethodHeaderFactory.buildForConstructor((ANonTerminal) constructorDcl.firstOrDefault(
                                "ConstructorDeclarator"));
                ANonTerminal body = (ANonTerminal) constructorDcl.firstOrDefault("ConstructorBody");

                if (!header.name.value.equals(parent.dclName)) {
                    throw new UnsupportedException("constructors with a different name than their parent");
                }

                MethodOrConstructorSymbol constructor = new ConstructorSymbol(header, constructorDcl, body);
                constructor.validate();
                parent.children.add(constructor);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }
}
