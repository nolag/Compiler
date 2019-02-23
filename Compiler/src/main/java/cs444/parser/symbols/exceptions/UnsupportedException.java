package cs444.parser.symbols.exceptions;

import cs444.CompilerException;

public class UnsupportedException extends CompilerException{
    private static final long serialVersionUID = 1L;

    public UnsupportedException(String what){
        super(what + " is not supported in JOOS!");
    }
}
