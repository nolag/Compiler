package cs444.parser.symbols.exceptions;

public class IllegalModifierException extends Exception{
    private static final long serialVersionUID = 1L;

    public IllegalModifierException(String is, String isAlso){
        super("It is illegal to declare something as both " + is + " and " + isAlso);
    }

    public IllegalModifierException(String is){
        super("JOOS 1W does not support " + is);
    }
}
