package org.polinux.exceptions.http;

import org.polinux.http.HttpServer;

/**
 * Represents an {@link HttpException} on an {@link HttpServer}
 */
public class HttpServerException extends HttpException {
	private static final long serialVersionUID = -3142072858147454064L;

	/**
	 * {@link HttpServer} linked to this exception;
	 */
	protected final HttpServer server;

	public HttpServerException(HttpServer server) {
		this.server = server;
	}

	public HttpServerException(HttpServer server, String message) {
		super(message);
		this.server = server;
	}

	public HttpServerException(HttpServer server, Throwable cause) {
		super(cause);
		this.server = server;
	}

	public HttpServerException(HttpServer server, String message, Throwable cause) {
		super(message, cause);
		this.server = server;
	}

	protected HttpServerException(HttpServer server, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.server = server;
	}

	public HttpServer getServer() {
		return this.server;
	}

}
