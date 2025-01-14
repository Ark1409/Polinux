package org.polinux.https.polinux.server;

import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.exceptions.https.polinux.PolinuxHttpsServerRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpClientThread;

/**
 * Represents the thread of a connection (or client) inside a
 * {@link PolinuxHttpsServerThread}. This class should be used in
 * {@link PolinuxHttpsServerThread}, and no where else unless absolutely
 * necessary.
 * <p>
 * A few notes when using this class (if needed):
 * <ul>
 * <li>{@link #run()} should not be called when running the sever. Instead, use
 * {@link #execute()}.
 * <li>The {@link #getServerThread() PolinuxHttpsServerThread} for this server
 * thread cannot be equal to {@code null}, for it is need throughout the code
 * for this server thread.
 * </ul>
 * 
 * @see PolinuxHttpClientThread
 * @see PolinuxHttpsServerThread
 */
public class PolinuxHttpsClientThread extends PolinuxHttpClientThread {

	/**
	 * Constructs a {@code Polinux HTTPS Client Thread}. Note that this constructor
	 * alone will not start the thread. {@link #execute()} must explicitly be called
	 * to start the server.
	 * 
	 * @param serverThread The {@link PolinuxHttpsServerThread} this client thread
	 *                     is running off.
	 * @param socket       The {@link SSLSocket} linked to this client.
	 * @throws PolinuxHttpServerRuntimeException If the underlying
	 *                                           {@link PolinuxHttpsServerThread} is
	 *                                           equal to {@code null} or if an I/O
	 *                                           error occurs when opening the
	 *                                           server.
	 */
	protected PolinuxHttpsClientThread(PolinuxHttpsServerThread serverThread, SSLSocket socket)
			throws PolinuxHttpServerRuntimeException {
		super(serverThread, socket);
		this.logger = PolinuxHttpsServer.HTTPS_LOGGER;
		// this.t = new Thread(this);
	}

	/**
	 * Retrieves the {@link SSLSocket} created with the {@link #getServerThread()
	 * server thread} linked to this client thread.
	 * 
	 * @return The linked {@link SSLSocket}.
	 */
	@Override
	public SSLSocket getSocket() {
		return (SSLSocket) super.getSocket();
	}

	/**
	 * Retrieves the {@link PolinuxHttpsServerThread} linked to this {@code secure}
	 * client thread. This method may return {@code null} if the thread runs without
	 * of a server (not recommended, not likely).
	 * 
	 * @return The linked {@link PolinuxHttpsServerThread}.
	 */
	@Override
	public PolinuxHttpsServerThread getServerThread() {
		return (PolinuxHttpsServerThread) super.getServerThread();
	}

	/**
	 * Starts the client thread.
	 * <p>
	 * Execution of this method is preferred over {@link #run()}, for this method
	 * prepare all necessary items before execution of the thread.
	 * 
	 * @throws PolinuxHttpsServerRuntimeException If the server has already been
	 *                                            started.
	 */
	@Override
	public synchronized void execute() throws PolinuxHttpsServerRuntimeException {
		if (t != null) {
			throw new PolinuxHttpsServerRuntimeException(this.getServerThread().getServer(),
					"Cannot start two instances of the same PolinuxHttpsClientThread");
		}
		t = new Thread(this);
		enabled = true;
		t.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public void run() {
		ensureValidSocket();
		ensureValidServer();
		super.run();
	}

	/**
	 * Retrieves the socket HTTPS request.
	 * 
	 * @param socketArg The {@link SSLSocket} through whom the request will be read.
	 */
	@Override
	protected String getRequest(Socket socketArg) throws Exception {
		SSLSocket socket = (SSLSocket) socketArg;

		final InputStream in = socket.getInputStream();
		String browserRequest;

		int firstChar;

		while ((firstChar = in.read()) == -1) {
		}

		browserRequest = String.valueOf((char) firstChar);

		while (in.available() > 0) {
			int amount = in.available();
			byte[] buffer = new byte[amount];
			final int len = in.read(buffer);
			browserRequest += new String(buffer, 0, len);
		}

		return browserRequest;
	}

	/**
	 * @throws PolinuxHttpsServerRuntimeException If the socket is not valid.
	 */
	@Override
	protected void ensureValidSocket() throws PolinuxHttpsServerRuntimeException {
		try {
			super.ensureValidSocket();
		} catch (PolinuxHttpServerRuntimeException e) {
			throw new PolinuxHttpsServerRuntimeException(this.getServerThread().getServer(),
					e.getMessage().replace("PolinuxHttpClientThread", "PolinuxHttpsClientThread")
							.replace("PolinuxHttpServerThread", "PolinuxHttpsServerThread"),
					e);
		}
	}

	/**
	 * @throws PolinuxHttpsServerRuntimeException If the server thread is not valid.
	 */
	@Override
	protected void ensureValidServer() throws PolinuxHttpsServerRuntimeException {
		try {
			super.ensureValidServer();
		} catch (PolinuxHttpServerRuntimeException e) {
			throw new PolinuxHttpsServerRuntimeException(this.getServerThread().getServer(),
					e.getMessage().replace("PolinuxHttpClientThread", "PolinuxHttpsClientThread")
							.replace("PolinuxHttpServerThread", "PolinuxHttpsServerThread"),
					e);
		}
	}

	/**
	 * {@code -1}, since HTTPS request are read differently.
	 * 
	 * @return -1 (null)
	 */
	@Override
	public int getReadSpeed() {
		return -1;
	}

	/**
	 * Does nothing. HTTPS request are read differently.
	 */
	@Override
	public void setReadSpeed(int readSpeed) {
	}
}
