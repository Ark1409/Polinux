package org.polinux.web;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.polinux.configuration.webapp.PolinuxWebApplicationConfiguration;
import org.polinux.http.polinux.servlet.PolinuxHttpServlet;

public class PolinuxWebApplication implements WebApplication {

	protected PolinuxWebApplicationConfiguration config;
	protected Set<PolinuxHttpServlet> servlets;
	protected File parentDirectory;
	protected File file;
	protected Map<String, String> webFiles;

	public PolinuxWebApplication(PolinuxWebApplicationConfiguration config, Set<PolinuxHttpServlet> servlets,
			File parent, File file, Map<String, String> webFiles) {
		this.config = config;
		this.servlets = servlets;
		this.parentDirectory = parent;
		this.file = file;
		this.webFiles = webFiles;
	}

	@Override
	public Set<? extends PolinuxHttpServlet> getServlets() {
		return this.servlets;
	}

	@Override
	public PolinuxWebApplicationConfiguration getConfiguration() {
		return this.config;
	}

	@Override
	public File getParentDirectory() {
		return this.parentDirectory;
	}

	@Override
	public File getFile() {
		return this.file;
	}

	public Map<String, String> getWebFiles() {
		return webFiles;
	}

}
