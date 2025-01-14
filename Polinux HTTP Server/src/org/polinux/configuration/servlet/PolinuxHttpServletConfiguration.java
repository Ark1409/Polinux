package org.polinux.configuration.servlet;

import java.io.File;
import java.util.Map;

public class PolinuxHttpServletConfiguration extends HttpServletConfiguration {

	public PolinuxHttpServletConfiguration(Map<String, Object> map, String servletName) {
		this((File) null, map, servletName);
	}

	public PolinuxHttpServletConfiguration(String path, Map<String, Object> data, String servletName) {
		this(new File(path), data, servletName);
	}

	public PolinuxHttpServletConfiguration(File f, Map<String, Object> data, String servletName) {
		super(f, data, servletName);
	}

	public String getServletClass() {
		return this.getString("class", null);
	}

	public String[] getServletUrlPatterns() {
		return this.getStringArray("url-patterns", new String[] { "/" + this.getServletName() });
	}

}
