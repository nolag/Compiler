package cs444.types.exceptions;

public class IllegalMethodOverloadException extends Exception{
    private static final long serialVersionUID = 1L;

    public IllegalMethodOverloadException(String where, String what, String why){
        super("Method " + what + " in " + where + " " + why);
    }
}
