package tn.esprit.autoworld.vehiclesales.exception;

/** Mapped to HTTP 409: the request is valid but not allowed in the current state. */
public class ConflictException extends RuntimeException {

	public ConflictException(String message) {
		super(message);
	}
}
