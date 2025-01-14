package org.polinux.web;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.polinux.configuration.webapp.WebApplicationConfiguration;
import org.polinux.http.servlet.HttpServlet;

public interface WebApplication {
	public WebApplicationConfiguration getConfiguration();

	public Set<? extends HttpServlet> getServlets();

	public default File getParentDirectory() {
		return getFile().getParentFile();
	}

	public File getFile();

	public Map<String, String> getWebFiles();
}
