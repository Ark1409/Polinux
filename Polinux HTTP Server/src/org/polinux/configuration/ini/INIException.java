package org.polinux.configuration.ini;

import org.polinux.configuration.ConfigurationException;

public class INIException extends ConfigurationException {
	private static final long serialVersionUID = 1L;

	private INIConfiguration c;

	public INIException(INIConfiguration c) {
		super(c);
	}

	public INIException(String message, INIConfiguration c) {
		super(message, c);
	}

	public INIException(Throwable cause, INIConfiguration c) {
		super(cause, c);
	}

	public INIException(String message, Throwable cause, INIConfiguration c) {
		super(message, cause, c);
	}

	public INIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
			INIConfiguration c) {
		super(message, cause, enableSuppression, writableStackTrace, c);
	}

	public INIConfiguration getINIConfiguration() {
		return this.c;
	}

}
