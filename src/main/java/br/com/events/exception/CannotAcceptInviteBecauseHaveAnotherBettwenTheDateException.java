package br.com.events.exception;

public class CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException extends Exception {

	private static final long serialVersionUID = 9007119794318525990L;

	public CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException() {
		super();
	}

	public CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException(String message) {
		super(message);
	}

	public CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotAcceptInviteBecauseHaveAnotherBettwenTheDateException(Throwable cause) {
		super(cause);
	}

}
