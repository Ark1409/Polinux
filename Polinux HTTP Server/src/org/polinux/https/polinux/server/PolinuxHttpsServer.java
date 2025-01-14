package org.polinux.https.polinux.server;

import java.io.File;
import java.net.ServerSocket;

import org.polinux.configuration.server.HttpsServerConfiguration;
import org.polinux.configuration.server.PolinuxHttpServerConfiguration;
import org.polinux.configuration.server.PolinuxHttpsServerConfiguration;
import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.exceptions.https.polinux.PolinuxHttpsServerRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServer;
import org.polinux.https.HttpsServer;
import org.polinux.logging.Logger;

/**
 * Represents an {@code HTTPS Server}, made by the {@code Polinux} API. This
 * class is used as an representative instance of the actual "server" linked to
 * the user's specified {@link java.net.InetAddress internet credentials}.
 * 
 * @see PolinuxHttpServer
 * @see PolinuxHttpsServerThread
 */
public class PolinuxHttpsServer extends PolinuxHttpServer implements HttpsServer {

	/**
	 * Represents the {@link org.polinux.logging.Logger} for the {@code Polinux}
	 * HTTPS API.
	 */
	public static final org.polinux.logging.Logger HTTPS_LOGGER = new Logger("Polinux HTTPS Server", System.err);

	/* Static initialization */
	static {
		HTTPS_LOGGER.lineSeperator = "\r\n";
	}
	/**
	 * Constructs a {@code Polinux HTTPS Server} with the
	 * {@link PolinuxHttpsServerConfiguration} being at the
	 * {@link PolinuxHttpsServerConfiguration#CONFIGURATION_PATH default
	 * configuration path}. Note that this constructor alone will not start the
	 * server. {@link #run()} must explicitly be called to start the server.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
	 *                                           the values of {@code null}. This
	 *                                           will also be thrown if the port is
	 *                                           out of range (0-65535, inclusive)
	 *                                           or if the address is already bound.
	 * @see #PolinuxHttpsServer(PolinuxHttpServerConfiguration)
	 */
	public PolinuxHttpsServer() throws PolinuxHttpServerRuntimeException {
		this(new PolinuxHttpsServerConfiguration(PolinuxHttpsServer.class
				.getResourceAsStream("/" + PolinuxHttpsServerConfiguration.CONFIGURATION_PATH.replace("\\", "/"))));
	}

	/**
	 * Constructs a {@code Polinux HTTPS Server} with the underlying configuration
	 * file Note that this constructor alone will not start the server.
	 * {@link #run()} must explicitly be called to start the server.
	 * 
	 * @param config The {@link File file} leading to the configuration. Can be
	 *               {@code null}.
	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
	 *                                           the values of {@code null}. This
	 *                                           will also be thrown if the port is
	 *                                           out of range (0-65535, inclusive)
	 *                                           or if the address is already bound.
	 * @see #PolinuxHttpsServer(PolinuxHttpsServerConfiguration)
	 */
	public PolinuxHttpsServer(File config) throws PolinuxHttpServerRuntimeException {
		this(new PolinuxHttpsServerConfiguration(config));
	}

	/**
	 * Constructs a {@code Polinux HTTPS Server} with the underlying configuration
	 * file Note that this constructor alone will not start the server.
	 * {@link #run()} must explicitly be called to start the server.
	 * 
	 * @param config The {@link PolinuxHttpsServerConfiguration} for this server
	 *               Cannot be {@code null}.
	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
	 *                                           the values of {@code null}. This
	 *                                           will also be thrown if the port is
	 *                                           out of range (0-65535, inclusive)
	 *                                           or if the address is already bound.
	 * @see #PolinuxHttpServer(PolinuxHttpServerConfiguration)
	 */
	public PolinuxHttpsServer(PolinuxHttpsServerConfiguration config) throws PolinuxHttpServerRuntimeException {
		this(config, true);
	}

	/**
	 * Constructs a {@code Polinux HTTPS Server} with the underlying configuration
	 * file Note that this constructor alone will not start the server.
	 * {@link #run()} must explicitly be called to start the server.
	 * 
	 * @param config     The {@link PolinuxHttpsServerConfiguration} for this server
	 *                   Cannot be {@code null}.
	 * @param initThread Whether the {@link #getServerThread() serverThread} should
	 *                   be initialized when constructing the server. This will, in
	 *                   turn, create a {@link ServerSocket} to link the the
	 *                   {@code IP} and {@code port} specified inside the
	 *                   configuration file. You may disable the server
	 *                   ({@link ServerSocket#close() ServerSocket.close()}) after
	 *                   calling this constructor to disable the created server.
	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
	 *                                           the values of {@code null}. This
	 *                                           will also be thrown if the port is
	 *                                           out of range (0-65535, inclusive)
	 *                                           or if the address is already bound.
	 */
	protected PolinuxHttpsServer(PolinuxHttpsServerConfiguration config, boolean initThread)
			throws PolinuxHttpServerRuntimeException {
		super(config, false);
		if (initThread)
			this.thread = new PolinuxHttpsServerThread(this);
	}

	/**
	 * Retrieves the {@link HttpsServerConfiguration configuration} for the current
	 * {@code HTTPS Server}.
	 * 
	 * @return The configuration file for the server. Probably not {@code null}.
	 * @throws PolinuxHttpsServerRuntimeException If the configuration cannot be found.
	 */
	@Override
	public PolinuxHttpsServerConfiguration getConfiguration() throws PolinuxHttpsServerRuntimeException {
		return (PolinuxHttpsServerConfiguration) super.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(final HttpsServerConfiguration configuration) {
		if (configuration != null && !(configuration instanceof PolinuxHttpsServerConfiguration))
			throw new PolinuxHttpsServerRuntimeException(this,
					"Polinux HTTPS Server only accepts PolinuxHttpsServerConfiguration");
		super.init(configuration);
	}

	/**
	 * Retrieves the {@link ServerType} for the {@code Polinux HTTPS server}.
	 * 
	 * @return The {@code Polinux HTTPS server}'s server type.
	 */
	@Override
	public ServerType getServerType() {
		return this.getConfiguration().getServerType();
	}

	/**
	 * Retrieves the {@code Polinux HTTPS Server's}
	 * {@link org.polinux.logging.Logger logger}. Does not return {@code null}.
	 * 
	 * @return The {@link org.polinux.logging.Logger logger} for this
	 *         {@code Server}, never {@code null}.
	 */
	public org.polinux.logging.Logger getLogger() {
		return HTTPS_LOGGER;
	}

}
