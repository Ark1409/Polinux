package org.polinux.http;

import java.net.ServerSocket;

import org.polinux.exceptions.http.HttpServerRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServerThread;

/**
 * Represents the main thread for an {@link HttpServer}.
 * 
 * @see PolinuxHttpServerThread
 */
public interface HttpServerThread extends HttpThread {
	/**
	 * Terminates the server thread.
	 * <p>
	 * Execution of this method may cause some errors inside code of the server
	 * (e.g. responses and request are immediately canceled), so it is a good idea
	 * to make sure everything is safe and can be closed before the execution of
	 * this method.
	 * 
	 * @throws HttpServerRuntimeException If the server has not yet been started.
	 */
	public void abort() throws HttpServerRuntimeException;

	/**
	 * Closes the current server thread. After execution of this method, this class
	 * may be disposed of (should <i>never</i> be used again).
	 * 
	 * @throws HttpServerRuntimeException if an I/O error occurs when closing the
	 *                                    socket
	 */
	public void close() throws HttpServerRuntimeException;

	/**
	 * Starts the server thread.
	 * <p>
	 * Execution of this method is preferred over {@link #run()}, for this method
	 * prepare all necessary items before execution of the thread.
	 * 
	 * @throws HttpServerRuntimeException If the server has already been started.
	 */
	public void execute() throws HttpServerRuntimeException;

	/**
	 * Gets whether the server thread is currently enabled.
	 * 
	 * @return {@code True} if the server thread is currently enabled, {@code false}
	 *         otherwise.
	 */
	public boolean isEnabled();

	/**
	 * Retrieves the {@link HttpServer} linked to this server thread. This method
	 * may return {@code null} if the thread runs without of a server (not
	 * recommended, not likely).
	 * 
	 * @return The linked {@link HttpServer}.
	 */
	public HttpServer getServer();

	/**
	 * Retrieves the {@link ServerSocket} created with the {@link #getServer()
	 * server} linked to this server thread.
	 * 
	 * @return The linked {@link ServerSocket}.
	 */
	public ServerSocket getServerSocket();

	/**
	 * Retrieves the list of all {@link HttpClientThread HttpClientThreads} that
	 * have connected to the server thread throughout its lifespan.
	 * 
	 * @return All the known {@link HttpClientThread HttpClientThreads}.
	 */
	public HttpClientThread[] getClientThreads();
}
