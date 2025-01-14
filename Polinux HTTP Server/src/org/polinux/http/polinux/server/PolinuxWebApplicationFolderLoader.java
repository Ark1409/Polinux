package org.polinux.http.polinux.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.polinux.http.polinux.server.PolinuxWebApplicationLoader.PolinuxWebApplicationLoaderException;
import org.polinux.web.PolinuxWebApplication;

class PolinuxWebApplicationFolderLoader {
	/* Varible declarations */
	protected static final List<PolinuxWebApplicationFolderLoader> loaders = new ArrayList<PolinuxWebApplicationFolderLoader>();
	public static final String DEFAULT_LOADER_NAME = "DEFAULT";
	protected String name;
	protected List<PolinuxWebApplicationLoader> appLoaders;

	// private List<PolinuxWebApplication> apps = new
	// LinkedList<PolinuxWebApplication>;
	protected static final PolinuxWebApplicationFolderLoader instance = new PolinuxWebApplicationFolderLoader(
			DEFAULT_LOADER_NAME);

	/* Constructor */
	protected PolinuxWebApplicationFolderLoader(String name) {
		this.name = name;
		appLoaders = new CopyOnWriteArrayList<PolinuxWebApplicationLoader>();
	}

	/* Called to load a directory of plugins */
	PolinuxWebApplicationFolderLoader loadDirectory(final File dir) {
		return loadDirectory(dir, "");
	}

	/* Private method used to load directory of plugins */
	private PolinuxWebApplicationFolderLoader loadDirectory(final File dir, final String prefix) {

		if (!dir.exists())
			return this;

		final File[] allFiles = dir.listFiles();

		for (int i = 0; i < allFiles.length; i++) {
			File file = allFiles[i];

			if (file.isDirectory()) {
				loadDirectory(file, prefix + "/" + file.getName());
			}

			else if (file.isFile()) {
				if (file.getName().trim().toLowerCase().endsWith(".pwa")) {
					PolinuxWebApplicationLoader loader = PolinuxWebApplicationLoader
							.getPluginLoader("PolinuxWebApplicationFolderLoader-v1.0/" + prefix
									+ (prefix.length() >= 1 ? "/" : "") + file.getName());

					boolean found = true;

					try {
						loader.loadWebApplication(file);
						loader.setLoaded(true);
					} catch (PolinuxWebApplicationLoaderException e) {
						e.printStackTrace();
						found = false;
					} catch (IOException e) {
						e.printStackTrace();
						found = false;
					}

					// loader.getAttachedPlugin().setPolinuxWebApplicationFolderLoader(this);
					// loader.setIdentifier(loader.getPluginDescriptionFile().getPluginName());

					this.appLoaders.add(loader);
				}
			}
		}

		return this;

	}

	/* Static methods related with initialization of this class */

	static final PolinuxWebApplicationFolderLoader getDefaultPolinuxWebApplicationFolderLoader() {
		return instance;
	}

	static PolinuxWebApplicationFolderLoader getPolinuxWebApplicationFolderLoader(String name) {
		if (!containsPolinuxWebApplicationFolderLoader(name)) {
			return createPolinuxWebApplicationFolderLoader(name);
		} else {
			for (int i = 0; i < loaders.size(); i++) {
				PolinuxWebApplicationFolderLoader loader = loaders.get(i);
				if (loader.getIdentifier().equals(name))
					return loader;
			}
		}
		return null;
	}

	static final boolean containsPolinuxWebApplicationFolderLoader(String name) {
		for (int i = 0; i < loaders.size(); i++) {
			if (loaders.get(i).getIdentifier().equals(name))
				return true;
		}
		return false;
	}

	static PolinuxWebApplicationFolderLoader createPolinuxWebApplicationFolderLoader(String name) {
		PolinuxWebApplicationFolderLoader loader = new PolinuxWebApplicationFolderLoader(name);
		loaders.add(loader);
		return loader;
	}

	/* Package private methods */

	String getIdentifier() {
		return this.name;
	}

	void setIdentifier(String name) {
		this.name = name;
	}

	void unload() {
		appLoaders = new CopyOnWriteArrayList<PolinuxWebApplicationLoader>();
	}

	List<PolinuxWebApplication> getWebApplications() {
		List<PolinuxWebApplication> plugins = new ArrayList<PolinuxWebApplication>(this.appLoaders.size());
		for (int i = 0; i < this.appLoaders.size(); i++) {
			PolinuxWebApplicationLoader loader = this.appLoaders.get(i);
			if (loader != null && loader.isLoaded())
				plugins.add(i, loader.getWebApplication());
		}
		return plugins;
	}
}
