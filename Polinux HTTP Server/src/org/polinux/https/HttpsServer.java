package org.polinux.https;

import org.polinux.configuration.server.HttpsServerConfiguration;
import org.polinux.exceptions.https.HttpsServerRuntimeException;
import org.polinux.http.HttpServer;
import org.polinux.https.polinux.server.PolinuxHttpsServer;

/**
 * Represents an {@code HTTP SSL Server}, not necessarily made by the
 * {@code Polinux} API. This interface is used as an representative instance of
 * an actual "server" linked to a specified host and port.
 * 
 * @see PolinuxHttpsServer
 */
public interface HttpsServer extends HttpServer {
	/**
	 * The default {@code SSL} port, if none is specified.
	 */
	public static final int DEFAULT_SSL_PORT = 0x1bb; // 443
	
	/**
	 * Retrieves the {@link HttpsServerConfiguration configuration} for the current
	 * {@code HTTPS Server}.
	 * 
	 * @return The configuration file for the server. Probably not {@code null}.
	 * @throws HttpsServerRuntimeException If the configuration cannot be found.
	 */
	@Override
	public HttpsServerConfiguration getConfiguration() throws HttpsServerRuntimeException;

	/**
	 * Initializes the {@code HTTPS Server}, if needed.
	 * 
	 * @param configuration The configuration for the server.
	 * @throws HttpsServerRuntimeException If an error occurs while initializing the
	 *                                     server.
	 */
	public void init(final HttpsServerConfiguration configuration) throws HttpsServerRuntimeException;
}
