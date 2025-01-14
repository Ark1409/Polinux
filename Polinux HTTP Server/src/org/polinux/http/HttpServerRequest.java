package org.polinux.http;

import java.net.Socket;
import java.util.regex.Pattern;

import org.polinux.http.servlet.Servlet;
import org.polinux.https.HttpsServer;

/**
 * Represents a client request from an incoming {@link HttpClientThread client}
 * on an {@link HttpServer} or an {@link HttpsServer}. The request should have a
 * matching {@link HttpServerResponse}.
 * 
 * @see HttpServerResponse
 */
public interface HttpServerRequest extends Attributable, Parametable, Headerable, Cookieable {
	/**
	 * Retrieves the {@code HTTP Request Method} used to access the following
	 * resource.
	 * 
	 * @return The client request's {@code HTTP Request Method}.
	 * @see HttpServerRequest.RequestMethod
	 */
	public String getMethod();

	/**
	 * Retrieves the {@link HttpSession} linked with this client.
	 * 
	 * @return The client's {@link HttpSession}.
	 */
	public HttpSession getSession();

	/**
	 * Retrieves the User Agent for the client.
	 * 
	 * @return The client User Agent.
	 */
	public String getUserAgent();

	/**
	 * Retrieves the HTTP version that the client is using. {@link HttpServer
	 * HttpServers} only accept {@value HttpServer#HTTP_VERSION}
	 * 
	 * @return The HTTP version of the client.
	 * @see {@link HttpServer#HTTP_VERSION}
	 */
	public String getHttpVersion();

	/**
	 * Retrieves the host for the client. May return {@code null} if no host was
	 * specified.
	 * 
	 * @return The host name of the web server the client is accessing.
	 */
	public String getHost();

	/**
	 * Retrieves the document path the client is trying to access. <i>This is
	 * <b>NOT</b> the full URI of the request</i> (i.e. does not contain query
	 * parameters).
	 * 
	 * @return The client's request path.
	 */
	public String getPath();

	/**
	 * Retrieves a path parameter from the client request. This is to be used
	 * whenever the {@link Servlet#getUrlPatterns() url pattern} for the servlet
	 * this request is sent to contains an asterisk in its path (e.g. /users/*&#47;)
	 * 
	 * @param index The index of the path parameter inside the path.
	 * @return The path parameter at the underlying index.
	 * @throws ArrayIndexOutOfBoundsException If the index for the path parameter is
	 *                                        not within the bounds of the path.
	 */
	public default String getPathParameter(int index) throws ArrayIndexOutOfBoundsException {
		return getPath().split(Pattern.quote("/"))[index];
	}

	/**
	 * Retrieves the Content-Type header contained with the browser's request. This
	 * method may return {@code null} if no content ype was specified.
	 * 
	 * @return The request's Content-Type.
	 * @see HttpServerResponse.ContentType
	 */
	public String getContentType();

	/**
	 * Retrieves the {@link Socket} linked with the current server request.
	 * 
	 * @return The request Socket. Should not be {@code null}.
	 */
	public Socket getSocket();

	/**
	 * Retrieves the current server request as it was received from the browser, as
	 * a string.
	 * 
	 * @return The server request, as a string.
	 */
	public String toString();

	/**
	 * Interface containing constants for HTTP request methods.
	 * 
	 * @see <a href="https://tools.ietf.org/html/rfc2068#section-5.1.1">RFC 2068 |
	 *      Methods</a>
	 *
	 */
	public static interface RequestMethod {
		/**
		 * Represents the HTTP {@code GET} method.
		 */
		public static final String GET = "GET";

		/**
		 * Represents the HTTP {@code HEAD} method.
		 */
		public static final String HEAD = "HEAD";

		/**
		 * Represents the HTTP {@code POST} method.
		 */
		public static final String POST = "POST";

		/**
		 * Represents the HTTP {@code PUT} method.
		 */
		public static final String PUT = "PUT";

		/**
		 * Represents the HTTP {@code DELETE} method.
		 */
		public static final String DELETE = "DELETE";

		/**
		 * Represents the HTTP {@code OPTIONS} method.
		 */
		public static final String OPTIONS = "OPTIONS";

		/**
		 * Represents the HTTP {@code TRACE} method.
		 */
		public static final String TRACE = "TRACE";
	}
}
