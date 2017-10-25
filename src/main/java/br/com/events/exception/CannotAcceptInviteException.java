package br.com.events.exception;

public class CannotAcceptInviteException extends Exception {

	private static final long serialVersionUID = 9007119794318525990L;

	public CannotAcceptInviteException() {
		super();
	}

	public CannotAcceptInviteException(String message) {
		super(message);
	}

	public CannotAcceptInviteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotAcceptInviteException(Throwable cause) {
		super(cause);
	}

}
