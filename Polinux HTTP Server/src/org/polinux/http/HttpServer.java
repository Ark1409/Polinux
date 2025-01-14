package org.polinux.http;

import java.net.Socket;
import java.util.Set;

import org.polinux.configuration.server.HttpServerConfiguration;
import org.polinux.exceptions.http.HttpRuntimeException;
import org.polinux.exceptions.http.HttpServerRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServer;
import org.polinux.https.HttpsServer;
import org.polinux.web.WebApplication;

/**
 * Represents an {@code HTTP Server}, not necessarily made by the
 * {@code Polinux} API. This interface is used as an representative instance of
 * an actual "server" linked to a specified host and port.
 * 
 * @see HttpsServer
 * @see PolinuxHttpServer
 */
public interface HttpServer extends HttpThread {
	/**
	 * The Http Version of all {@code Http Servers}. This is a must.
	 */
	public static final String HTTP_VERSION = "HTTP/1.1";

	/**
	 * Initializes the {@code HTTP Server}, if needed.
	 * 
	 * @throws HttpServerRuntimeException If an error occurs while initializing the
	 *                                    server.
	 */
	public void init() throws HttpServerRuntimeException;

	/**
	 * Initializes the {@code HTTP Server}, if needed.
	 * 
	 * @param configuration The configuration for the server.
	 * @throws HttpServerRuntimeException If an error occurs while initializing the
	 *                                    server.
	 */
	public void init(final HttpServerConfiguration configuration) throws HttpServerRuntimeException;

	/**
	 * Retrieves the name of the {@code Http Server}, that may be used inside the
	 * Http Header "<i>Server:</i>".
	 * 
	 * @return The "name" of the {@code Http Server} ({@link #NAME} + "/" +
	 *         {@link #VERSION}).
	 */
	public String getName();

	/**
	 * Retrieves the {@link HttpServerConfiguration configuration} for the current
	 * {@code HttpServer}.
	 * 
	 * @return The configuration file for the server. Probably not {@code null}.
	 * @throws HttpServerRuntimeException If the configuration cannot be found.
	 */
	public HttpServerConfiguration getConfiguration() throws HttpServerRuntimeException;

	/**
	 * Retrieves the host of the current server. This value cannot be changed, and
	 * new {@link HttpServer} will have to be created with a new host if a change is
	 * wished.
	 * 
	 * @return The host for this server, never {@code null}
	 * @throws HttpServerRuntimeException If the server's host is equal to
	 *                                    {@code null} or is invalid. (unlikely).
	 */
	public String getHost() throws HttpServerRuntimeException;

	/**
	 * Retrieves the port of the current server. This value cannot be changed, and
	 * new {@link HttpServer} will have to be created with a new port if a change is
	 * wished.
	 * 
	 * @return The port for this server, never {@code null}
	 * @throws HttpServerRuntimeException If the server's port is equal to
	 *                                    {@code null} or is invalid. (unlikely).
	 */
	public int getPort() throws HttpServerRuntimeException;

	/**
	 * Starts the server. It is a good idea to make sure all {@code credentials}
	 * need for the server to start are correct before running this method.
	 * 
	 * @see #run()
	 */
	@Override
	public default void execute() throws HttpRuntimeException {
		this.run();
	}

	/**
	 * Starts the server. It is a good idea to make sure all {@code credentials}
	 * need for the server to start are correct before running this method.
	 * 
	 * @throws HttpServerRuntimeException If an error in the server occurs.
	 */
	public void run() throws HttpServerRuntimeException;

	/**
	 * Terminates the server.
	 * <p>
	 * Execution of this method may cause some errors inside code of the server
	 * (e.g. responses and request are immediately canceled), so it is a good idea
	 * to make sure everything is safe and can be closed before the execution of
	 * this method.
	 * 
	 * @throws HttpServerRuntimeException If an error occurs in the server.
	 */
	public void shutdown() throws HttpServerRuntimeException;

	/**
	 * Terminates the server.
	 * <p>
	 * Execution of this method may cause some errors inside code of the server
	 * (e.g. responses and request are immediately canceled), so it is a good idea
	 * to make sure everything is safe and can be closed before the execution of
	 * this method.
	 * 
	 * @see #shutdown()
	 */
	@Override
	public default void abort() throws HttpRuntimeException {
		this.shutdown();
	}

	/**
	 * Terminates the server.
	 * <p>
	 * Execution of this method may cause some errors inside code of the server
	 * (e.g. responses and request are immediately canceled), so it is a good idea
	 * to make sure everything is safe and can be closed before the execution of
	 * this method.
	 * 
	 * @see #shutdown()
	 */
	@Override
	public default void close() throws HttpRuntimeException {
		this.abort();
	}

	/**
	 * Retrieves the version of the {@code Http Server}, that may be used inside the
	 * Http Header "<i>Server:</i>".
	 * 
	 * @return The "version" of the {@code Http Server}.
	 */
	public double getVersion();

	/**
	 * Retrieves the Logger for the {@code Http Server}. The server may return
	 * {@code null} if the server is specified to not have a
	 * {@link org.polinux.logging.Logger logger} linked to it.
	 * 
	 * @return The {@link org.polinux.logging.Logger logger} for this
	 *         {@code Http Server}, may be {@code null}.
	 */
	public org.polinux.logging.Logger getLogger();

	/**
	 * Retrieves all the {@link WebApplication} known to the {@code HTTP server}.
	 * 
	 * @return All the known {@link WebApplication ebApplications}. Will return an
	 *         {@code empty set} if there are none, not {@code null}.
	 */
	public Set<? extends WebApplication> getWebApplications();

	/**
	 * Retrieves the {@code backlog} for the {@code HTTP server}.
	 * 
	 * @return How many {@link Socket connections} the {@code HTTP server} can
	 *         handle at once.
	 */
	public int getBacklog();

	/**
	 * Retrieves the {@link HttpServerThread} linked to this {@code HTTP server}
	 * thread. This method may return {@code null} if the server runs without of a
	 * thread (not recommended, not likely).
	 * 
	 * @return The linked {@link HttpServerThread}.
	 */
	public HttpServerThread getServerThread();

	/**
	 * Retrieves the {@link ServerType} for the {@code HTTP server}.
	 * 
	 * @return The {@code HTTP server}'s server type.
	 */
	public ServerType getServerType();

	/**
	 * Represents a Server Type.
	 * 
	 * @see HttpServer
	 * @see HttpsServer
	 */
	public static enum ServerType {
		/**
		 * Represents a server that supports HTTP
		 */
		HTTP,

		/**
		 * Represents a server that supports HTTPS
		 */
		HTTPS,

		/**
		 * Represents a server that supports HTTP & HTTPS
		 */
		HTTP_HTTPS;
	}
}
