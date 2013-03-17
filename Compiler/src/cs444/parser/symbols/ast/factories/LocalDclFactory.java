package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class LocalDclFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if(!from.getName().equalsIgnoreCase("LocalVariableDeclaration")) return from;
        ANonTerminal dcl = (ANonTerminal) from;
        TypeSymbol type = (TypeSymbol) dcl.firstOrDefault("Type");
        ANonTerminal vardecl = (ANonTerminal) dcl.firstOrDefault("VariableDeclarator");
        NameSymbol name = (NameSymbol) vardecl.firstOrDefault("Name");
        ANonTerminal initVal = (ANonTerminal) vardecl.firstOrDefault("VariableInitializer");

        boolean isArray = dcl.firstOrDefault("lbracket") == null ? false : true;

        //This only works because there is one dcl per line, otherwise need to copy the type symbol
        if(type.isArray && isArray) throw new UnsupportedException("Arrays of arrays");
        if(isArray) type.isArray = true;

        DclSymbol localDcl =  new DclSymbol(name.value, dcl, type, initVal, true);
        localDcl.validate();

        return localDcl;
    }

}
