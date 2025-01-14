package org.polinux.configuration.server;

public class DefaultPolinuxHttpsServerConfiguration extends PolinuxHttpsServerConfiguration {

	public DefaultPolinuxHttpsServerConfiguration() {
		super(DefaultPolinuxHttpsServerConfiguration.class
				.getResourceAsStream("/" + CONFIGURATION_PATH.replace("\\", "/")));
	}

}
