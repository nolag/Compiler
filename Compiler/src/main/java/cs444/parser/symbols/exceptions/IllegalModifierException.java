package cs444.parser.symbols.exceptions;

import cs444.CompilerException;

public class IllegalModifierException extends CompilerException {
    private static final long serialVersionUID = 1L;

    public IllegalModifierException(String is, String isAlso) {
        super("It is illegal to declare something as both " + is + " and " + isAlso);
    }

    public IllegalModifierException(String is) {
        super("JOOS 1W does not support " + is);
    }
}
