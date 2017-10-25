package br.com.events.exception;

public class InviteAlreadySendException extends Exception {

	private static final long serialVersionUID = 9007119794318525990L;

	public InviteAlreadySendException() {
		super();
	}

	public InviteAlreadySendException(String message) {
		super(message);
	}

	public InviteAlreadySendException(String message, Throwable cause) {
		super(message, cause);
	}

	public InviteAlreadySendException(Throwable cause) {
		super(cause);
	}

}
