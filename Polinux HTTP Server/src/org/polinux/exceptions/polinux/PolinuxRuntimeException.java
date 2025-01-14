package org.polinux.exceptions.polinux;

/**
 * Represents a {@link RuntimeException} on the {@code Polinux API}
 */
public class PolinuxRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -276351665515912922L;

	public PolinuxRuntimeException() {
		super();
	}

	public PolinuxRuntimeException(String message) {
		super(message);
	}

	public PolinuxRuntimeException(Throwable cause) {
		super(cause);
	}

	public PolinuxRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	protected PolinuxRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
