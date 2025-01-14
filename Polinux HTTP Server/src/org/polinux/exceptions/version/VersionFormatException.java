package org.polinux.exceptions.version;

import org.polinux.exceptions.polinux.PolinuxRuntimeException;

/**
 * The {@link org.polinux.exceptions.polinux.PolinuxRuntimeException
 * PolinuxRuntimeException} thrown whenever a formatting error occurs
 * while change a {@link net.plugin.plugin.version.Version version's} string
 * value.
 */
public class VersionFormatException extends PolinuxRuntimeException {

	private static final long serialVersionUID = 1L;

	public VersionFormatException() {
		super();
	}

	public VersionFormatException(String message) {
		super(message);
	}

	public VersionFormatException(Throwable cause) {
		super(cause);
	}

	public VersionFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	protected VersionFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
