package org.polinux.http.polinux.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.http.HttpClientThread;
import org.polinux.http.HttpServerThread;
import org.polinux.https.polinux.server.PolinuxHttpsServerThread;

/**
 * Represents the thread of a {@link PolinuxHttpServer} that handles incoming
 * connections. This class should be used in {@link PolinuxHttpServer}, and no
 * where else unless absolutely necessary.
 * <p>
 * A few notes when using this class (if needed):
 * <ul>
 * <li>{@link #run()} should not be called when running the sever. Instead, use
 * {@link #execute()}.
 * <li>The {@link #server PolinuxHttpServer} for this server thread cannot be
 * equal to {@code null}, for it is need throughout the code for this server
 * thread.
 * </ul>
 * 
 * @see PolinuxHttpServer
 * @see PolinuxHttpsServerThread
 */
@SuppressWarnings({ "deprecation" })
public class PolinuxHttpServerThread implements HttpServerThread {

	/**
	 * The {@link java.lang.Thread} linked to this server thread.
	 */
	protected Thread t;

	/**
	 * The {@link java.net.ServerSocket} for this server thread. Should not be
	 * touched and is handled by this class.
	 */
	protected transient ServerSocket serverSocket;

	/**
	 * The {@link PolinuxHttpServer} linked to this server thread. Cannot be changed
	 * ({@code final}).
	 */
	protected transient final PolinuxHttpServer server;

	/**
	 * Whether the thread is currently enabled.
	 */
	protected volatile boolean enabled = false;

	/**
	 * Represents a list containing all client threads.
	 */
	protected transient volatile List<PolinuxHttpClientThread> clientThreads = new LinkedList<PolinuxHttpClientThread>();

	/**
	 * Constructs a {@code Polinux HTTP Server Thread}. Note that this constructor
	 * alone will not start the server. {@link #execute()} must explicitly be called
	 * to start the server.
	 * 
	 * @param server The PolinuxHttpServer this thread is running off.
	 * @throws PolinuxHttpServerRuntimeException If the underlying
	 *                                           {@link PolinuxHttpServer} is equal
	 *                                           to {@code null} or if an I/O error
	 *                                           occurs when opening the server.
	 */
	protected PolinuxHttpServerThread(final PolinuxHttpServer server) throws PolinuxHttpServerRuntimeException {
		this(server, true);
	}

	/**
	 * Constructs a {@code Polinux HTTP Server Thread}. Note that this constructor
	 * alone will not start the server. {@link #execute()} must explicitly be called
	 * to start the server.
	 * 
	 * @param server     The PolinuxHttpServer this thread is running off.
	 * @param initServer Whether to initialize the {@link #getServerSocket() server
	 *                   socket} for the server. Set to {@code false} if the
	 *                   {@link ServerSocket} initialization will be handled by a
	 *                   sub-class.
	 * @throws PolinuxHttpServerRuntimeException If the underlying
	 *                                           {@link PolinuxHttpServer} is equal
	 *                                           to {@code null} or if an I/O error
	 *                                           occurs when opening the server.
	 */
	protected PolinuxHttpServerThread(final PolinuxHttpServer server, final boolean initServer) {
		this.server = server;
		ensureValidServer();
		if (initServer) {
			try {
				// Creates server with host, port and backlog (backlog = how many connection the
				// server can handle at once)
				serverSocket = new ServerSocket(server.getPort(), server.getBacklog(),
						InetAddress.getByName(server.getHost()));
			} catch (IOException e) {
				throw new PolinuxHttpServerRuntimeException(this.server, e.getMessage(), e);
			}
		}
	}

	/**
	 * Ensures the server instance is valid.
	 * 
	 * @throws PolinuxHttpServerRuntimeException Thrown if the server instance is
	 *                                           currently not valid.
	 */
	private void ensureValidServer() throws PolinuxHttpServerRuntimeException {
		if (this.server == null) {
			throw new PolinuxHttpServerRuntimeException(null,
					"Cannot create PolinuxHttpServerThread with null PolinuxHttpServer");
		}
	}

	/**
	 * Starts the server thread.
	 * <p>
	 * Execution of this method is preferred over {@link #run()}, for this method
	 * prepare all necessary items before execution of the thread.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If the server has already been
	 *                                           started.
	 */
	public synchronized void execute() throws PolinuxHttpServerRuntimeException {
		if (t != null) {
			throw new PolinuxHttpServerRuntimeException(this.server,
					"Cannot start two instances of the same PolinuxHttpServerThread");
		}
		t = new Thread(this);
		enabled = true;
		t.start();
	}

	/**
	 * 
	 * Invokes the run method. {@link #execute()} should be invoked rather than this
	 * method, since the starts the server "raw-ly" (a new thread is not initiated).
	 * 
	 * @deprecated {@link #execute()} should be used instead.
	 */
	@Deprecated
	@Override
	public void run() {

		this.logStarted();

		while (this.isEnabled()) {
			try {
				// Accept client
				final Socket socket = serverSocket.accept();

				// Log client
				this.server.getLogger()
						.log("Client accessing HTTP server: (" + socket.getInetAddress().getHostName() + ") "
								+ socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort()
								+ " (remote port " + socket.getPort() + " )");

				// Handle request on different thread
				final PolinuxHttpClientThread clientThread = new PolinuxHttpClientThread(this, socket);
				clientThreads.add(clientThread);

				// Execute client thread
				clientThread.execute();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Logs to the {@link #server server's} logger that the server thread has
	 * started.
	 */
	protected void logStarted() {
		this.server.getLogger()
				.log("Server Started | Host: " + this.server.getHost() + " | Port: " + this.server.getPort());
		this.server.getLogger().flush();
	}

	/**
	 * Closes the current server thread. After execution of this method, this class
	 * may be disposed of (should <i>never</i> be used again).
	 * 
	 * @throws PolinuxHttpServerRuntimeException if an I/O error occurs when closing
	 *                                           the socket
	 */
	public synchronized void close() throws PolinuxHttpServerRuntimeException {
		this.abort();
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new PolinuxHttpServerRuntimeException(this.server, e.getMessage(), e);
		}
	}

	/**
	 * Terminates the server thread.
	 * <p>
	 * Execution of this method may cause some errors inside code of the server
	 * (e.g. responses and request are immediately canceled), so it is a good idea
	 * to make sure everything is safe and can be closed before the execution of
	 * this method.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If the server has not yet been
	 *                                           started.
	 */
	public synchronized void abort() throws PolinuxHttpServerRuntimeException {
		if (t == null) {
			throw new PolinuxHttpServerRuntimeException(this.server,
					"Cannot abort PolinuxHttpServerThread that has not started!");
		}
		this.enabled = false;
		t.stop();
		t = null;
	}

	/**
	 * Gets whether the server thread is currently enabled.
	 * 
	 * @return {@code True} if the server thread is currently enabled, {@code false}
	 *         otherwise.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Retrieves the {@link PolinuxHttpServer} linked to this server thread.
	 * 
	 * @return The linked {@link PolinuxHttpServer}.
	 */
	public PolinuxHttpServer getServer() {
		return this.server;
	}

	/**
	 * Retrieves the {@link ServerSocket} created with the {@link #getServer()
	 * server} linked to this server thread.
	 * 
	 * @return The linked {@link ServerSocket}.
	 */
	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}

	/**
	 * Retrieves the list of all {@link HttpClientThread HttpClientThreads} that
	 * have connected to the server thread throughout its lifespan.
	 * 
	 * @return All the known {@link HttpClientThread HttpClientThreads}.
	 */
	@Override
	public PolinuxHttpClientThread[] getClientThreads() {
		return this.clientThreads.toArray(new PolinuxHttpClientThread[0]);
	}

}
