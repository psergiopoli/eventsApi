package br.com.events.exception;

public class InvalidEndOrStartDayOfEventException extends Exception {

	private static final long serialVersionUID = 9007119794318525990L;

	public InvalidEndOrStartDayOfEventException() {
		super();
	}

	public InvalidEndOrStartDayOfEventException(String message) {
		super(message);
	}

	public InvalidEndOrStartDayOfEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEndOrStartDayOfEventException(Throwable cause) {
		super(cause);
	}

}
