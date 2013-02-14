package cs444.parser.symbols.ast.factories;

import java.util.LinkedList;
import java.util.List;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ClassSymbol;
import cs444.parser.symbols.ast.FieldSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class FieldSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws UnsupportedException, IllegalModifierException {
        if(ClassSymbol.class.isInstance(from)){
            ClassSymbol parent = (ClassSymbol) from;

            List<ISymbol> remove = new LinkedList<ISymbol>();

            for(ISymbol child : parent.getAll("FieldDeclaration")){
                remove.add(child);
                ANonTerminal nonTerm = (ANonTerminal) child;
                TypeSymbol type = (TypeSymbol) nonTerm.firstOrDefault("Type");
                ANonTerminal vardecl = (ANonTerminal) nonTerm.firstOrDefault("VariableDeclarator");
                NameSymbol name = (NameSymbol) vardecl.firstOrDefault("Name");
                ANonTerminal initVal = (ANonTerminal) vardecl.firstOrDefault("VariableInitializer");

                boolean isArray = nonTerm.firstOrDefault("lbracket") == null ? false : true;

                //This only works because there is one dcl per line, otherwise need to copy the type symbol
                if(type.isArray && isArray) throw new UnsupportedException("Arrays of arrays");
                if(isArray) type.isArray = true;

                FieldSymbol field =  new FieldSymbol(name.value, nonTerm, type, initVal);

                parent.children.add(field);
            }

            parent.children.removeAll(remove);
        }
        return from;
    }
}
