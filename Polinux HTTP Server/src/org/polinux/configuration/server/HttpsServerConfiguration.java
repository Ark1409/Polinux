package org.polinux.configuration.server;

import java.io.File;

import org.polinux.http.HttpServer.ServerType;

public interface HttpsServerConfiguration extends HttpServerConfiguration {
	public default boolean isUseSSL() {
		return true;
	}

	public abstract Integer getHttpPort();

	public abstract Integer getPort();

	public default ServerType getServerType() {
		if (this.isUseHttp() && this.isUseHttps())
			return ServerType.HTTP_HTTPS;
		if (this.isUseHttp())
			return ServerType.HTTP;
		if (this.isUseHttps())
			return ServerType.HTTPS;
		return null;
	}

	public abstract boolean isUseHttp();

	public default boolean isUseHttps() {
		return this.isUseSSL();
	}

	public abstract File getSSLKeyStoreFile();

	public abstract String getSSLKeyStoreFilePassword();
	
	public abstract File getSSLPFXFile();

	public abstract String getSSLPFXFilePassword();

	public abstract boolean isSessionCookieSecure();
	
	public abstract boolean isUseSSLPFX();
	
	public abstract boolean isUseSSLKeystore();
	
	public abstract boolean isEnablePFXCaching();
	
	
	
	
}
