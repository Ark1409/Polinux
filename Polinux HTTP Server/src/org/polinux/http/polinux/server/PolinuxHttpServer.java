package org.polinux.http.polinux.server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;

import org.polinux.commands.PolinuxCommand;
import org.polinux.commands.PolinuxCommandManager;
import org.polinux.configuration.server.HttpServerConfiguration;
import org.polinux.configuration.server.PolinuxHttpServerConfiguration;
import org.polinux.exceptions.http.HttpRuntimeException;
import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.http.HttpServer;
import org.polinux.https.polinux.server.PolinuxHttpsServer;
import org.polinux.logging.Logger;
import org.polinux.utils.collections.CollectionUtils;
import org.polinux.web.PolinuxWebApplication;

/**
 * Represents an {@code HTTP Server}, made by the {@code Polinux} API. This
 * class is used as an representative instance of the actual "server" linked to
 * the user's specified {@link java.net.InetAddress internet credentials}.
 * 
 * @see PolinuxHttpsServer
 * @see PolinuxHttpServerThread
 */
public class PolinuxHttpServer implements HttpServer {

	/**
	 * Thread for the server. Should not be touched and is managed by the
	 * {@code Polinux} API.
	 */
	protected transient PolinuxHttpServerThread thread;

	/**
	 * Represents the {@link PolinuxCommandManager} for the
	 * {@code Polinux HTTP Server}.
	 */
	protected transient PolinuxCommandManager commandManager;

	/**
	 * Represents the {@link org.polinux.logging.Logger} for the {@code Polinux}
	 * HTTP API.
	 */
	public static final org.polinux.logging.Logger HTTP_LOGGER = new Logger("Polinux HTTP Server", System.err);
	
	/* Static initialization */
	static {
		HTTP_LOGGER.lineSeperator = "\r\n";
	}

//	/**
//	 * Represents the {@link PolinuxHttpServer.ServerType} for the current
//	 * {@code PolinuxHttpServer}.
//	 */
//	protected final PolinuxHttpServer.ServerType serverType;

	/**
	 * The default host, if none is specified.
	 */
	public static final String DEFAULT_HOST = "127.0.0.1";

	/**
	 * The default port, if none is specified.
	 */
	public static final int DEFAULT_PORT = 0x50; // 80

	/**
	 * Represents the {@code default} {@link PolinuxHttpServer.ServerType} for all
	 * {@link PolinuxHttpServer PolinuxHttpServers}.
	 */
	public static final PolinuxHttpServer.ServerType DEFAULT_SERVER_TYPE = PolinuxHttpServer.ServerType.HTTP;

	/**
	 * The minimum value for a port.
	 */
	public static final int PORT_MIN = 0x0; // 0

	/**
	 * The maximum value for a port.
	 */
	public static final int PORT_MAX = 0xFFFF; // 65535

	/**
	 * Default constant representing how many connections the
	 * {@code PolinuxHttpServer} can receive at once.
	 */
	static final int BACKLOG = PolinuxHttpServerConfiguration.DEFAULT_BACKLOG;

	/**
	 * The name of the Polinux Server (static, not instance wise).
	 */
	private static final String NAME = "Polinux";

	/**
	 * The version of the Polinux Server (static, not instance wise).
	 */
	private static final double VERSION = 1.0;

	/**
	 * Represents the {@link PolinuxHttpServerConfiguration configuration file} for
	 * this {@code Polinux HTTP server}.
	 */
	protected PolinuxHttpServerConfiguration config;

	/**
	 * Represents the {@link PolinuxWebApplication web application} currently known
	 * to the server.
	 */
	protected Set<PolinuxWebApplication> apps;

	/**
	 * Represents whether the {@code Polinux HTTP server} has been initialized
	 * (whether {@link #init() has been called})
	 */
	protected volatile boolean initialized = false;

	/* ---------------------- OLD CONSTRUCTORS ---------------------- */

//	/**
//	 * Constructs a {@code Polinux HTTP Server} with the {@link #DEFAULT_HOST} and
//	 * {@link #DEFAULT_PORT}
//	 * <p>
//	 * Note that the constructor alone will not start this server. {@link #run()}
//	 * must explicitly be called to start the server.
//	 * 
//	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
//	 *                                           the values of {@code null}. This
//	 *                                           will also be thrown if the port is
//	 *                                           out of range (0-65535, inclusive)
//	 *                                           or if the address is already bound.
//	 */
//	public PolinuxHttpServer() throws PolinuxHttpServerRuntimeException {
//		this(DEFAULT_HOST, DEFAULT_PORT);
//	}
//
//	/**
//	 * Constructs a {@code Polinux HTTP Server} with the underlying host and the
//	 * {@link #DEFAULT_PORT}. Note that this constructor alone will not start the
//	 * server. {@link #run()} must explicitly be called to start the server.
//	 * 
//	 * @param host The host for the server. Cannot be {@code null}.
//	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
//	 *                                           the values of {@code null}. This
//	 *                                           will also be thrown if the port is
//	 *                                           out of range (0-65535, inclusive)
//	 *                                           or if the address is already bound.
//	 */
//	public PolinuxHttpServer(String host) throws PolinuxHttpServerRuntimeException {
//		this(host, DEFAULT_PORT);
//	}
//
//	/**
//	 * Constructs a {@code Polinux HTTP Server} with the underlying port and the
//	 * {@link #DEFAULT_HOST}. Note that this constructor alone will not start the
//	 * server. {@link #run()} must explicitly be called to start the server.
//	 * 
//	 * @param port The port for the server. Cannot be {@code null}.
//	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
//	 *                                           the values of {@code null}. This
//	 *                                           will also be thrown if the port is
//	 *                                           out of range (0-65535, inclusive)
//	 *                                           or if the address is already bound.
//	 */
//	public PolinuxHttpServer(int port) throws PolinuxHttpServerRuntimeException {
//		this(DEFAULT_HOST, port);
//	}
//
//	/**
//	 * Constructs a {@code Polinux HTTP Server} with the underlying host and port.
//	 * Note that this constructor alone will not start the server. {@link #run()}
//	 * must explicitly be called to start the server.
//	 * 
//	 * @param host The host for the server. Cannot be {@code null}.
//	 * @param port The port for the server. Cannot be {@code null}.
//	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
//	 *                                           the values of {@code null}. This
//	 *                                           will also be thrown if the port is
//	 *                                           out of range (0-65535, inclusive)
//	 *                                           or if the address is already bound.
//	 */
//	public PolinuxHttpServer(String host, int port) throws PolinuxHttpServerRuntimeException {
//		this.host = host;
//		this.port = port;
//		ensureValidHost();
//		ensureValidPort();
//
//		this.thread = new PolinuxHttpServerThread(this);
//		File configFile = new File(PolinuxServerConfiguration.CONFIGURATION_PATH);
//		if (!configFile.exists()) {
//			if (!configFile.getParentFile().exists())
//				configFile.getParentFile().mkdirs();
//
//			InputStream fileIn = this.getClass().getResourceAsStream("/server/configuration.yml");
//
//			try {
//				this.saveResource(fileIn, configFile, CharacterSet.defaultCharset());
//			} catch (IOException e) {
//				throw new PolinuxHttpServerRuntimeException(this, e.getMessage(), e);
//			}
//		}
//
//		this.setConfiguration(new PolinuxServerConfiguration(configFile));
//
//	}

	/**
	 * Constructs a {@code Polinux HTTP Server} with the
	 * {@link PolinuxHttpServerConfiguration} being at the
	 * {@link PolinuxHttpServerConfiguration#CONFIGURATION_PATH default
	 * configuration path}. Note that this constructor alone will not start the
	 * server. {@link #run()} must explicitly be called to start the server.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
	 *                                           the values of {@code null}. This
	 *                                           will also be thrown if the port is
	 *                                           out of range (0-65535, inclusive)
	 *                                           or if the address is already bound.
	 * @see #PolinuxHttpServer(PolinuxHttpServerConfiguration)
	 */
	public PolinuxHttpServer() {
		this(PolinuxHttpServerConfiguration
				.writeDefaultConfig(new File(PolinuxHttpServerConfiguration.CONFIGURATION_PATH.replace("\\", "/"))));
	}

	/**
	 * Constructs a {@code Polinux HTTP Server} with the underlying configuration
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
	 * @see #PolinuxHttpServer(PolinuxHttpServerConfiguration)
	 */
	public PolinuxHttpServer(File config) throws PolinuxHttpServerRuntimeException {
		this(new PolinuxHttpServerConfiguration(config));
	}

	/**
	 * Constructs a {@code Polinux HTTP Server} with the underlying configuration
	 * file. Note that this constructor alone will not start the server.
	 * {@link #run()} must explicitly be called to start the server.
	 * 
	 * @param config The {@link PolinuxHttpServerConfiguration} for this server
	 *               Cannot be {@code null}.
	 * @throws PolinuxHttpServerRuntimeException If either the host or the port have
	 *                                           the values of {@code null}. This
	 *                                           will also be thrown if the port is
	 *                                           out of range (0-65535, inclusive)
	 *                                           or if the address is already bound.
	 */
	public PolinuxHttpServer(PolinuxHttpServerConfiguration config) throws PolinuxHttpServerRuntimeException {
		this(config, true);
	}

	/**
	 * Constructs a {@code Polinux HTTP Server} with the underlying configuration
	 * file. Note that this constructor alone will not start the server.
	 * {@link #run()} must explicitly be called to start the server.
	 * 
	 * @param config     The {@link PolinuxHttpServerConfiguration} for this server
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
	protected PolinuxHttpServer(PolinuxHttpServerConfiguration config, boolean initThread)
			throws PolinuxHttpServerRuntimeException {

		File configFile = config.getFile();

		if (configFile != null) {
			if (!configFile.exists()) {
				PolinuxHttpServerConfiguration.writeDefaultConfig(configFile);
			}
		}

		if (!config.isUseHttp()) {
			throw new PolinuxHttpServerRuntimeException(this,
					"PolinuxHttpServerConfiguration must accept HTTP on a PolinuxHttpServer");
		}

		this.setConfiguration(config);

		ensureValidHost();
		ensureValidPort();

		this.commandManager = new PolinuxCommandManager(this);

		if (initThread)
			this.thread = new PolinuxHttpServerThread(this);
	}

	/**
	 * Starts the server. It is preferable to make sure all {@code credentials} need
	 * for the server to start are correct before running this method.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If an error occurs in the server.
	 */
	public synchronized void run() throws PolinuxHttpServerRuntimeException {
		thread.execute();
		if (this.commandManager != null)
			this.commandManager.execute();
	}

	/**
	 * Terminates the server.
	 * <p>
	 * Execution of this method may cause some errors inside code of the server
	 * (e.g. responses and request are immediately canceled), so it is a good idea
	 * to make sure everything is safe and can be closed before the execution of
	 * this method.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If an error occurs in the server.
	 */
	public synchronized void shutdown() throws PolinuxHttpServerRuntimeException {
		thread.close();
	}

	/**
	 * Retrieves the host of the current server. This value cannot be changed, and
	 * new {@link PolinuxHttpServer} will have to be created with a new host if a
	 * change is wished.
	 * 
	 * @return The host for this server, never {@code null}
	 * @throws PolinuxHttpServerRuntimeException If the server's host is equal to
	 *                                           {@code null} or is invalid.
	 *                                           (unlikely).
	 */
	public String getHost() throws PolinuxHttpServerRuntimeException {
		ensureValidHost();
		return this.getConfiguration().getHost();
	}

	/**
	 * Retrieves the port of the current server. This value cannot be changed, and
	 * new {@link PolinuxHttpServer} will have to be created with a new port if a
	 * change is wished.
	 * 
	 * @return The port for this server, never {@code null}
	 * @throws PolinuxHttpServerRuntimeException If the server's port is equal to
	 *                                           {@code null} or is invalid.
	 *                                           (unlikely).
	 */
	public int getPort() throws PolinuxHttpServerRuntimeException {
		ensureValidPort();
		return this.getConfiguration().getPort();
	}

	/**
	 * Ensures valid host
	 */
	protected void ensureValidHost() throws PolinuxHttpServerRuntimeException {
		if (this.getConfiguration() == null || this.getConfiguration().getHost() == null) {
			throw new PolinuxHttpServerRuntimeException(this, "Invalid host for server!");
		}
	}

	/**
	 * Ensures valid port
	 */
	protected void ensureValidPort() throws PolinuxHttpServerRuntimeException {
		if (this.getConfiguration() == null || this.getConfiguration().getPort() < PORT_MIN
				|| this.getConfiguration().getPort() > PORT_MAX) {
			throw new PolinuxHttpServerRuntimeException(this, "Invalid port for server!");
		}
	}

	/**
	 * Retrieves the {@code Polinux HTTP Server's} {@link org.polinux.logging.Logger
	 * logger}. Does not return {@code null}.
	 * 
	 * @return The {@link org.polinux.logging.Logger logger} for this
	 *         {@code Server}, never {@code null}.
	 */
	public org.polinux.logging.Logger getLogger() {
		return HTTP_LOGGER;
	}

	/**
	 * Retrieves the name of the Polinux Server, that may be used inside the Http
	 * Header "<i>Server:</i>".
	 * 
	 * @return The "name" of the Polinux Http Server ({@link #NAME} + "/" +
	 *         {@link #getVersion() VERSION}).
	 */
	@Override
	public String getName() {
		return NAME + "/" + Double.toString(VERSION);
	}

	/**
	 * Retrieves the version of the Polinux Server, that may be used inside the Http
	 * Header "<i>Server:</i>".
	 * 
	 * @return The version of the Polinux Http Server.
	 */
	@Override
	public double getVersion() {
		return VERSION;
	}

	/**
	 * Sets the {@link PolinuxHttpServerConfiguration} for this
	 * {@code PolinuxHttpServer}. This method should only be called {@code once} and
	 * should not be changed after it is set.
	 * 
	 * @param config The new {@link PolinuxHttpServerConfiguration}.
	 */
	protected void setConfiguration(PolinuxHttpServerConfiguration config) {
		this.config = config;
	}

	/**
	 * Retrieves the {@link PolinuxHttpServerConfiguration configuration} for the
	 * current {@code PolinuxHttpServer}.
	 * 
	 * @return The configuration file for the server. Probably not {@code null}.
	 * 
	 */
	@Override
	public PolinuxHttpServerConfiguration getConfiguration() throws PolinuxHttpServerRuntimeException {
		if (this.config == null)
			throw new PolinuxHttpServerRuntimeException(this,
					"Configuration for PolinuxHttpServer cannot be found (null?)");
		return this.config;
	}

	/**
	 * Initializes the {@code PolinuxHttpServer}. This method is {@code required}
	 * and must be called before {@link #run()} in order for the server to work
	 * properly.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If an error occurs while
	 *                                           initializing the server.
	 * @see #init(HttpServerConfiguration)
	 */
	@Override
	public void init() throws PolinuxHttpServerRuntimeException {
		init(this.config);
	}

	/**
	 * Initializes the {@code PolinuxHttpServer}. This method is {@code required}
	 * and must be called before {@link #run()} in order for the server to work
	 * properly.
	 * 
	 * @param configuration The configuration for the server.
	 * @throws PolinuxHttpServerRuntimeException If an error occurs while
	 *                                           initializing the server.
	 */
	@Override
	public void init(final HttpServerConfiguration configuration) throws PolinuxHttpServerRuntimeException {
		if (configuration != null && !(configuration instanceof PolinuxHttpServerConfiguration))
			throw new PolinuxHttpServerRuntimeException(this,
					"Polinux HTTP Server only accepts PolinuxHttpServerConfiguration");
		if (this.config != configuration)
			this.config = configuration == null ? null : (PolinuxHttpServerConfiguration) configuration;

		if (this.config != null) {
			String webRoot = this.config.getWebsiteRoot();

			if (webRoot == null)
				webRoot = PolinuxHttpServerConfiguration.DEFAULT_WEB_ROOT;

			File webRootFile = new File(webRoot);

			if (!webRootFile.exists()) {
				webRootFile.mkdirs();
			} else if (webRootFile.isFile()) {
				throw new PolinuxHttpServerRuntimeException(this,
						"Cannot create web root in " + webRoot + " because a file version already exists!");
			}

			this.apps = loadApps(webRootFile);

		}
	}

	/**
	 * Loads the {@link PolinuxWebApplication PolinuxWebApplications} from the
	 * underlying folder.
	 * 
	 * @param websiteRoot The folder from where to load the applications.
	 * @return A {@link Set} containg the loaded applications.
	 */
	private synchronized Set<PolinuxWebApplication> loadApps(File websiteRoot) {
		if (!websiteRoot.exists())
			websiteRoot.mkdirs();
		final PolinuxWebApplicationFolderLoader loader = PolinuxWebApplicationFolderLoader
				.getPolinuxWebApplicationFolderLoader(getName());
		loader.unload();
		return CollectionUtils.toSet(loader.loadDirectory(websiteRoot).getWebApplications());
	}

	/**
	 * Retrieves all the {@link PolinuxWebApplication PolinuxWebApplications} known
	 * to the {@code HTTP server}.
	 * 
	 * @return All the known {@link PolinuxWebApplication PolinuxWebApplications}.
	 *         Will return an {@code empty set} if there are none, not {@code null}.
	 */
	@Override
	public Set<? extends PolinuxWebApplication> getWebApplications() {
		ensureValidApps();
		return this.apps;
	}

	/* Ensures the web app list is valid. */
	private void ensureValidApps() {
		if (this.apps == null) {
			this.apps = new LinkedHashSet<PolinuxWebApplication>();
		}
	}

	/**
	 * Retrieves the {@code backlog} for the {@code Polinux HTTP server}.
	 * 
	 * @return How many {@link Socket connections} the {@code Polinux HTTP server}
	 *         can handle at once.
	 * @throws throws PolinuxHttpServerRuntimeException
	 */
	@Override
	public int getBacklog() throws PolinuxHttpServerRuntimeException {
		return this.getConfiguration().getBacklog();
	}

	/**
	 * Retrieves the {@link PolinuxHttpServerThread} linked to this
	 * {@code Polinux HTTP server} thread. This method may return {@code null} if
	 * the server runs without of a thread (not recommended, not likely).
	 * 
	 * @return The linked {@link PolinuxHttpServerThread}.
	 */
	@Override
	public PolinuxHttpServerThread getServerThread() {
		return this.thread;
	}

	/**
	 * Retrieves the {@link ServerType} for the {@code Polinux HTTP server}.
	 * 
	 * @return The {@code Polinux HTTP server}'s server type.
	 */
	@Override
	public ServerType getServerType() {
		return ServerType.HTTP;
	}

	/**
	 * Retrieves the {@link PolinuxCommandManager} for the current
	 * {@code Polinux HTTP server}. This class can be used to add any
	 * {@link PolinuxCommand PolinuxCommands} to the list of known commands.
	 * 
	 * @return The server's command manager.
	 */
	public PolinuxCommandManager getCommandManager() {
		return this.commandManager;
	}

	/**
	 * Sets the {@link PolinuxCommandManager} for the current
	 * {@code Polinux HTTP server}.
	 * 
	 * @param commandManager The server's new command manager. Cannot be
	 *                       {@code null}.
	 */
	public void setCommandManager(PolinuxCommandManager commandManager) {
		if (commandManager != null)
			this.commandManager = commandManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void abort() throws HttpRuntimeException {
		this.shutdown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void close() throws HttpRuntimeException {
		this.abort();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void execute() throws HttpRuntimeException {
		this.run();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		return this.getServerThread().isEnabled();
	}

}
