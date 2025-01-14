package org.polinux.exceptions.http;

import org.polinux.exceptions.polinux.PolinuxRuntimeException;
import org.polinux.http.HttpServer;

/**
 * Represents a {@link PolinuxRuntimeException} on an {@link HttpServer}
 */
public class HttpServerRuntimeException extends HttpRuntimeException {

	private static final long serialVersionUID = 1420468428417647912L;

	/**
	 * {@link HttpServer} linked to this exception;
	 */
	protected final HttpServer server;

	public HttpServerRuntimeException(HttpServer server) {
		super();
		this.server = server;
	}

	public HttpServerRuntimeException(HttpServer server, String message) {
		super(message);
		this.server = server;
	}

	public HttpServerRuntimeException(HttpServer server, Throwable cause) {
		super(cause);
		this.server = server;
	}

	public HttpServerRuntimeException(HttpServer server, String message, Throwable cause) {
		super(message, cause);
		this.server = server;
	}

	protected HttpServerRuntimeException(HttpServer server, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.server = server;
	}

	/**
	 * 
	 * @return
	 */
	public HttpServer getServer() {
		return this.server;
	}

}
