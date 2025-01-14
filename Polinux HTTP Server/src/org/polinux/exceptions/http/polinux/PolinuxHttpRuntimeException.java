package org.polinux.exceptions.http.polinux;

import org.polinux.exceptions.http.HttpRuntimeException;

public class PolinuxHttpRuntimeException extends HttpRuntimeException {


	private static final long serialVersionUID = 868470847407704499L;

	public PolinuxHttpRuntimeException() {
		super();
	}

	public PolinuxHttpRuntimeException(String message) {
		super(message);
	}

	public PolinuxHttpRuntimeException(Throwable cause) {
		super(cause);
	}

	public PolinuxHttpRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	protected PolinuxHttpRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
