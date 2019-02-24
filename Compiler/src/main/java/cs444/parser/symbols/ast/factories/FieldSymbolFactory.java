package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

import java.util.LinkedList;
import java.util.List;

public class FieldSymbolFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if (from instanceof ClassSymbol) {
            ClassSymbol parent = (ClassSymbol) from;

            List<ISymbol> remove = new LinkedList<ISymbol>();

            for (ISymbol child : parent.getAll("FieldDeclaration")) {
                remove.add(child);
                ANonTerminal nonTerm = (ANonTerminal) child;
                TypeSymbol type = (TypeSymbol) nonTerm.firstOrDefault("Type");
                ANonTerminal vardecl = (ANonTerminal) nonTerm.firstOrDefault("VariableDeclarator");
                NameSymbol name = (NameSymbol) vardecl.firstOrDefault("Name");
                ANonTerminal initVal = (ANonTerminal) vardecl.firstOrDefault("VariableInitializer");

                boolean isArray = nonTerm.firstOrDefault("lbracket") != null;

                //This only works because there is one dcl per line, otherwise need to copy the type symbol
                if (type.isArray && isArray) {
                    throw new UnsupportedException("Arrays of arrays");
                }
                if (isArray) {
                    type.isArray = true;
                }

                DclSymbol field = new DclSymbol(name.value, nonTerm, type, initVal, false);

                field.validate();
                parent.children.add(field);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }
}
