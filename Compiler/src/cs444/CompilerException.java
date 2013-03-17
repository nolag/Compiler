package cs444;

public class CompilerException extends Exception {
    private static final long serialVersionUID = 1L;

    public CompilerException(String msg){
        super(msg);
    }

    public CompilerException(String className,
            String methodName, String message) {
        super("In " + className + "." + methodName + ", " + message);
    }
}
