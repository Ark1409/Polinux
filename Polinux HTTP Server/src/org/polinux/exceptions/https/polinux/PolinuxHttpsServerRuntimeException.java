package org.polinux.exceptions.https.polinux;

import org.polinux.exceptions.https.HttpsServerRuntimeException;
import org.polinux.https.HttpsServer;
import org.polinux.https.polinux.server.PolinuxHttpsServer;

public class PolinuxHttpsServerRuntimeException extends HttpsServerRuntimeException {

	private static final long serialVersionUID = -2609133847864656215L;

	public PolinuxHttpsServerRuntimeException(PolinuxHttpsServer server) {
		super(server);
	}

	public PolinuxHttpsServerRuntimeException(PolinuxHttpsServer server, String message) {
		super(server, message);
	}

	public PolinuxHttpsServerRuntimeException(PolinuxHttpsServer server, Throwable cause) {
		super(server, cause);
	}

	public PolinuxHttpsServerRuntimeException(PolinuxHttpsServer server, String message, Throwable cause) {
		super(server, message, cause);
	}

	protected PolinuxHttpsServerRuntimeException(HttpsServer server, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(server, message, cause, enableSuppression, writableStackTrace);
	}

	public PolinuxHttpsServer getServer() {
		return (PolinuxHttpsServer) super.getServer();
	}

}
