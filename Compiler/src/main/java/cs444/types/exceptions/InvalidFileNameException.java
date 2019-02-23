package cs444.types.exceptions;

import cs444.CompilerException;

public class InvalidFileNameException extends CompilerException {
	private static final long serialVersionUID = 1L;
	
	public InvalidFileNameException(String className, String fileName) {
		super("Class" + className + " was declared in file named " + fileName);
	}
}
