package org.polinux.http;

import java.net.Socket;

import org.polinux.exceptions.http.HttpServerRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServerThread;
/**
 * Represents a thread related to an {@code HTTP client}.
 * @see PolinuxHttpClientThread
 */
public interface HttpClientThread extends HttpThread {
	/**
	 * Starts the client thread.
	 * <p>
	 * Execution of this method is preferred over {@link #run()}, for this method
	 * prepare all necessary items before execution of the thread.
	 * 
	 * @throws HttpServerRuntimeException If the server has already been started.
	 */
	public void execute() throws HttpServerRuntimeException;

	/**
	 * Terminates the client thread.
	 * <p>
	 * Execution of this method may cause some errors inside code of the
	 * {@link #getServerThread() server} (e.g. responses and request are immediately
	 * canceled), so it is a good idea to make sure everything is safe and can be
	 * closed before the execution of this method.
	 * 
	 * @throws HttpServerRuntimeException If the server has not yet been started.
	 */
	public void abort() throws HttpServerRuntimeException;

	/**
	 * Closes the current client thread. After execution of this method, this class
	 * may be disposed of (should <i>never</i> be used again).
	 * 
	 * @throws HttpServerRuntimeException if an I/O error occurs when closing the
	 *                                    socket
	 */
	public void close() throws HttpServerRuntimeException;

	/**
	 * Retrieves the {@link Socket} created with the {@link #getServerThread()
	 * server thread} linked to this client thread.
	 * 
	 * @return The linked {@link Socket}.
	 */
	public Socket getSocket();

	/**
	 * Retrieves the {@link HttpServerThread} linked to this client thread. This
	 * method may return {@code null} if the thread runs without of a server (not
	 * recommended, not likely).
	 * 
	 * @return The linked {@link HttpServerThread}.
	 */
	public PolinuxHttpServerThread getServerThread();
}
