package org.polinux.exceptions.http;

import org.polinux.exceptions.polinux.PolinuxRuntimeException;

public class HttpRuntimeException extends PolinuxRuntimeException {
	private static final long serialVersionUID = -1767884091848244099L;

	public HttpRuntimeException() {
		super();
	}

	public HttpRuntimeException(String message) {
		super(message);
	}

	public HttpRuntimeException(Throwable cause) {
		super(cause);
	}

	public HttpRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	protected HttpRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
