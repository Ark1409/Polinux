package org.polinux.configuration.servlet;

import java.io.File;
import java.util.Map;

import org.polinux.configuration.yaml.YamlConfiguration;

@SuppressWarnings("deprecation")
public abstract class HttpServletConfiguration extends YamlConfiguration {
	protected String servletName;

	public HttpServletConfiguration(Map<String, Object> map, String servletName) {
		this((File) null, map, servletName);
	}

	public HttpServletConfiguration(String path, Map<String, Object> data, String servletName) {
		this(new File(path), data, servletName);
	}

	public HttpServletConfiguration(File f, Map<String, Object> data, String servletName) {
		super(f, data);
		this.servletName = servletName;
	}

	public String getServletName() {
		return this.servletName;
	}
	
	public abstract String getServletClass();
	
	public abstract String[] getServletUrlPatterns();
}
