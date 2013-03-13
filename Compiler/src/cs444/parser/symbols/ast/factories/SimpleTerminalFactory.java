package cs444.parser.symbols.ast.factories;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.NullSymbol;
import cs444.parser.symbols.ast.SuperSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class SimpleTerminalFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        String value = from.getName();
        if(value.equals(JoosNonTerminal.NULL)) return new NullSymbol();
        if(value ==  JoosNonTerminal.TRUE_VALUE || value ==  JoosNonTerminal.FALSE_VALUE)  return new BooleanLiteralSymbol(value);
        if(value == JoosNonTerminal.THIS) return new ThisSymbol();
        if(value == JoosNonTerminal.SUPER) return new SuperSymbol();
        return from;
    }
}
