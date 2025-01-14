package org.polinux.configuration.server;

import org.polinux.http.HttpResponseCookie;
import org.polinux.http.HttpServer.ServerType;

public interface HttpServerConfiguration {

	public abstract String getHost();

	public abstract Integer getPort();

	public abstract String getWebsiteRoot();

	public abstract String getSessionCookieName();

	public default ServerType getServerType() {
		return ServerType.HTTP;
	}

	public default boolean isUseHttp() {
		return true;
	}

	public abstract boolean isSessionCookieHttpOnly();

	public default boolean isSessionCookieSecure() {
		return false;
	}

	public abstract HttpResponseCookie.SameSitePolicy getSessionCookieSameSitePolicy();
	
	public abstract boolean isHttpsRedirect();
	
	public abstract int getBacklog();
	
	public abstract String getSessionCookieDomain();
	
	

}
