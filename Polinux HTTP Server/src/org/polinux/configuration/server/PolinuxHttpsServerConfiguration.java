package org.polinux.configuration.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.polinux.configuration.yaml.YamlConfigurationSection;
import org.polinux.http.HttpServer.ServerType;
import org.polinux.https.polinux.server.PolinuxHttpsServer;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class PolinuxHttpsServerConfiguration extends PolinuxHttpServerConfiguration
		implements HttpsServerConfiguration {
	protected static final int DEFAULT_SSL_PORT = PolinuxHttpsServer.DEFAULT_SSL_PORT;

	public PolinuxHttpsServerConfiguration(String path) {
		this(path, DEFAULT_FLOW);
	}

	public PolinuxHttpsServerConfiguration(String path, FlowStyle flowStyle) {
		this(new File(path), flowStyle);
	}

	public PolinuxHttpsServerConfiguration(File f) {
		this(f, DEFAULT_FLOW);
	}

	public PolinuxHttpsServerConfiguration(File f, org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) {
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

	public PolinuxHttpsServerConfiguration(InputStream in) {
		super(in);
	}

	@Override
	public boolean isUseSSL() {
		if (!this.sslSectionExist())
			return false;

		final Object enabledSSL = getSSLSection().getObject("enabled", false);

		return Boolean.parseBoolean(String.valueOf(enabledSSL));
	}

	@Override
	public Integer getPort() {
		if (!this.sslSectionExist())
			return DEFAULT_SSL_PORT;

		final Object sslPort = getSSLSection().getObject("port", DEFAULT_SSL_PORT);

		return Integer.parseInt(String.valueOf(sslPort));
	}

	@Override
	public File getSSLKeyStoreFile() {
		if (!this.sslKeystoreSectionExist())
			return null;
		final Object keyStoreFile = this.getSSLKeystoreSection().getObject("keystore-file", null);

		if (keyStoreFile == null)
			return null;

		return new File(String.valueOf(keyStoreFile).replace("\\", "/"));
	}

	@Override
	public String getSSLKeyStoreFilePassword() {
		if (!this.sslKeystoreSectionExist())
			return null;
		final Object keyStoreFile = this.getSSLKeystoreSection().getObject("keystore-password", null);

		if (keyStoreFile == null)
			return null;

		return String.valueOf(keyStoreFile);
	}

	@Override
	public boolean isUseHttp() {
		if (!this.webHttpSectionExist())
			return this.isUseSSL() ? false : super.isUseHttp();

		final Object isUseHttp = this.getWebHttpSection().getObject("enabled", false);

		return Boolean.parseBoolean(String.valueOf(isUseHttp));

	}

	@Override
	public boolean isSessionCookieSecure() {
		if (!this.sessionCookieSectionExist())
			return false;

		final Object isSecure = this.getSessionCookieSection().getObject("secure", false);

		return Boolean.parseBoolean(String.valueOf(isSecure));
	}

	@Override
	public Integer getHttpPort() {
		return super.getHttpPort();
	}

	@Override
	public ServerType getServerType() {
		return ServerType.HTTPS;
	}

	protected boolean sslSectionExist() {
		return this.containsConfigurationSection("web.https");
	}

	protected boolean sslKeystoreSectionExist() {
		return this.containsConfigurationSection("web.https.keystore");
	}

	protected boolean sslPFXSectionExist() {
		return this.containsConfigurationSection("web.https.pfx");
	}

	protected YamlConfigurationSection getSSLSection() {
		return getWebSection().getConfigurationSection("https");
	}

	protected YamlConfigurationSection getSSLKeystoreSection() {
		return getSSLSection().getConfigurationSection("keystore");
	}

	protected YamlConfigurationSection getSSLPFXSection() {
		return getSSLSection().getConfigurationSection("pfx");
	}

	@Override
	public File getSSLPFXFile() {
		if (!sslPFXSectionExist())
			return null;

		final Object pfxFile = this.getSSLPFXSection().getObject("pfx-file", null);

		if (pfxFile == null)
			return null;

		return new File(String.valueOf(pfxFile).replace("\\", "/"));
	}

	@Override
	public String getSSLPFXFilePassword() {
		if (!sslPFXSectionExist())
			return null;

		final Object pfxFilePass = this.getSSLPFXSection().getObject("pfx-password", null);

		if (pfxFilePass == null)
			return null;

		return String.valueOf(pfxFilePass);
	}

	@Override
	public boolean isUseSSLPFX() {
		if (!sslPFXSectionExist())
			return false;
		final Object isKeystore = this.getSSLPFXSection().getObject("use-pfx", false);

		return Boolean.parseBoolean(String.valueOf(isKeystore));
	}

	@Override
	public boolean isUseSSLKeystore() {
		if (!sslKeystoreSectionExist())
			return false;

		final Object isKeystore = this.getSSLKeystoreSection().getObject("use-keystore", false);

		return Boolean.parseBoolean(String.valueOf(isKeystore));
	}

	@Override
	public boolean isHttpsRedirect() {
		if (!sslSectionExist())
			return false;

		final Object isRedirect = this.getWebHttpSection().getObject("https-redirect", false);

		return Boolean.parseBoolean(String.valueOf(isRedirect));
	}

	@Override
	public boolean isEnablePFXCaching() {
		if (!sslPFXSectionExist())
			return true;

		final Object isPfxCache = this.getSSLPFXSection().getObject("enable-pfx-caching", false);

		return Boolean.parseBoolean(String.valueOf(isPfxCache).trim().equalsIgnoreCase("false") ? "false" : "true");
	}

}
