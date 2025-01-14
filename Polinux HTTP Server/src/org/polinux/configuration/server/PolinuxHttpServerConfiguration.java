package org.polinux.configuration.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.polinux.configuration.yaml.YamlConfiguration;
import org.polinux.configuration.yaml.YamlConfigurationException;
import org.polinux.configuration.yaml.YamlConfigurationSection;
import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.http.HttpResponseCookie;
import org.polinux.http.HttpResponseCookie.SameSitePolicy;
import org.polinux.http.polinux.server.PolinuxHttpServer;
import org.polinux.http.polinux.session.PolinuxHttpSessionCookie;
import org.polinux.utils.enc.CharacterSet;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

@SuppressWarnings({ "deprecation" })
public class PolinuxHttpServerConfiguration extends YamlConfiguration implements HttpServerConfiguration {

	/**
	 * Represents the path of the default {@link PolinuxHttpServerConfiguration}
	 * file.
	 */
	public static final String CONFIGURATION_PATH = "server" + File.separator + "configuration.yml";

	protected static final String DEFAULT_HOST = PolinuxHttpServer.DEFAULT_HOST;
	protected static final int DEFAULT_PORT = PolinuxHttpServer.DEFAULT_PORT;

	protected static final String DEFAULT_SESSION_COOKIE_NAME = PolinuxHttpSessionCookie.DEFAULT_NAME;

	public static final String DEFAULT_WEB_ROOT = "wwwroot";

	public static final int DEFAULT_BACKLOG = 0x64; // 100

	public PolinuxHttpServerConfiguration(String path) {
		this(path, DEFAULT_FLOW);
	}

	public PolinuxHttpServerConfiguration(String path, FlowStyle flowStyle) {
		this(new File(path), flowStyle);
	}

	public PolinuxHttpServerConfiguration(File f) {
		this(f, DEFAULT_FLOW);
	}

	public PolinuxHttpServerConfiguration(File f, org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) {
		super(f, flowStyle);
		if (!f.exists()) {
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ensureWebExist();
	}

	@Deprecated
	public PolinuxHttpServerConfiguration(File f, InputStream in) {
		super(f, in);

		ensureWebExist();
	}

	public PolinuxHttpServerConfiguration(InputStream in) {
		super(in);
		ensureWebExist();
	}

	@Deprecated
	public PolinuxHttpServerConfiguration(Map<String, Object> map) {
		super(map);
		ensureWebExist();
	}

	@Deprecated
	public PolinuxHttpServerConfiguration(String path, Map<String, Object> data) {
		super(new File(path), data);
		ensureWebExist();
	}

	@Deprecated
	public PolinuxHttpServerConfiguration(File f, Map<String, Object> data) {
		super(f, data);
		ensureWebExist();
	}

	protected boolean ensureWebExist() {
		if (!webExists()) {
			try {
				throw new PolinuxServerConfigurationException(
						"\"web\" section inside server configuration does not exist!", this);
			} catch (PolinuxServerConfigurationException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public String getHost() {
		if (!webExists())
			return null;
		final Object host = getWebSection().getObject("host", null);

		if (host == null)
			return null;

		return String.valueOf(host);
	}

	public Integer getHttpPort() {
		if (!webExists())
			return DEFAULT_PORT;

		final Object port = getWebHttpSection().getObject("port", DEFAULT_PORT);

		return Integer.parseInt(String.valueOf(port));
	}

	public String getWebsiteRoot() {
		if (!webExists())
			return DEFAULT_WEB_ROOT;

		final Object root = getWebSection().getObject("root", DEFAULT_WEB_ROOT);

		return String.valueOf(root).replace("\\", "/");
	}

	protected boolean webExists() {
		return this.containsConfigurationSection("web");
	}

	protected YamlConfigurationSection getWebSection() {
		return this.getConfigurationSection("web");
	}

	protected boolean webHttpSectionExist() {
		return this.containsConfigurationSection("web.http");
	}

	protected YamlConfigurationSection getWebHttpSection() {
		return getWebSection().getConfigurationSection("http");
	}

	protected YamlConfigurationSection getSessionCookieSection() {
		return getWebSection().getConfigurationSection("session.cookie");
	}

	protected boolean sessionCookieSectionExist() {
		return this.containsConfigurationSection("web.session.cookie");
	}

	@Override
	public String getSessionCookieName() {
		if (!sessionCookieSectionExist())
			return DEFAULT_SESSION_COOKIE_NAME;

		final Object name = getSessionCookieSection().getObject("name", DEFAULT_SESSION_COOKIE_NAME);

		return String.valueOf(name);
	}

	@Override
	public boolean isSessionCookieHttpOnly() {
		if (!sessionCookieSectionExist())
			return true;

		final Object httpOnly = getSessionCookieSection().getObject("http-only", true);

		return Boolean.parseBoolean(String.valueOf(httpOnly));
	}

	@Override
	public boolean isSessionCookieSecure() {
		return false;
	}

	@Override
	public SameSitePolicy getSessionCookieSameSitePolicy() {
		if (!sessionCookieSectionExist())
			return HttpResponseCookie.SameSitePolicy.LAX;

		final Object sameSitePolicy = getSessionCookieSection().getObject("same-site-policy", null);

		if (sameSitePolicy == null)
			return HttpResponseCookie.SameSitePolicy.LAX;

		return HttpResponseCookie.SameSitePolicy.valueOf(String.valueOf(sameSitePolicy).toUpperCase()) == null
				? HttpResponseCookie.SameSitePolicy.LAX
				: HttpResponseCookie.SameSitePolicy.valueOf(String.valueOf(sameSitePolicy).toUpperCase());
	}

	@Override
	public boolean isUseHttp() {
		return true;
	}

	public static PolinuxHttpServerConfiguration loadConfiguration(InputStream in) {
		return new PolinuxHttpServerConfiguration(in);
	}

	public static PolinuxHttpServerConfiguration loadConfiguration(File file) {
		return new PolinuxHttpServerConfiguration(file);
	}

	public static PolinuxHttpServerConfiguration loadConfiguration(String path) {
		return loadConfiguration(new File(path));
	}

	public static final class PolinuxServerConfigurationException extends YamlConfigurationException {

		private static final long serialVersionUID = 1479931815403466353L;

		public PolinuxServerConfigurationException(PolinuxHttpServerConfiguration c) {
			this("", c);
		}

		public PolinuxServerConfigurationException(String message, PolinuxHttpServerConfiguration c) {
			super(message, c);

		}

		public PolinuxServerConfigurationException(Throwable cause, PolinuxHttpServerConfiguration c) {
			super(cause, c);

		}

		public PolinuxServerConfigurationException(String message, Throwable cause, PolinuxHttpServerConfiguration c) {
			super(message, cause, c);
		}

		public PolinuxServerConfigurationException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace, PolinuxHttpServerConfiguration y) {
			super(message, cause, enableSuppression, writableStackTrace, y);

		}

		public PolinuxHttpServerConfiguration getPolinuxServerConfiguration() {
			return (PolinuxHttpServerConfiguration) this.c;
		}
	}

	@Override
	public Integer getPort() {
		return this.getHttpPort();
	}

	public static PolinuxHttpServerConfiguration writeDefaultConfig(File configFile) {
		if (configFile == null)
			return null;

		if (!configFile.exists()) {
			if (!configFile.getParentFile().exists()) {
				configFile.getParentFile().mkdirs();
			}
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			saveResource(PolinuxHttpServerConfiguration.class
					.getResourceAsStream("/" + CONFIGURATION_PATH.replace("\\", "/")), configFile, CharacterSet.UTF_8);
		} catch (PolinuxHttpServerRuntimeException | IOException e) {
			e.printStackTrace();
		}

		return new PolinuxHttpServerConfiguration(configFile);

	}

	/**
	 * Saves the specified resource (InputStream) to the specified path (second
	 * file) with the specified encoding. The advantage of using save resource is
	 * that this method will keep the comments from the original file from the
	 * InputStream, for this method literally just copies the contents of the
	 * original file and puts it inside of the specified one.<br>
	 * <br>
	 * This is a protected method and therefore is only accessible through an
	 * implementation of this class.
	 * 
	 * @param in       The file from the InputStream to copy.
	 * @param to       The file to copy the data to.
	 * @param encoding The encoding to keep in mind.
	 * @throws IOException                       If an I/O Exception occurs.
	 * @throws PolinuxHttpServerRuntimeException If the the InputStream specified is
	 *                                           {@code null} or if the file to copy
	 *                                           the data to is {@code null}.
	 */
	protected static final void saveResource(InputStream in, File to, Charset encoding)
			throws IOException, PolinuxHttpServerRuntimeException {

		if (in == null) {
			throw new PolinuxHttpServerRuntimeException(null, "Cannot save null resource");
		}
		if (to == null) {
			throw new PolinuxHttpServerRuntimeException(null, "Cannot save resource to null directory");
		}
		encoding = encoding == null ? CharacterSet.UTF_8 : encoding;
		if (!to.getParentFile().exists()) {
			to.getParentFile().mkdirs();
		}
		if (!to.exists()) {
			to.createNewFile();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
		BufferedWriter bw = new BufferedWriter(new FileWriter(to));
		String data = "";
		String line;
		// final String lineSeperator = PolinuxHttpServer.HTTP_LOGGER.lineSeperator;
		final String lineSeperator = System.lineSeparator();
		while ((line = br.readLine()) != null) {
			data += line + lineSeperator;
		}
		data = data.substring(0, data.lastIndexOf(lineSeperator));
		bw.write(data);

		br.close();
		bw.close();
	}

	@Override
	public boolean isHttpsRedirect() {
		if (!this.webHttpSectionExist())
			return false;

		final Object isRedirect = this.getWebHttpSection().getObject("https-redirect", false);

		return Boolean.parseBoolean(String.valueOf(isRedirect));
	}

	@Override
	public int getBacklog() {
		if (!this.webExists())
			return DEFAULT_BACKLOG;

		final Object backlog = this.getWebSection().getObject("backlog", DEFAULT_BACKLOG);

		return Integer.parseInt(String.valueOf(backlog));
	}

	public final boolean save() {
		return true;
	}

	public final void dumpToFile(File f) throws IOException {
		if (!f.equals(this.f))
			super.dumpToFile(f);
	}

	@Override
	public String getSessionCookieDomain() {
		if (!sessionCookieSectionExist())
			return null;

		final Object sessionCookieDomain = getSessionCookieSection().getObject("domain", null);

		if (sessionCookieDomain == null)
			return null;

		return String.valueOf(sessionCookieDomain);
	}
}
