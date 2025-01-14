package org.polinux.http.polinux.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.http.HttpClientThread;
import org.polinux.http.HttpServer;
import org.polinux.http.HttpServerRequest;
import org.polinux.http.HttpServerResponse;
import org.polinux.http.polinux.servlet.PolinuxHttpServerRequest;
import org.polinux.http.polinux.servlet.PolinuxHttpServerResponse;
import org.polinux.http.polinux.servlet.PolinuxHttpServlet;
import org.polinux.https.HttpsServer;
import org.polinux.https.polinux.server.PolinuxHttpsClientThread;
import org.polinux.utils.io.InputStreamReader;
import org.polinux.web.PolinuxWebApplication;

/**
 * Represents the thread of a connection (or client) inside a
 * {@link PolinuxHttpServerThread}. This class should be used in
 * {@link PolinuxHttpServerThread}, and no where else unless absolutely
 * necessary.
 * <p>
 * A few notes when using this class (if needed):
 * <ul>
 * <li>{@link #run()} should not be called when running the sever. Instead, use
 * {@link #execute()}.
 * <li>The {@link #serverThread PolinuxHttpServerThread} for this server thread
 * cannot be equal to {@code null}, for it is need throughout the code for this
 * server thread.
 * </ul>
 * 
 * @see PolinuxHttpsClientThread
 * @see PolinuxHttpServerThread
 */
@SuppressWarnings({ "deprecation" })
public class PolinuxHttpClientThread implements HttpClientThread {
	/**
	 * The {@link java.lang.Thread} linked to this server thread.
	 */
	protected transient Thread t;

	/**
	 * The {@link PolinuxHttpServerThread} linked to this server thread. Cannot be
	 * changed ({@code final}).
	 */
	protected transient final PolinuxHttpServerThread serverThread;

	/**
	 * The {@link java.net.Socket} for this server thread. Should not be touched and
	 * is handled by this class.
	 */
	protected transient Socket socket;

	/**
	 * Whether the thread is currently enabled.
	 */
	protected volatile boolean enabled = false;

	/**
	 * Default speed for how much bytes should be read at a time when processing an
	 * Http Request.
	 */
	static final int DEFAULT_READSPEED = 1024;

	/**
	 * Gets how much bytes should be read at a time when processing an Http Request.
	 * Default is {@code 1024}.
	 */
	protected int readSpeed = DEFAULT_READSPEED;

	/**
	 * The logger for the server.
	 */
	protected org.polinux.logging.Logger logger = PolinuxHttpServer.HTTP_LOGGER;

	/**
	 * Constructs a {@code Polinux HTTP Client Thread}. Note that this constructor
	 * alone will not start the server. {@link #execute()} must explicitly be called
	 * to start the server.
	 * 
	 * @param serverThread The {@link PolinuxHttpServerThread} this client thread is
	 *                     running off.
	 * @param socket       The {@link Socket} linked to this client.
	 * @throws PolinuxHttpServerRuntimeException If the underlying
	 *                                           {@link PolinuxHttpServer} is equal
	 *                                           to {@code null} or if an I/O error
	 *                                           occurs when opening the server.
	 */
	protected PolinuxHttpClientThread(final PolinuxHttpServerThread serverThread, final Socket socket)
			throws PolinuxHttpServerRuntimeException {
		this.serverThread = serverThread;
		this.socket = socket;
		this.logger = this.serverThread.getServer().getLogger();

		ensureValidServer();
		ensureValidSocket();

	}

	/**
	 * Ensures that he {@link #getSocket() socket} is valid.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If the socket is not valid.
	 */
	protected void ensureValidSocket() throws PolinuxHttpServerRuntimeException {
		if (this.serverThread == null) {
			throw new PolinuxHttpServerRuntimeException(this.serverThread.server,
					"Cannot create PolinuxHttpClientThread with null Socket");
		}
	}

	/**
	 * Ensures that he {@link #getServerThread() server thread} is valid.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If the server thread is not valid.
	 */
	protected void ensureValidServer() throws PolinuxHttpServerRuntimeException {
		if (this.serverThread == null) {
			throw new PolinuxHttpServerRuntimeException(null,
					"Cannot create PolinuxHttpClientThread with null PolinuxHttpServerThread");
		}

		if (!this.serverThread.isEnabled()) {
			throw new PolinuxHttpServerRuntimeException(null, "PolinuxHttpServerThread has been closed!");
		}
	}

	/**
	 * Starts the client thread.
	 * <p>
	 * Execution of this method is preferred over {@link #run()}, for this method
	 * prepare all necessary items before execution of the thread.
	 * 
	 * @throws PolinuxHttpServerRuntimeException If the server has already been
	 *                                           started.
	 */
	public synchronized void execute() throws PolinuxHttpServerRuntimeException {
		if (t != null) {
			throw new PolinuxHttpServerRuntimeException(this.serverThread.server,
					"Cannot start two instances of the same PolinuxHttpClientThread");
		}
		t = new Thread(this);
		enabled = true;
		t.start();
	}

	/**
	 * Retrieves the {@link Socket} created with the {@link #getServerThread()
	 * server thread} linked to this client thread.
	 * 
	 * @return The linked {@link Socket}.
	 */
	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * Retrieves the {@link PolinuxHttpServerThread} linked to this client thread.
	 * This method may return {@code null} if the thread runs without of a server
	 * (not recommended, not likely).
	 * 
	 * @return The linked {@link PolinuxHttpServerThread}.
	 */
	public PolinuxHttpServerThread getServerThread() {
		return this.serverThread;
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

		try {

			ensureValidServer();
			ensureValidSocket();

			final String stringRequest = getRequest(getSocket());

			PolinuxHttpServerRequest serverRequest = PolinuxHttpServerRequest.parse(this, stringRequest);

			PolinuxHttpServerResponse serverResponse = new PolinuxHttpServerResponse(this);

			PrintStream p = new PrintStream(socket.getOutputStream(), true);

			logger.log("REQUEST:" + logger.lineSeperator + stringRequest);
			logger.log(logger.lineSeperator);
			logger.log("Params: " + serverRequest.getParameters());
			logger.flush();

			// p.println(serverResponse.asString(logger.lineSeperator));

			if (serverRequest.getHeader("Connection") != null
					&& serverRequest.getHeader("Connection").getValue().equalsIgnoreCase("Close")) {
				this.getSocket().setKeepAlive(false);
			} else {
				this.getSocket().setKeepAlive(true);
			}

			if (getServerThread().getServer().getConfiguration().isHttpsRedirect()
					&& (!(getServerThread().getServer() instanceof HttpsServer))) {
				serverResponse.setStatus(HttpServerResponse.StatusCode.TEMPORARY_REDIRECT);
				final String protocol = "https";
				final String reqHost = serverRequest.getHost();
				final String host = reqHost == null ? this.getServerThread().getServer().getHost() : reqHost;
				final String requestUri = serverRequest.getPath();
				final String queryWithPrefix = stringRequest.split(logger.lineSeperator)[0].split(" ")[1];

				final String url = protocol + "://" + host + requestUri + queryWithPrefix;
//
				serverResponse.setHeader("Location", url);
				serverResponse.setHeader("Content-Length", "0");

				// serverResponse.setStatus(HttpServerResponse.StatusCode.OK);

				serverResponse.setContentType("text/html");

				// serverResponse.getWriter().println("<!DOCTYPE html><html></html>");

				final String asString = serverResponse.asString(logger.lineSeperator, true);

				p.println(asString);

				p.flush();
				p.close();
				logger.log("Redirected socket " + socket + " to HTTPS server.");
				logger.flush();

				// socket.close();
				return;
			}

			serverResponse.setStatus(200);

			boolean servletFound;

			try {
				servletFound = forwardToServlets(serverRequest, serverResponse);
			} catch (Throwable e) {
				internalServerError(getSocket(), e);
				return;
			}

			if (!servletFound) {
				boolean fileFound;
				try {
					fileFound = forwardToFiles(serverRequest, serverResponse);
				} catch (Throwable e) {
					internalServerError(getSocket(), e);
					return;
				}

				if (!fileFound) {

					serverResponse.setStatus(HttpServerResponse.StatusCode.NOT_FOUND);

					serverResponse.getWriter().println(get404Page());
				}

			} else {
				final int status = serverResponse.getStatus();

				if ((!(serverResponse.getWriter().hasWritten())) && (status >= 400)) {
					serverResponse.getWriter().println(getErrorPage(status, ""));
				}

			}

			if (serverRequest.getSession().isNew()) {
				serverResponse.addCookie(serverRequest.getSession().getLinkedCookie());
			}

			// serverResponse.getWriter().println(prebuildMessage);

			final String resAsString = serverResponse.asString(logger.lineSeperator, true);
			
			logger.log("SentBack::::::: ");
			logger.log(resAsString);
			
			p.println(resAsString);

			p.flush();

			p.close();

			this.enabled = false;

//			p.close();
//			socket.close();

//
//			final String[] lines = request.split(logger.lineSeperator);
//
//			String method = null;
//
//			String path = null;
//
//			String httpVersion = null;
//
//			String host = null;
//
//			String userAgent = null;
//
//			String accept = null;
//
//			String connection = null;
//
//			String cookie = null;
//
//			List<String> header = new LinkedList<String>();
//
//			for (int i = 0; i < lines.length; i++) {
//				final String currentLine = lines[i].trim();
//
//				header.add(currentLine);
//
//				if (i == 0) {
//
//					try {
//						final String[] parts = currentLine.split(" ");
//
//						method = parts[0];
//
//						path = parts[1];
//
//						httpVersion = parts[2];
//					} catch (Exception e) {
//						String message = HttpServer.HTTP_VERSION + " " + HttpServerResponse.StatusCode.BAD_REQUEST
//								+ " Bad Request" + logger.lineSeperator;
//
//						message += "Cache-Control: private, no-cache, no-store, must-revalidate, max-age=0"
//								+ logger.lineSeperator;
//
//						message += "Pragma: no-cache" + logger.lineSeperator;
//
//						message += "Expires: -1" + logger.lineSeperator;
//
//						message += "Server: " + this.serverThread.server.getName() + logger.lineSeperator;
//
//						message += "Content-Type: text/html; charset=UTF-8" + logger.lineSeperator;
//
//						message += logger.lineSeperator;
//
//						message += "<html>" + logger.lineSeperator;
//
//						message += "<head>" + logger.lineSeperator;
//						message += "<style>" + logger.lineSeperator;
//
//						message += ".banner {background-color: #74b9ff; color: #FFFFFF;}" + logger.lineSeperator;
//						message += ".error {background-color: #dfe6e9; color: #e34234; border: 2px solid #ff7675;}"
//								+ logger.lineSeperator;
//
//						message += "</style>";
//
//						message += "</head>" + logger.lineSeperator;
//
//						message += "<body>" + logger.lineSeperator;
//
//						message += "<h1 class=\"banner\">HTTP - " + HttpServerResponse.StatusCode.BAD_REQUEST
//								+ " - Bad Request</h1>" + logger.lineSeperator;
//						message += "<p>The client entered an incorrect or invalid request.</p>" + logger.lineSeperator;
//
//						message += "<p style=\"error\">" + logger.lineSeperator;
//
//						StringWriter sw = new StringWriter();
//
//						e.printStackTrace(new PrintWriter(sw));
//
//						message += sw.toString();
//
//						message += "</p>" + logger.lineSeperator;
//
//						message += "</body>" + logger.lineSeperator;
//
//						message += "</html>" + logger.lineSeperator;
//
//					}
//
//					continue;
//				}
//
//				if (currentLine.toLowerCase().startsWith("host:")) {
//					final String[] parts = currentLine.split(":");
//
//					host = parts[1].trim();
//					continue;
//				}
//
//				if (currentLine.toLowerCase().startsWith("user-agent:")) {
//					final String[] parts = currentLine.split(":");
//
//					userAgent = parts[1].trim();
//					continue;
//				}
//
//				if (currentLine.toLowerCase().startsWith("accept:")) {
//					final String[] parts = currentLine.split(":");
//
//					accept = parts[1].trim();
//					continue;
//				}
//
//				if (currentLine.toLowerCase().startsWith("connection:")) {
//					final String[] parts = currentLine.split(":");
//
//					connection = parts[1].trim();
//					continue;
//				}
//
////				JavaCompiler c = ToolProvider.getSystemJavaCompiler();
////				
////				c.run(null, null, null, "file.java");
//
//				if (currentLine.toLowerCase().startsWith("cookie:")) {
//					final String[] parts = currentLine.split(":");
//
//					cookie = parts[1].trim();
//					continue;
//				}
//
//				if (currentLine.toLowerCase().startsWith("connection:")) {
//					final String[] parts = currentLine.split(":");
//
//					connection = parts[1].trim();
//					continue;
//				}
//
//			}
//
//			logger.log("Request: \r\n" + request);
//
////			PolinuxHttpServerRequest serverRequest = PolinuxHttpServerRequest.parse(this.serverThread.server, this,
////					request, logger.lineSeperator);
////
////			PolinuxHttpServerResponse serverResponse = new PolinuxHttpServerResponse(this.serverThread.server, this);
////
////			if (serverRequest.getCookie(this.serverThread.server.config.getSessionCookieName()) == null) {
////
////			} else {
////				
////			}
////
//			final PolinuxHttpServletWriter out = new PolinuxHttpServletWriter();
//
////			final String message = "HTTP/1.1 200 OK" + logger.lineSeperator + "Cache-Control: private, max-age=0"
////					+ logger.lineSeperator + "Server: " + this.serverThread.server.getName() + logger.lineSeperator
////					+ "Content-Type: text/html; charset=UTF-8" + logger.lineSeperator + "Set-Cookie: alvyn=specyy"
////					+ logger.lineSeperator + "Expires: -1" + "\r\n\r\n"
////					+ "<html><head></head><body><h1>This is the webpage that was sent back from the print stream</h1></body></html>";
//
////			String message = HttpServer.HTTP_VERSION + " " + HttpServerResponse.StatusCode.BAD_REQUEST + " Bad Request"
////					+ logger.lineSeperator;
//			
//			String message = "";
//
//			message += "Cache-Control: private, no-cache, no-store, must-revalidate, max-age=0" + logger.lineSeperator;
//
//			message += "Pragma: no-cache" + logger.lineSeperator;
//
//			message += "Expires: -1" + logger.lineSeperator;
//
//			message += "Server: " + this.serverThread.server.getName() + logger.lineSeperator;
//
//			message += "Content-Type: text/html; charset=UTF-8" + logger.lineSeperator;
//
//			message += logger.lineSeperator;
//
//			message += "<html>" + logger.lineSeperator;
//
//			message += "<head>" + logger.lineSeperator;
//			message += "<style>" + logger.lineSeperator;
//
//			message += ".banner {background-color: #74b9ff; color: #FFFFFF;}" + logger.lineSeperator;
//			message += ".error {background-color: #dfe6e9; color: #e34234; border: 2px solid #ff7675; white-space: pre-wrap;}"
//					+ logger.lineSeperator;
//
//			message += "</style>";
//
//			message += "</head>" + logger.lineSeperator;
//
//			message += "<body>" + logger.lineSeperator;
//
//			message += "<h1 class=\"banner\">HTTP - " + HttpServerResponse.StatusCode.BAD_REQUEST
//					+ " - Bad Request</h1>" + logger.lineSeperator;
//			message += "<p>The client entered an incorrect or invalid request.<br>Client IP:"
//					+ socket.getInetAddress().toString() + "</p>" + logger.lineSeperator;
//
//			message += "<p class=\"error\">";
//
//			try {
//				Object o = new String("hh");
//				PrintStream p = (PrintStream) o;
//
//			} catch (Exception e) {
//				StringWriter sw = new StringWriter();
//				e.printStackTrace(new PrintWriter(sw));
//				message += sw.toString();
//			}
//
//			message += "</p>" + logger.lineSeperator;
//
//			message += "<p class=\"banner\">" + this.serverThread.server.getName() + "</p>" + logger.lineSeperator;
//
//			message += "</body>" + logger.lineSeperator;
//
//			message += "</html>" + logger.lineSeperator;
//
//			out.println(message);
//
//			PolinuxHttpServerRequest serverRequest = PolinuxHttpServerRequest.parse(this.serverThread.server, this,
//					request, logger.lineSeperator);
//
//			PolinuxHttpServerResponse serverResponse = new PolinuxHttpServerResponse(this.serverThread.server, this);
//			serverResponse.setStatus(200);
//			//serverResponse.getWriter().println(message);
//			final String finalMessageToSend = serverResponse.asString(logger.lineSeperator);
//			logger.log("FINALMTOSEND: " + finalMessageToSend);
//			socket.getOutputStream().write(finalMessageToSend.getBytes());
//			logger.log("METHOD: " + method);
//
//			logger.log("PATH: " + path);
//
//			logger.log("HTTP: " + httpVersion);
//
//			logger.log("HOST: " + String.valueOf(host));
//
//			logger.log("COOKIES: " + String.valueOf(cookie));
//
//			logger.log(socket.getInetAddress());
//
//			socket.close();
//
//			logger.log("IP IS: " + socket.getInetAddress());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void internalServerError(final Socket socket, final Throwable error) throws IOException {
		String message = HttpServer.HTTP_VERSION + " " + HttpServerResponse.StatusCode.INTERNAL_SERVER_ERROR
				+ " Internal Server Error" + logger.lineSeperator;

		message += "Cache-Control: private, no-cache, no-store, must-revalidate, max-age=0" + logger.lineSeperator;

		message += "Pragma: no-cache" + logger.lineSeperator;

		message += "Expires: -1" + logger.lineSeperator;

		message += "Server: " + this.serverThread.server.getName() + logger.lineSeperator;

		message += "Content-Type: text/html; charset=UTF-8" + logger.lineSeperator;

		message += logger.lineSeperator;

		message += "<html>" + logger.lineSeperator;

		message += "<head>" + logger.lineSeperator;
		message += "<style>" + logger.lineSeperator;

		message += ".banner {background-color: #74b9ff; color: #FFFFFF;}" + logger.lineSeperator;
		message += ".error {background-color: #dfe6e9; color: #e34234; border: 2px solid #ff7675; white-space: pre-wrap;}"
				+ logger.lineSeperator;

		message += "</style>";

		message += "</head>" + logger.lineSeperator;

		message += "<body>" + logger.lineSeperator;

		message += "<h1 class=\"banner\">HTTP - " + HttpServerResponse.StatusCode.INTERNAL_SERVER_ERROR
				+ " - Internal Server Error</h1>" + logger.lineSeperator;
		message += "<p>An internal server error occured while trying to parse the client's request.<br><br>Details:</p>"
				+ logger.lineSeperator;

		message += "<p class=\"error\">" + logger.lineSeperator;

		StringWriter sw = new StringWriter();

		error.printStackTrace(new PrintWriter(sw));

		message += sw.toString().trim();

		message += "</p>" + logger.lineSeperator;

		message += "<p class=\"banner\">" + this.serverThread.server.getName() + "</p>" + logger.lineSeperator;

		message += "</body>" + logger.lineSeperator;

		message += "</html>" + logger.lineSeperator;

		PrintStream p = new PrintStream(socket.getOutputStream());

		p.println(message);

		p.flush();
		p.close();
		this.enabled = false;
	}

	/**
	 * Retrieves the socket HTTP request.
	 * 
	 * @param socket The {@link Socket} through whom the request will be read.
	 * @return The socket's HTTP request.
	 * @throws Exception If an error occurs.
	 */
	protected String getRequest(Socket socket) throws Exception {
		// Old way of reading
//		String stringRequest = "";
//
//		final InputStream in = socket.getInputStream();
//
//		while (in.available() <= 0) {
//			continue;
//		}
//
//		final int speed = readSpeed;
//		byte[] bytes;
//
//		while (in.available() > 0) {
//			bytes = new byte[speed];
//			final int len = in.read(bytes, 0, bytes.length);
//
//			stringRequest += new String(bytes, 0, len);
//		}
//
//		return stringRequest;

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

	private char[] getErrorPage(final int status, final String body) {
		if (status == HttpServerResponse.StatusCode.NOT_FOUND) {
			return get404Page();
		}
		String prebuildMessage = "";
		prebuildMessage += "<!DOCTYPE html>" + logger.lineSeperator;
		prebuildMessage += "<html>" + logger.lineSeperator;

		prebuildMessage += "\t<head>" + logger.lineSeperator;
		prebuildMessage += "\t\t<style>" + logger.lineSeperator;

		prebuildMessage += "\t\t\t.banner {background-color: #74b9ff; color: #FFFFFF;}" + logger.lineSeperator;
		prebuildMessage += "\t\t\t.error {background-color: #dfe6e9; color: #e34234; border: 2px solid #ff7675; white-space: pre-wrap;}"
				+ logger.lineSeperator;

		prebuildMessage += "\t\t</style>" + logger.lineSeperator;

		prebuildMessage += "\t</head>" + logger.lineSeperator;

		prebuildMessage += "\t<body>" + logger.lineSeperator;

		prebuildMessage += "\t\t\t<h1 class=\"banner\">HTTP - " + status + " - "
				+ HttpServerResponse.StatusCode.getStatusMessage(status) + "</h1>" + logger.lineSeperator;
		prebuildMessage += "\t\t\t" + body + "<br>" + logger.lineSeperator;

//		prebuildMessage += "<p class=\"error\">";
//
//		try {
//			Object o = new String("hh");
//			PrintStream p = (PrintStream) o;
//		} catch (Exception e) {
//			StringWriter sw = new StringWriter();
//			e.printStackTrace(new PrintWriter(sw));
//			prebuildMessage += sw.toString();
//		}
//
//		prebuildMessage += "</p>" + logger.lineSeperator;

		prebuildMessage += "\t\t\t<p class=\"banner\">" + this.serverThread.server.getName() + "</p>"
				+ logger.lineSeperator;
		prebuildMessage += "\t</body>" + logger.lineSeperator;

		prebuildMessage += "</html>" + logger.lineSeperator;
		return prebuildMessage.toCharArray();
	}

	private char[] get404Page() {
		String prebuildMessage = "";
		prebuildMessage += "<!DOCTYPE html>" + logger.lineSeperator;
		prebuildMessage += "<html>" + logger.lineSeperator;

		prebuildMessage += "\t<head>" + logger.lineSeperator;
		prebuildMessage += "\t\t<style>" + logger.lineSeperator;

		prebuildMessage += "\t\t\t.banner {background-color: #74b9ff; color: #FFFFFF;}" + logger.lineSeperator;
		prebuildMessage += "\t\t\t.error {background-color: #dfe6e9; color: #e34234; border: 2px solid #ff7675; white-space: pre-wrap;}"
				+ logger.lineSeperator;

		prebuildMessage += "\t\t</style>" + logger.lineSeperator;

		prebuildMessage += "\t</head>" + logger.lineSeperator;

		prebuildMessage += "\t<body>" + logger.lineSeperator;

		prebuildMessage += "\t\t\t<h1 class=\"banner\">HTTP - " + HttpServerResponse.StatusCode.NOT_FOUND
				+ " - Not Found</h1>" + logger.lineSeperator;
		prebuildMessage += "\t\t\t<h3>The page requested was not found.</h3><br>" + logger.lineSeperator;

//		prebuildMessage += "<p class=\"error\">";
//
//		try {
//			Object o = new String("hh");
//			PrintStream p = (PrintStream) o;
//		} catch (Exception e) {
//			StringWriter sw = new StringWriter();
//			e.printStackTrace(new PrintWriter(sw));
//			prebuildMessage += sw.toString();
//		}
//
//		prebuildMessage += "</p>" + logger.lineSeperator;

		prebuildMessage += "\t\t\t<p class=\"banner\">" + this.serverThread.server.getName() + "</p>"
				+ logger.lineSeperator;
		prebuildMessage += "\t</body>" + logger.lineSeperator;

		prebuildMessage += "</html>" + logger.lineSeperator;
		return prebuildMessage.toCharArray();
	}

	/**
	 * Forwards a request & response to the known servlets.
	 * 
	 * @param req The request.
	 * @param res The response.
	 * @return True if servlet was found, false otherwise.
	 */
	protected synchronized boolean forwardToServlets(HttpServerRequest req, HttpServerResponse res) {
		boolean found = false;
		Set<? extends PolinuxWebApplication> apps = this.serverThread.server.getWebApplications();
		final String urlPattern = req.getPath();

		for (PolinuxWebApplication app : apps) {
			for (PolinuxHttpServlet servlet : app.getServlets()) {
				for (String url : servlet.getUrlPatterns()) {
					if (servletURLMatchRequestURL(urlPattern, url, app)) {
						servlet.service(req, res);
						found = true;
					}
				}

			}
		}

		return found;
	}

	/*
	 * Checks whether the two URL patterns (servlet pattern & request pattern) are
	 * matching
	 */
	private boolean servletURLMatchRequestURL(String requestPattern, String servletPattern, PolinuxWebApplication app) {
		// What a mess

		String newServletPattern = app.getParentDirectory().getPath().replace("\\", "/")
				+ servletPattern.replace("\\", "/");
		newServletPattern = newServletPattern.endsWith("/")
				? newServletPattern.substring(0, newServletPattern.length() - 1)
				: newServletPattern;

		final String rootName = this.serverThread.server.getConfiguration().getWebsiteRoot().replace("\\", "/");
		String newRequestPattern = ((rootName.endsWith("/") || rootName.endsWith("\\"))
				? rootName.substring(0, rootName.length() - 1)
				: rootName) + requestPattern;
		newRequestPattern = newRequestPattern.endsWith("/")
				? newRequestPattern.substring(0, newRequestPattern.length() - 1)
				: newRequestPattern;

		if (newRequestPattern.equalsIgnoreCase(newServletPattern))
			return true;
		return checkURLPatternMatch(newServletPattern, newRequestPattern);
	}

	/* Further checking for servlet URL pattern matching (with asterisks) */
	private boolean checkURLPatternMatch(String urlPattern, String check) {
		// What a mess

		final String SLASH = Pattern.quote("/");
		while (urlPattern.contains("//")) {
			urlPattern = urlPattern.replace("//", "/");
		}

		if (urlPattern.equalsIgnoreCase("/*"))
			return true;

		final String[] urlPatternSplit = urlPattern.split(SLASH);

		while (check.contains("//")) {
			check = check.replace("//", "/");
		}

		final String[] checkSplit = check.split(SLASH);

		System.out.println(check.length());
		System.out.println(check);

		if (urlPattern.equalsIgnoreCase("/*/"))
			return checkSplit.length == 2;

		if (checkSplit.length != urlPatternSplit.length && !urlPattern.endsWith("/*"))
			return false;

		if (urlPattern.endsWith("/*/")) {
			for (int i = 0; i < (urlPatternSplit.length - 1); i++) {
				final String urlPatternPart = urlPatternSplit[i].trim();

				final String checkPart;

				try {
					checkPart = checkSplit[i].trim();
				} catch (ArrayIndexOutOfBoundsException e) {
					return false;
				}

				if (urlPatternPart.equalsIgnoreCase("*"))
					continue;

				if (!checkPart.equalsIgnoreCase(urlPatternPart))
					return false;
			}

		} else {

			for (int i = 0; i < urlPatternSplit.length; i++) {

				final String urlPatternPart = urlPatternSplit[i].trim();

				final String checkPart;

				try {
					checkPart = checkSplit[i].trim();
				} catch (ArrayIndexOutOfBoundsException e) {
					return false;
				}

				if (urlPatternPart.equalsIgnoreCase("*"))
					continue;

				if (!checkPart.equalsIgnoreCase(urlPatternPart))
					return false;
			}
		}
		return true;
	}

	/**
	 * Forwards a request & response to the known files.
	 * 
	 * @param req The request.
	 * @param res The response.
	 * @return True if file was found, false otherwise.
	 * @throws IOException If an I/O error occurs
	 */
	protected synchronized boolean forwardToFiles(HttpServerRequest req, HttpServerResponse res) throws IOException {
		boolean found = false;

		final String httpRequestUrl = req.getPath();

		String finalFileContent = null;

		final String root = this.getServerThread().getServer().getConfiguration().getWebsiteRoot().endsWith("/")
				? this.getServerThread().getServer().getConfiguration().getWebsiteRoot().substring(0,
						this.getServerThread().getServer().getConfiguration().getWebsiteRoot().length() - 1)
				: this.getServerThread().getServer().getConfiguration().getWebsiteRoot();

		File f = new File(root + httpRequestUrl);

		if (f.exists()) {
			FileInputStream in = null;
			try {
				in = new FileInputStream(f);
				finalFileContent = InputStreamReader.read(in);

			} catch (Exception e) {
				finalFileContent = null;
			} finally {
				if (in != null)
					in.close();
			}
		}

		final Set<? extends PolinuxWebApplication> apps = this.getServerThread().getServer().getWebApplications();

		for (PolinuxWebApplication app : apps) {
			if (finalFileContent != null)
				break;

			final String appRoot = app.getConfiguration().getWebsiteRoot().endsWith("/")
					? app.getConfiguration().getWebsiteRoot().substring(0,
							app.getConfiguration().getWebsiteRoot().length() - 1)
					: app.getConfiguration().getWebsiteRoot();

			for (Map.Entry<String, String> entry : app.getWebFiles().entrySet()) {
				if (finalFileContent != null)
					break;

				String filePath = entry.getKey();

				final String fileContent = entry.getValue();

				String httpRequestUrlFormatted = appRoot + httpRequestUrl;

				String fileName = filePath.substring(filePath.lastIndexOf("/") + "/".length(), filePath.length());

				File filePathAsFile = app.getParentDirectory();

				File serverDirAsFile = new File(this.getServerThread().getServer().getConfiguration().getWebsiteRoot());

				if (!filePathAsFile.getAbsolutePath().equals(serverDirAsFile.getAbsolutePath())) {
					String fileRootAbs = filePathAsFile.getAbsolutePath().replace("\\", "/");
					String serverRootAbs = serverDirAsFile.getAbsolutePath().replace("\\", "/");
					String newPath = appRoot + (fileRootAbs.substring(serverRootAbs.length(), fileRootAbs.length()));
					filePath = newPath + "/" + fileName;
				}

				filePath = filePath.trim();
				filePath = filePath.endsWith("/") ? filePath.substring(0, filePath.length() - 1) : filePath;

				if ((filePath + "/").equals(httpRequestUrlFormatted) || filePath.equals(httpRequestUrlFormatted)) {
					finalFileContent = fileContent;
					break;
				}

				String newFileName = "/" + fileName + "/";

				for (String welcome : app.getConfiguration().getWelcomeFiles()) {
					if (fileName.equals(welcome)) {
						newFileName = "/";
						break;
					}
				}

				String newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + newFileName;

				if (newFilePath.equals(httpRequestUrlFormatted)
						|| newFilePath.substring(0, newFilePath.length() - 1).equals(httpRequestUrlFormatted)) {
					finalFileContent = fileContent;
					break;
				}

			}
		}

		found = (finalFileContent != null);

		if (found) {
			res.getWriter().println(finalFileContent);
		}

		return found;
	}

	/**
	 * Closes the current client thread. After execution of this method, this class
	 * may be disposed of (should <i>never</i> be used again).
	 * 
	 * @throws PolinuxHttpServerRuntimeException if an I/O error occurs when closing
	 *                                           the socket
	 */
	public synchronized void close() throws PolinuxHttpServerRuntimeException {
		try {
			this.abort();
		} catch (PolinuxHttpServerRuntimeException e) {

		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				throw new PolinuxHttpServerRuntimeException(this.serverThread.server, e.getMessage(), e);
			}
		}
	}

	/**
	 * Terminates the client thread.
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
			throw new PolinuxHttpServerRuntimeException(this.serverThread.server,
					"Cannot abort PolinuxHttpClientThread that has not started!");
		}
		this.enabled = false;
		t.stop();
		t = null;
	}

	/**
	 * Retrieves whether the client thread is currently enabled.
	 * 
	 * @return {@code True} if the client thread is currently enabled, {@code false}
	 *         otherwise.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Gets how much bytes should be read at a time when processing an Http Request.
	 * 
	 * @return The current "read speed" of the client thread. Default is
	 *         {@value #DEFAULT_READSPEED}.
	 */
	public int getReadSpeed() {
		return readSpeed;
	}

	/**
	 * Sets the current "read sped" for the current client thread. Read speed is
	 * considered to be how much bytes should be read at a time when processing an
	 * Http Request.
	 * 
	 * @param readSpeed The "read speed", normally a multiple of
	 *                  {@value #DEFAULT_READSPEED}.
	 */
	public void setReadSpeed(int readSpeed) {
		this.readSpeed = readSpeed;
	}

}
