package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class FieldAccessSymbolFactory extends ASTSymbolFactory {

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if (!from.getName().equalsIgnoreCase("FIELDACCESS")) {
            return from;
        }
        ANonTerminal nonTerm = (ANonTerminal) from;
        FieldAccessSymbol access = new FieldAccessSymbol(nonTerm.children.get(0), nonTerm.children.get(1));
        return access;
    }
}