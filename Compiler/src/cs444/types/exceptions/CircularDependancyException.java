package cs444.types.exceptions;

public class CircularDependancyException extends Exception{
    private static final long serialVersionUID = 1L;

    public CircularDependancyException(String name){
        super("Circular reference with " + name);
    }
}
