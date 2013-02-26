package cs444.types;

public class DuplicateDeclearationException extends Exception{
    private static final long serialVersionUID = 1L;

    public DuplicateDeclearationException(String symbol, String in){
        super("Duplicate symbol " + symbol + " in " + in);
    }
}
