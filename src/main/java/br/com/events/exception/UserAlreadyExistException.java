package br.com.events.exception;

public class UserAlreadyExistException extends Exception {

	private static final long serialVersionUID = 9007119794318525990L;

	public UserAlreadyExistException() {
		super();
	}

	public UserAlreadyExistException(String message) {
		super(message);
	}

	public UserAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
