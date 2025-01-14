package org.polinux.exceptions.http.polinux;

import org.polinux.exceptions.http.HttpServerException;
import org.polinux.http.polinux.server.PolinuxHttpServer;

/**
 * Represents an {@link Exception} on a {@link PolinuxHttpServer}
 */
public class PolinuxHttpServerException extends HttpServerException {
	private static final long serialVersionUID = -4981559300160070855L;

	public PolinuxHttpServerException(PolinuxHttpServer server) {
		super(server);
	}

	public PolinuxHttpServerException(PolinuxHttpServer server, String message) {
		super(server, message);
	}

	public PolinuxHttpServerException(PolinuxHttpServer server, Throwable cause) {
		super(server, cause);
	}

	public PolinuxHttpServerException(PolinuxHttpServer server, String message, Throwable cause) {
		super(server, message, cause);
	}

	protected PolinuxHttpServerException(PolinuxHttpServer server, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(server, message, cause, enableSuppression, writableStackTrace);
	}

	public PolinuxHttpServer getServer() {
		return (PolinuxHttpServer) super.getServer();
	}

}
