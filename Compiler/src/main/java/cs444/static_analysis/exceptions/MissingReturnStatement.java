package cs444.static_analysis.exceptions;

import cs444.CompilerException;

public class MissingReturnStatement extends CompilerException {
    private static final long serialVersionUID = 1L;

    public MissingReturnStatement(String className, String methodName) {
        super("In " + className + " method '" + methodName +
                "' is missing return statement");
    }
}
