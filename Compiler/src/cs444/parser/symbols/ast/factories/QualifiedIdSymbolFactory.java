package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.ast.ListedSymbol;
import cs444.parser.symbols.ast.QualifiedIdSymbol;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class QualifiedIdSymbolFactory extends ASTSymbolFactory{

    @Override
    protected ANonTerminal convert(ANonTerminal from) throws OutOfRangeException, UnsupportedException {
        if(!from.getName().toUpperCase().equals("QUALIFIEDIDENTIFIER")) return from;
        Terminal first = (Terminal)from.firstOrDefault("ID");
        ListedSymbol rest = (ListedSymbol) from.firstOrDefault("N_dot_Id_0");

        return new QualifiedIdSymbol(first, rest);
    }

}
