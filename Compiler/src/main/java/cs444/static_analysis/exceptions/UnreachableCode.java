package cs444.static_analysis.exceptions;

import cs444.CompilerException;

public class UnreachableCode extends CompilerException {
    private static final long serialVersionUID = 1L;

    public UnreachableCode(String code, String className, String methodName) {
        super("In " + className + " method '" + methodName +
                "' unreachable code '" + code + "'");
    }

}
