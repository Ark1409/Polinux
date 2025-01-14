package org.polinux.exceptions.https;

import org.polinux.exceptions.http.HttpServerRuntimeException;
import org.polinux.https.HttpsServer;

public class HttpsServerRuntimeException extends HttpServerRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2894939010274995110L;

	public HttpsServerRuntimeException(HttpsServer server) {
		super(server);
	}

	public HttpsServerRuntimeException(HttpsServer server, String message) {
		super(server, message);
	}

	public HttpsServerRuntimeException(HttpsServer server, Throwable cause) {
		super(server, cause);
	}

	public HttpsServerRuntimeException(HttpsServer server, String message, Throwable cause) {
		super(server, message, cause);
	}

	protected HttpsServerRuntimeException(HttpsServer server, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(server, message, cause, enableSuppression, writableStackTrace);
	}

	public HttpsServer getServer() {
		return (HttpsServer) super.getServer();
	}
}
