package org.polinux.exceptions.polinux.commands;

import org.polinux.exceptions.polinux.PolinuxRuntimeException;

public class PolinuxCommandRuntimeException extends PolinuxRuntimeException {
	private static final long serialVersionUID = -3502954824126222559L;

	public PolinuxCommandRuntimeException() {
		super();
	}

	public PolinuxCommandRuntimeException(String message) {
		super(message);
	}

	public PolinuxCommandRuntimeException(Throwable cause) {
		super(cause);
	}

	public PolinuxCommandRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	protected PolinuxCommandRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
