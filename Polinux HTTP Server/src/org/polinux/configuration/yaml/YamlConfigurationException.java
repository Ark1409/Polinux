package org.polinux.configuration.yaml;

import org.polinux.configuration.ConfigurationException;

public class YamlConfigurationException extends ConfigurationException {

	private static final long serialVersionUID = -7674140997985785003L;

	public YamlConfigurationException(YamlConfiguration y) {
		this("", y);
	}

	public YamlConfigurationException(String message, YamlConfiguration y) {
		super(message, y);
		
	}

	public YamlConfigurationException(Throwable cause, YamlConfiguration y) {
		super(cause, y);
		
	}

	public YamlConfigurationException(String message, Throwable cause, YamlConfiguration y) {
		super(message, cause, y);
	}

	public YamlConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
			YamlConfiguration y) {
		super(message, cause, enableSuppression, writableStackTrace, y);
		
	}

	public YamlConfiguration getYamlConfiguration() {
		return (YamlConfiguration) this.c;
	}

}
