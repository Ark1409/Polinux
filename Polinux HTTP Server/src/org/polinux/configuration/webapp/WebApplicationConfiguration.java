package org.polinux.configuration.webapp;

import java.util.List;

import org.polinux.configuration.yaml.YamlConfigurationSection;

public interface WebApplicationConfiguration {

	public abstract List<String> getWelcomeFiles();

	public abstract YamlConfigurationSection getServletConfigurationSection();

	public abstract String getServletClass(String servletName);

	public abstract String[] getServletUrlPatterns(String servletName);

	public abstract String getWebsiteRoot();
}
