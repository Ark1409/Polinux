package org.polinux.exceptions.polinux.commands;

import org.polinux.exceptions.polinux.PolinuxException;

public class PolinuxCommandException extends PolinuxException {

	private static final long serialVersionUID = 5984525601245239069L;

	public PolinuxCommandException() {
		super();
	}

	public PolinuxCommandException(String message) {
		super(message);
	}

	public PolinuxCommandException(Throwable cause) {
		super(cause);
	}

	public PolinuxCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	protected PolinuxCommandException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
