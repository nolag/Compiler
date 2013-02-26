package cs444.types;

public class UndeclaredException extends Exception{
    private static final long serialVersionUID = 1L;

    public UndeclaredException(String what, String in){
        super("In " + in + " " + what + " is undeclared.");
    }
}
