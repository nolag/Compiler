package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class TypeSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ANonTerminal from) throws UnsupportedException {
        if(!from.name.equalsIgnoreCase("Type")) return from;
        Terminal first = (Terminal)from.firstOrDefault("Id");
        if(first == null) return from.firstOrDefault("BasicType");
        ANonTerminal rest = (ANonTerminal)from.firstOrDefault("N_dot_Id_0");

        String fullName = first.token.lexeme;

        if(null != rest){
            for(ISymbol child : rest.children){
                Terminal nextPart = (Terminal)child;
                fullName += "." + nextPart.token.lexeme;
            }
        }

        ANonTerminal arrays =  (ANonTerminal) from.firstOrDefault("BracketsOpt");
        int arrayDimention = arrays == null ? 0 : arrays.children.size();
        return new TypeSymbol(fullName, arrayDimention);
    }

}
