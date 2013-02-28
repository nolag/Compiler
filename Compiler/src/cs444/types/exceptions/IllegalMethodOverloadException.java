package cs444.types.exceptions;

import cs444.CompilerException;

public class IllegalMethodOverloadException extends CompilerException{
    private static final long serialVersionUID = 1L;

    public IllegalMethodOverloadException(String where, String what, String why){
        super("Method " + what + " in " + where + " " + why);
    }
}
