package org.polinux.https.polinux.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.exceptions.https.polinux.PolinuxHttpsServerRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServerThread;

/**
 * Represents the thread of a {@link PolinuxHttpsServer} that handles incoming
 * connections. This class should be used in {@link PolinuxHttpsServer}, and no
 * where else unless absolutely necessary.
 * <p>
 * A few notes when using this class (if needed):
 * <ul>
 * <li>{@link #run()} should not be called when running the sever. Instead, use
 * {@link #execute()}.
 * <li>The {@link #server PolinuxHttpsServer} for this server thread cannot be
 * equal to {@code null}, for it is need throughout the code for this server
 * thread.
 * </ul>
 * 
 * @see PolinuxHttpsServer
 * @see PolinuxHttpServerThread
 */
public class PolinuxHttpsServerThread extends PolinuxHttpServerThread {
	/**
	 * Represents the {@link SSLContext} for the server. Modify at your own risk.
	 */
	protected SSLContext context;

	/**
	 * Constructs a {@code Polinux HTTPS Server Thread}. Note that this constructor
	 * alone will not start the server. {@link #execute()} must explicitly be called
	 * to start the server.
	 * 
	 * @param server The PolinuxHttpsServer this thread is running off.
	 * @throws PolinuxHttpsServerRuntimeException If the underlying
	 *                                            {@link PolinuxHttpsServer} is
	 *                                            equal to {@code null} or if an I/O
	 *                                            error occurs when opening the
	 *                                            server.
	 */
	protected PolinuxHttpsServerThread(PolinuxHttpsServer server) throws PolinuxHttpsServerRuntimeException {
		this(server, true);
	}

	/**
	 * Constructs a {@code Polinux HTTPS Server Thread}. Note that this constructor
	 * alone will not start the server. {@link #execute()} must explicitly be called
	 * to start the server.
	 * 
	 * @param server     The PolinuxHttpsServer this thread is running off.
	 * @param initServer Whether to initialize the {@link #getServerSocket() server
	 *                   socket} for the server. Set to {@code false} if the
	 *                   {@link ServerSocket} initialization will be handled by a
	 *                   sub-class.
	 * @throws PolinuxHttpsServerRuntimeException If the underlying
	 *                                            {@link PolinuxHttpsServer} is
	 *                                            equal to {@code null} or if an I/O
	 *                                            error occurs when opening the
	 *                                            server.
	 */
	protected PolinuxHttpsServerThread(PolinuxHttpsServer server, final boolean initServer)
			throws PolinuxHttpsServerRuntimeException {
		super(server, false);

		/* Initializes the server */

		File keyStoreFile = null;
		String keyStorePassword = null;

		// If using jks, set keystore file & password to jks file & password inside
		// config
		if (server.getConfiguration().isUseSSLKeystore()) {
			keyStoreFile = server.getConfiguration().getSSLKeyStoreFile();
			keyStorePassword = server.getConfiguration().getSSLKeyStoreFilePassword();
		}

		// If using PFX and not JKS, load PFX
		if (server.getConfiguration().isUseSSLPFX()) {
			// If also using JKS, which means both are set to trues, throw an exception
			if (server.getConfiguration().isUseSSLKeystore()) {
				throw new PolinuxHttpsServerRuntimeException(server,
						"PolinuxHttpsServer cannot use both PFX and KEYSTORE");
			}
			// Set keystore file & password to PFX file & password inside config for now
			keyStoreFile = server.getConfiguration().getSSLPFXFile();
			keyStorePassword = server.getConfiguration().getSSLPFXFilePassword();

			final File keyStoreSaveFile = new File(
					"cache" + File.separator + "keystores" + File.separator + keyStoreFile.getName() + ".jks");

			if (keyStoreSaveFile.exists()) {
				// Delete cached PFX if pfx caching is disabled
				if (!server.getConfiguration().isEnablePFXCaching()) {
					server.getLogger().log("Deleting pfx cache for " + keyStoreSaveFile.getPath() + "...");
					keyStoreSaveFile.delete();
					server.getLogger().log("Deleted pfx cache for " + keyStoreSaveFile.getPath() + " !");
				}
			}

			if (!keyStoreSaveFile.exists()) {
				if (!keyStoreSaveFile.getParentFile().exists())
					keyStoreSaveFile.getParentFile().mkdirs();

				server.getLogger().log("Using PFX, creating keystore for pfx file " + keyStoreFile.getPath() + "...");
				server.getLogger().log("Ignore the warnings (if any) below this message...");

				String[] keyToolArgs = new String[] { "-importkeystore", "-srckeystore", keyStoreFile.getAbsolutePath(),
						"-destkeystore", keyStoreSaveFile.getAbsolutePath(), "-deststoretype", "JKS", "-deststorepass",
						keyStorePassword, "-srcstorepass", keyStorePassword };

				try {
					// If PFX does not exist, transform PFX into JKS and keep the new JKS inside
					// cache if possible
					sun.security.tools.keytool.Main.main(keyToolArgs);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// Set keystore file to the new JKS version of the PFX files
			keyStoreFile = keyStoreSaveFile;
		}

		// Throw exception if password or keystore file still equal null up to this
		// point
		if (keyStoreFile == null || keyStorePassword == null) {
			throw new PolinuxHttpsServerRuntimeException(server, "PolinuxHttpsServer was unable to find keystore/pfx");
		}

		try {
			// Create SSLContext from the keystore info
			SSLContext context = PolinuxHttpsServerThread.SSLUtils.createSSLServerSSLContext(keyStoreFile,
					keyStorePassword);
			this.context = context;
			if (initServer)
				// Create SSL socket from SSLContext, if initServer is set to true
				this.serverSocket = (SSLServerSocket) context.getServerSocketFactory().createServerSocket(
						server.getPort(), server.getBacklog(), InetAddress.getByName(server.getHost()));
		} catch (PolinuxHttpServerRuntimeException | GeneralSecurityException | IOException e) {
			// Print caught exception
			e.printStackTrace();
			// Throw exception indicating general error
			throw new PolinuxHttpsServerRuntimeException(server, "Unable to load keystore "
					+ keyStoreFile.getAbsolutePath() + ". Try deleting the cache, then try again.");
		}

	}

	/**
	 * Starts the server thread.
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
			// Thread has already started, throw an exception
			throw new PolinuxHttpsServerRuntimeException(this.getServer(),
					"Cannot start two instances of the same PolinuxHttpsServerThread");
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

		this.logStarted();

		while (this.isEnabled()) {
			try {
				// Accept client
				final SSLSocket socket = (SSLSocket) getServerSocket().accept();

				// Log client
				this.server.getLogger()
						.log("Client accessing HTTPS server: (" + socket.getInetAddress().getHostName() + ") "
								+ socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort()
								+ " (remote port " + socket.getPort() + " )");

				// Handle request on different thread
				final PolinuxHttpsClientThread clientThread = new PolinuxHttpsClientThread(this, socket);
				clientThreads.add(clientThread);

				// Execute client thread
				clientThread.execute();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Retrieves the {@link PolinuxHttpsServer} linked to this server thread.
	 * 
	 * @return The linked {@link PolinuxHttpsServer}.
	 */
	@Override
	public PolinuxHttpsServer getServer() {
		return (PolinuxHttpsServer) super.getServer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSLServerSocket getServerSocket() {
		return (SSLServerSocket) super.getServerSocket();
	}

	/**
	 * Class containing SSL utilities
	 */
	protected static final class SSLUtils {
		protected static String KEY_STORE_TYPE = "JKS";
		// private static String TRUST_STORE_TYPE = "JKS";
		protected static String KEY_MANAGER_TYPE = "SunX509";
		// private static String TRUST_MANAGER_TYPE = "SunX509";
		protected static String PROTOCOL = "TLS";

		/**
		 * Generates an {@link SSLServerSocket} from the underlying credentials.
		 * 
		 * @param keystore         The keystore file.
		 * @param keystorePassword The keystore file password.
		 * @param port             The port for the ssl server.
		 * @param backlog          The ssl server's backlog
		 * @param address          The {@link InetAddress} for the server.
		 * @return The created {@link SSLServerSocket}.
		 * @throws GeneralSecurityException If a {@link GeneralSecurityException}
		 *                                  occurs.
		 * @throws IOException              If an I/O error occurs.
		 */
		protected static final SSLServerSocket createSSLServer(final File keystore, final String keystorePassword,
				final int port, final int backlog, final InetAddress address)
				throws GeneralSecurityException, IOException {
			SSLContext context = createSSLServerSSLContext(keystore, keystorePassword);

			return (SSLServerSocket) context.getServerSocketFactory().createServerSocket(port, backlog, address);
		}

		/**
		 * Generates the {@link SSLContenxt} for the keystore file.
		 * 
		 * @param keystoreFile     The keystore file.
		 * @param keystorePassword The keystore file password.
		 * @return The generated ssl context.
		 * @throws GeneralSecurityException If a {@link GeneralSecurityException}
		 *                                  occurs.
		 * @throws IOException              If an I/O error occurs.
		 */
		protected static final SSLContext createSSLServerSSLContext(final File keystoreFile,
				final String keystorePassword) throws GeneralSecurityException, IOException {
			SSLContext serverSSLCtx;

			// System.setProperty("javax.net.ssl.trustStore",
			// keystoreFile.getAbsolutePath());
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
			keyStore.load(new FileInputStream(keystoreFile), keystorePassword.toCharArray());
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_TYPE);
			keyManagerFactory.init(keyStore, keystorePassword.toCharArray());
			serverSSLCtx = SSLContext.getInstance(PROTOCOL);
			serverSSLCtx.init(keyManagerFactory.getKeyManagers(), null, null);

			return serverSSLCtx;
		}
	}

}
