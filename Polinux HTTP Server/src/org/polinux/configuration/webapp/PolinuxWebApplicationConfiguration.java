package org.polinux.configuration.webapp;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.polinux.configuration.yaml.YamlConfiguration;
import org.polinux.configuration.yaml.YamlConfigurationSection;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

@SuppressWarnings({ "deprecation" })
public class PolinuxWebApplicationConfiguration extends YamlConfiguration implements WebApplicationConfiguration {
	public static final String CONFIGURATION_PATH = "app" + File.separator + "app.yml";

	public static final String DEFAULT_ROOT = "wwwroot";

	public PolinuxWebApplicationConfiguration(String path) {
		super(path);
	}

	public PolinuxWebApplicationConfiguration(String path, FlowStyle flowStyle) {
		super(path, flowStyle);
	}

	public PolinuxWebApplicationConfiguration(File f) {
		super(f);
	}

	public PolinuxWebApplicationConfiguration(File f, FlowStyle flowStyle) {
		super(f, flowStyle);
	}

	@Deprecated
	public PolinuxWebApplicationConfiguration(File f, InputStream in) {
		super(f, in);
	}

	public PolinuxWebApplicationConfiguration(InputStream in) {
		super(in);
	}

	@Deprecated
	public PolinuxWebApplicationConfiguration(Map<String, Object> map) {
		super(map);
	}

	@Deprecated
	public PolinuxWebApplicationConfiguration(String path, Map<String, Object> data) {
		super(new File(path), data);
	}

	@Deprecated
	public PolinuxWebApplicationConfiguration(File f, Map<String, Object> data) {
		super(f, data);
	}

	public boolean containsAppConfigurationSection() {
		return this.containsConfigurationSection("app");
	}

	public boolean containsWelcomeFiles() {
		return getWelcomeFiles() != null;
	}

	public boolean containsServletConfigurationSection() {
		return this.containsConfigurationSection("app.servlets");
	}

	public List<String> getWelcomeFiles() {
		if (!containsAppConfigurationSection())
			return null;

		return this.getStringList("app.welcome", null);
	}

	public YamlConfigurationSection getServletConfigurationSection() {
		if (!containsAppConfigurationSection())
			return null;
		return this.getConfigurationSection("app.servlets");
	}

	public String getServletClass(String servletName) {
		return getServletConfigurationSection().getString(servletName + ".class", null);
	}

	public String[] getServletUrlPatterns(String servletName) {
		return getServletConfigurationSection().getStringArray(servletName + ".url-patterns", new String[] {});
	}

	@Override
	public String getWebsiteRoot() {
		if (!containsAppConfigurationSection())
			return null;

		return String.valueOf(this.getObject("app.root", DEFAULT_ROOT));
	}

	public static PolinuxWebApplicationConfiguration loadWebApplicationConfiguration(InputStream in) {
		return new PolinuxWebApplicationConfiguration(in);
	}

}
