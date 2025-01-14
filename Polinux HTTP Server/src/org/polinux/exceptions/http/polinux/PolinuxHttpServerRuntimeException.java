package org.polinux.exceptions.http.polinux;

import org.polinux.exceptions.http.HttpServerRuntimeException;
import org.polinux.http.HttpServer;
import org.polinux.http.polinux.server.PolinuxHttpServer;

/**
 * Represents a {@link RuntimeException} on a {@link PolinuxHttpServer}
 */
public class PolinuxHttpServerRuntimeException extends HttpServerRuntimeException {
	private static final long serialVersionUID = -9091145388008234152L;

	public PolinuxHttpServerRuntimeException(HttpServer server) {
		super(server);
	}

	public PolinuxHttpServerRuntimeException(HttpServer server, String message) {
		super(server, message);
	}

	public PolinuxHttpServerRuntimeException(HttpServer server, Throwable cause) {
		super(server, cause);
	}

	public PolinuxHttpServerRuntimeException(HttpServer server, String message, Throwable cause) {
		super(server, message, cause);
	}

	protected PolinuxHttpServerRuntimeException(HttpServer server, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(server, message, cause, enableSuppression, writableStackTrace);
	}
	
	public PolinuxHttpServer getServer() {
		return (PolinuxHttpServer) super.getServer();
	}

}
