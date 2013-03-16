package cs444.types.exceptions;

import cs444.CompilerException;

public class ExplicitThisInStaticException extends CompilerException {

	private static final long serialVersionUID = 1L;

	public ExplicitThisInStaticException(String className, String methodName) {
		super("Explicit use of 'this' in class " + className + " method " + methodName);
	}
}
