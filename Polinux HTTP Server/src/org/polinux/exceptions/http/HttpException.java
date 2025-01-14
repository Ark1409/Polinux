package org.polinux.exceptions.http;

import org.polinux.exceptions.polinux.PolinuxException;

public class HttpException extends PolinuxException {
	private static final long serialVersionUID = 8947306786360688299L;

	public HttpException() {
		super();
	}

	public HttpException(String message) {
		super(message);
	}

	public HttpException(Throwable cause) {
		super(cause);
	}

	public HttpException(String message, Throwable cause) {
		super(message, cause);
	}

	protected HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
