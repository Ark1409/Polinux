package org.polinux.exceptions.https.polinux;

import org.polinux.exceptions.https.HttpsServerException;
import org.polinux.https.polinux.server.PolinuxHttpsServer;

public class PolinuxHttpsException extends HttpsServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3632495917734595672L;

	public PolinuxHttpsException(PolinuxHttpsServer server) {
		super(server);
	}

	public PolinuxHttpsException(PolinuxHttpsServer server, String message) {
		super(server, message);
	}

	public PolinuxHttpsException(PolinuxHttpsServer server, Throwable cause) {
		super(server, cause);
	}

	public PolinuxHttpsException(PolinuxHttpsServer server, String message, Throwable cause) {
		super(server, message, cause);
	}

	protected PolinuxHttpsException(PolinuxHttpsServer server, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(server, message, cause, enableSuppression, writableStackTrace);
	}

	public PolinuxHttpsServer getServer() {
		return (PolinuxHttpsServer) super.getServer();
	}

}
