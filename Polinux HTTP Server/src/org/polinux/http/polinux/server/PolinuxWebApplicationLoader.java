package org.polinux.http.polinux.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.polinux.configuration.servlet.PolinuxHttpServletConfiguration;
import org.polinux.configuration.webapp.PolinuxWebApplicationConfiguration;
import org.polinux.exceptions.polinux.PolinuxRuntimeException;
import org.polinux.http.polinux.servlet.PolinuxHttpServlet;
import org.polinux.utils.io.InputStreamReader;
import org.polinux.web.PolinuxWebApplication;
import org.polinux.web.WebApplication;

class PolinuxWebApplicationLoader extends URLClassLoader {
	private static final List<PolinuxWebApplicationLoader> loaders = new ArrayList<PolinuxWebApplicationLoader>();
	private String name;
	protected PolinuxWebApplicationConfiguration config;
	public static final String RESOURCE_FILE_SEPERATOR = "/";
	protected boolean loaded = false;
	protected PolinuxWebApplication app;
	private PolinuxLibClassLoader libLoader;

	@Deprecated
	public static final String DEFAULT_LOADER_NAME = "DEFAULT";

	@Deprecated
	private static final PolinuxWebApplicationLoader instance = new PolinuxWebApplicationLoader(DEFAULT_LOADER_NAME);

	private static final String WEBAPP_DESCRIPTION_FILE = PolinuxWebApplicationConfiguration.CONFIGURATION_PATH
			.replace("\\", "/");

	public PolinuxWebApplicationLoader(String name) {
		super(new URL[] {}, ClassLoader.getSystemClassLoader());
		this.name = name;

	}

	public PolinuxWebApplication getWebApplication() {
		return this.app;
	}

	/**
	 * Loads and sets this plugin loader's attached web application to the specified
	 * file. Note that the specified file is not the parent/directory of the file,
	 * but rather than the file itself. If you input the file's getInputStream, this
	 * method may throw a {@link PolinuxWebApplicationLoaderException}.
	 * 
	 * @param f The web application (as a {@link java.io.File File}).
	 * @throws IOException                          If an I/O error occurs.
	 * @throws PolinuxWebApplicationLoaderException If an error occurred while
	 *                                              trying to load the plugin.
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public void loadWebApplication(final File f) throws IOException, PolinuxWebApplicationLoaderException {
		if (!f.getName().trim().toLowerCase().endsWith(".pwa")) {
			throw new PolinuxWebApplicationLoaderException(
					"PolinuxWebApplicationLoaderException only loads .pwa files");
		}

		if (!f.exists()) {
			throw new PolinuxWebApplicationLoaderException(
					"PolinuxWebApplicationLoaderException cannot load .pwa file that does not exist!");
		}
		final URL fileURL = f.toURI().toURL();

		final JarFile jar;
		jar = new JarFile(f);

		JarEntry configJarEntry;

		try {
			configJarEntry = jar.getJarEntry(WEBAPP_DESCRIPTION_FILE);
		} catch (IllegalStateException e) {
			throw new PolinuxWebApplicationLoaderException("PWA file " + f.getPath() + " is closed!", e);
		}

		if (configJarEntry == null) {
			throw new PolinuxWebApplicationLoaderException(
					"PWA file " + f.getPath() + " does not contain " + WEBAPP_DESCRIPTION_FILE + "!");
		}

		PolinuxWebApplicationConfiguration config;

		try {
			config = PolinuxWebApplicationConfiguration
					.loadWebApplicationConfiguration(jar.getInputStream(configJarEntry));
		} catch (IOException e) {
			throw new PolinuxWebApplicationLoaderException(
					"Unable to load " + WEBAPP_DESCRIPTION_FILE + " from pwa file " + f.getPath());
		}

		this.config = config;

		if (!config.containsAppConfigurationSection()) {
			throw new PolinuxWebApplicationLoaderException(WEBAPP_DESCRIPTION_FILE + " from pwa file " + f.getName()
					+ " does not contain \"app\" main section");
		}

//		if (!config.containsPluginMain()) {
//			throw new PolinuxWebApplicationLoaderException(WEBAPP_DESCRIPTION_FILE + " from pwa file "
//					+ f.getName() + " does not contain a main class locator");
//		}

//		if (!config.containsPluginVersion()) {
//			throw new PolinuxWebApplicationLoaderException(
//					Plugin.PLUGIN_DESCRIPTION_FILE + " from jar file " + f.getName() + " does not contain a version");
//		}

		this.addURL(fileURL);

		final PolinuxLibClassLoader libLoader = new PolinuxLibClassLoader(f, this);

		this.libLoader = libLoader;

		Set<PolinuxHttpServlet> servlets = new LinkedHashSet<PolinuxHttpServlet>();

		if (config.containsServletConfigurationSection()) {

			Set<String> keys = config.getServletConfigurationSection().getKeys(false);

			for (String servletName : keys) {

				Class<? extends PolinuxHttpServlet> clazz;

				try {

					clazz = (Class<? extends PolinuxHttpServlet>) Class
							.forName(String.valueOf(this.config.getServletClass(servletName)), true, libLoader);
				} catch (ClassNotFoundException e) {
					throw new PolinuxWebApplicationLoaderException(
							"Unable to find servlet class " + String.valueOf(this.config.getServletClass(servletName)));
				} catch (ClassCastException e) {
					throw new PolinuxWebApplicationLoaderException(
							"Servlet class " + String.valueOf(this.config.getServletClass(servletName))
									+ " does not extend PolinuxHttpServlet");
				}

				// this.dataFolder = new File(f.getParentFile(), config.getPluginName());

				PolinuxHttpServlet servlet = null;

				try {
					servlet = clazz.getConstructor().newInstance();

					PolinuxHttpServletConfiguration servletConfig = new PolinuxHttpServletConfiguration(
							config.getServletConfigurationSection().getConfigurationSection(servletName).toMap(),
							servletName);
					servlet.init(servletConfig);
					servlets.add(servlet);

					servlet.init();

					// config.setAttachedPlugin(this.plugin);
				} catch (InstantiationException e) {
					// this.loaded = false;
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// this.loaded = false;
					throw new PolinuxWebApplicationLoaderException(
							"Unable to access empty constructor for class " + clazz.getName(), e);
				} catch (IllegalArgumentException e) {
					// this.loaded = false;
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// this.loaded = false;
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// this.loaded = false;
					throw new PolinuxWebApplicationLoaderException(
							"Unable to find empty constructor for class " + clazz.getName(), e);
				} catch (ClassCastException e) {
					// this.loaded = false;
					throw new PolinuxWebApplicationLoaderException(
							"Servlet class " + clazz.getName() + " does not extend PolinuxHttpServlet", e);
				} catch (ExceptionInInitializerError e) {
					// this.loaded = false;
					throw new PolinuxWebApplicationLoaderException("Unable to initialize class " + clazz.getName(), e);
				}

			}

		}
		Enumeration<JarEntry> entries = jar.entries();

		Map<String, String> docFiles = new LinkedHashMap<String, String>();

		for (; entries.hasMoreElements();) {
			JarEntry e = entries.nextElement();
			final String name = e.getName();

			if (name.startsWith(this.config.getWebsiteRoot().endsWith("/") ? this.config.getWebsiteRoot()
					: this.config.getWebsiteRoot() + "/")) {
				try {
					final InputStream in = jar.getInputStream(e);

					docFiles.put(name, InputStreamReader.read(in));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}

		app = new PolinuxWebApplication(this.config, servlets, f.getParentFile(), f, docFiles);
		this.loaded = true;

		// Finally, close the jar
		jar.close();

	}

	@Deprecated
	public static final PolinuxWebApplicationLoader getDefaultPluginLoader() {
		return instance;
	}

	public static final PolinuxWebApplicationLoader getPluginLoader(String name) {
		if (name.equals(DEFAULT_LOADER_NAME)) {
			return getDefaultPluginLoader();
		}
		if (!containsPluginLoader(name)) {
			return createPluginLoader(name);
		} else {
			for (int i = 0; i < loaders.size(); i++) {
				PolinuxWebApplicationLoader loader = loaders.get(i);
				if (loader.getIdentifier().equals(name))
					return loader;
			}
		}
		return null;
	}

	private static final boolean containsPluginLoader(String name) {
		for (int i = 0; i < loaders.size(); i++) {
			PolinuxWebApplicationLoader loader = loaders.get(i);
			if (loader.getIdentifier().equals(name))
				return true;
		}
		return false;
	}

	private static final PolinuxWebApplicationLoader createPluginLoader(String name) {
		PolinuxWebApplicationLoader loader = new PolinuxWebApplicationLoader(name);
		loaders.add(loader);
		return loader;
	}

	public String getIdentifier() {
		return this.name;
	}

	void setIdentifier(String name) {
		this.name = name;
	}

	/**
	 * Gets the resource from the specified path. It is not necessary to include a
	 * '/' before the resource name. If the resource was not found, this method
	 * returns {@code null}.
	 * 
	 * @return The resource found (or null).
	 * @throws PluginLoaderException If the plugin attached to this plugin loader
	 *                               has not been loaded yet or of the resource path
	 *                               is null
	 */
	public URL getResource(String resource) {
		if (!isLoaded()) {
			throw new PolinuxWebApplicationLoaderException("Cannot access resource for unloaded plugin");
		}
		if (resource == null) {
			throw new PolinuxWebApplicationLoaderException("Cannot find resource for path null");
		}
		try {
			return super.getResource(resource);
		} catch (NullPointerException e) {
			return null;
		}
	}

	void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Gets whether this web application class loader is loaded and linked to a
	 * {@link WebApplication} from the {@link #loadWebApplication(File)} method.
	 * 
	 * @return whether this web application class loader is loaded and linked to a
	 *         {@link WebApplication}.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	PolinuxLibClassLoader getLibClassLoader() {
		if (!isLoaded()) {
			throw new PolinuxWebApplicationLoaderException("Cannot access resource for unloaded plugin");
		}
		return this.libLoader;
	}

	/**
	 * Unloads this web application loader. This will also set the loader to not be
	 * initialized.
	 */
	public void unload() {
//		if (this.plugin != null) {
//			this.plugin.deinit();
//		}
		this.app = null;
		this.config = null;
		this.loaded = false;
	}

	static class PolinuxWebApplicationLoaderException extends PolinuxRuntimeException {
		private static final long serialVersionUID = -3407032887003790776L;

		public PolinuxWebApplicationLoaderException() {
			super();
		}

		public PolinuxWebApplicationLoaderException(String message) {
			super(message);
		}

		public PolinuxWebApplicationLoaderException(Throwable cause) {
			super(cause);
		}

		public PolinuxWebApplicationLoaderException(String message, Throwable cause) {
			super(message, cause);
		}

		protected PolinuxWebApplicationLoaderException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

}
