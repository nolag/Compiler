package cs444.parser.symbols.factories;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.NonTerminal;

public class JoosNonTerminalFactory extends NonTerminalFactory {

    public JoosNonTerminalFactory(String name) {
        super(name);
    }

    @Override
    public NonTerminal create(ISymbol[] children) {
        return new JoosNonTerminal(name, children);
    }
}
