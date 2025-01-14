package org.polinux.exceptions.polinux;

/**
 * Represents an {@link Exception} on the {@code Polinux API}
 */
public class PolinuxException extends Exception {
	private static final long serialVersionUID = 3653252366926181731L;

	public PolinuxException() {
		super();
	}

	public PolinuxException(String message) {
		super(message);
	}

	public PolinuxException(Throwable cause) {
		super(cause);
	}

	public PolinuxException(String message, Throwable cause) {
		super(message, cause);
	}

	public PolinuxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
