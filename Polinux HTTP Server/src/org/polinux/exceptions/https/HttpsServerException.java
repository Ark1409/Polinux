package org.polinux.exceptions.https;

import org.polinux.exceptions.http.HttpServerException;
import org.polinux.https.HttpsServer;

public class HttpsServerException extends HttpServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6302049695568065224L;

	public HttpsServerException(HttpsServer server) {
		super(server);
	}

	public HttpsServerException(HttpsServer server, String message) {
		super(server, message);
	}

	public HttpsServerException(HttpsServer server, Throwable cause) {
		super(server, cause);
	}

	public HttpsServerException(HttpsServer server, String message, Throwable cause) {
		super(server, message, cause);
	}

	protected HttpsServerException(HttpsServer server, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(server, message, cause, enableSuppression, writableStackTrace);
	}

	public HttpsServer getServer() {
		return (HttpsServer) super.getServer();
	}

}
