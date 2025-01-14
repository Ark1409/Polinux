package org.polinux.configuration;

public class ConfigurationException extends Exception {

	private static final long serialVersionUID = -4603265993387990659L;
	
	/**
	 * {@link org.polinux.configuration.Configuration} linked to this exception.
	 */
	protected Configuration c;

	public ConfigurationException(Configuration c) {
		this("", c);
	}

	public ConfigurationException(String message, Configuration c) {
		super(message);
		this.c = c;
	}

	public ConfigurationException(Throwable cause, Configuration c) {
		super(cause);
		this.c = c;
	}

	public ConfigurationException(String message, Throwable cause, Configuration c) {
		super(message, cause);
		this.c = c;
	}

	protected ConfigurationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, Configuration c) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.c = c;
	}

	public Configuration getConfiguration() {
		return this.c;
	}

}
