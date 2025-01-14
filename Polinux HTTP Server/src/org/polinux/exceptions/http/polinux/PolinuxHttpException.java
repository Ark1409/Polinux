package org.polinux.exceptions.http.polinux;

import org.polinux.exceptions.http.HttpException;

public class PolinuxHttpException extends HttpException {

	private static final long serialVersionUID = -8387719438976601356L;

	public PolinuxHttpException() {
		super();
	}

	public PolinuxHttpException(String message) {
		super(message);
	}

	public PolinuxHttpException(Throwable cause) {
		super(cause);
	}

	public PolinuxHttpException(String message, Throwable cause) {
		super(message, cause);
	}

	protected PolinuxHttpException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
