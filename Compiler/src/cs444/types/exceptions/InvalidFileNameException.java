package cs444.types.exceptions;

public class InvalidFileNameException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidFileNameException(String className, String fileName) {
		super("Class" + className + " was declared in file named " + fileName);
	}
}
