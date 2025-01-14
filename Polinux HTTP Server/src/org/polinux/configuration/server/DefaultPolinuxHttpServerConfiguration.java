package org.polinux.configuration.server;

public class DefaultPolinuxHttpServerConfiguration extends PolinuxHttpServerConfiguration {
	public DefaultPolinuxHttpServerConfiguration() {
		super(DefaultPolinuxHttpServerConfiguration.class
				.getResourceAsStream("/" + CONFIGURATION_PATH.replace("\\", "/")));

	}

}
