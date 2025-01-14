package org.polinux.http.polinux.servlet;

import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.polinux.exceptions.http.polinux.PolinuxHttpRuntimeException;
import org.polinux.exceptions.http.polinux.PolinuxHttpServerRuntimeException;
import org.polinux.http.HttpFormUrlEncodedParameter;
import org.polinux.http.HttpHeader;
import org.polinux.http.HttpMultipartFormParameter;
import org.polinux.http.HttpParameter;
import org.polinux.http.HttpRequestCookie;
import org.polinux.http.HttpServer;
import org.polinux.http.HttpServerRequest;
import org.polinux.http.HttpServerResponse;
import org.polinux.http.HttpSession;
import org.polinux.http.polinux.server.PolinuxHttpClientThread;
import org.polinux.http.polinux.server.PolinuxHttpServer;
import org.polinux.http.polinux.session.PolinuxHttpSession;
import org.polinux.https.polinux.server.PolinuxHttpsClientThread;
import org.polinux.https.polinux.server.PolinuxHttpsServer;
import org.polinux.utils.collections.CollectionUtils;
import org.polinux.utils.enc.URLDecoder;

/**
 * Represents a client request from an incoming {@link PolinuxHttpClientThread
 * client} on a {@link PolinuxHttpServer} or a {@link PolinuxHttpsServer}. The
 * request should have a matching {@link PolinuxHttpServerResponse}.
 * 
 * @see PolinuxHttpServerResponse
 */
public class PolinuxHttpServerRequest implements HttpServerRequest {
	/**
	 * Represents the {@link HttpServerRequest.RequestMethod HTTP method} used in
	 * the client request.
	 * 
	 * @final This field's value should not be changed after being set.
	 */
	protected String method = HttpServerRequest.RequestMethod.GET;

	/**
	 * Represents the {@link HttpHeader HttpHeaders} the client sent during the
	 * request. This list DOES contain {@link HttpRequestCookie cookie} headers.
	 * 
	 * @final This field's value should not be changed after being set.
	 */
	protected List<HttpHeader> headers = new LinkedList<HttpHeader>();

	/**
	 * Represents the {@link HttpRequestCookie HttpRequestCookies} sent by the
	 * client request.
	 * 
	 * @final This field's value should not be changed after being set.
	 * 
	 */
	protected List<HttpRequestCookie> cookies = new LinkedList<HttpRequestCookie>();
	/**
	 * Represents the {@link PolinuxHttpServer} the {@link PolinuxHttpClientThread}
	 * is coming from.
	 *
	 * @see #client
	 */
	protected final PolinuxHttpServer server;

	/**
	 * Represents the {@link PolinuxHttpClientThread} the client request is coming
	 * from.
	 */
	protected final PolinuxHttpClientThread client;

	/**
	 * Represents the {@link PolinuxHttpSession} currently linked to this request.
	 * 
	 * @final This field's value should not be changed after being set.
	 */
	protected PolinuxHttpSession session;

	/**
	 * Map of attributes for the object. These are discarded after the object is
	 * last used (i.e. after it is cleaned up by the GC) and is NOT the same as a
	 * {@link HttpSession#getAttributes() HttpSession attribute}.
	 * 
	 * @final This field's value should not be changed after being set.
	 */
	protected Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	/**
	 * Represents the {@code HTTP version} used the client request. Usually
	 * {@code HTTP/1.1}
	 * 
	 * @final This field's value should not be changed after being set.
	 */
	protected String httpVersion = HttpServer.HTTP_VERSION;

	/**
	 * Represents the request URI path.
	 * 
	 * @final This field's value should not be changed after being set.
	 */
	protected String path = null;

	/**
	 * Represents the client request, as a string
	 * 
	 * @final This field's value should not be changed after being set.
	 * @see #toString()
	 */
	protected String asString = null;

	/**
	 * Represents the list of {@link HttpParameter HttpParameters} inside the client
	 * request, usually containing HTML form parameters.
	 * 
	 * @final This field's value should not be changed after being set.
	 * @see HttpParameter
	 */
	protected List<HttpParameter> parameters = new LinkedList<HttpParameter>();

	/**
	 * Constructs a {@link PolinuxHttpServerRequest}.
	 * 
	 * @param client  The {@link PolinuxHttpClientThread} linked to this request.
	 * @param request The request to parse.
	 */
	public PolinuxHttpServerRequest(final PolinuxHttpClientThread client, final String request) {
		this(client, request, true);
	}

	/**
	 * Constructs a {@link PolinuxHttpServerRequest}.
	 * 
	 * @param client  The {@link PolinuxHttpClientThread} linked to this request.
	 * @param request The request to parse.
	 * @param parse   Whether the {@link PolinuxHttpServerRequest} class should
	 *                parse the request itself. Set to {@code false} if this class
	 *                should not handling parsing (i.e. {@link #parse(String)}
	 *                method will not be called, even if overridden).
	 * @see #parse(String)
	 */
	protected PolinuxHttpServerRequest(PolinuxHttpClientThread client, String request, boolean parse) {
		this.server = client.getServerThread().getServer();
		this.client = client;
		if (parse)
			parse(request);
	}

	/** {@inheritDoc} */
	@Override
	public String getMethod() {
		return this.method;
	}

	/** {@inheritDoc} */
	@Override
	public List<HttpHeader> getHeaders() {
		return this.headers;
	}

	/** {@inheritDoc} */
	@Override
	public String[] getHeaderNames() {
		String[] names = new String[this.getHeaders().size()];

		for (int i = 0; i < this.headers.size(); i++) {
			names[i] = this.headers.get(i).getHeader();
		}

		return names;
	}

	/** {@inheritDoc} */
	@Override
	public PolinuxHttpSession getSession() {
		return this.session;
	}

	/** {@inheritDoc} */
	@Override
	public HttpRequestCookie[] getCookies() {
		return this.cookies.toArray(new HttpRequestCookie[0]);
	}

	/** {@inheritDoc} */
	@Override
	public String getContentType() {
		return this.getHeader("Content-Type") == null ? null : this.getHeader("Content-Type").getValue();
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/** {@inheritDoc} */
	@Override
	public String getUserAgent() {
		return this.getHeader("User-Agent") == null ? null : this.getHeader("User-Agent").getValue();
	}

	/** {@inheritDoc} */
	@Override
	public String getHttpVersion() {
		return this.httpVersion;
	}

	/** {@inheritDoc} */
	@Override
	public HttpHeader getHeader(String name) {
		for (int i = 0; i < this.headers.size(); i++) {
			HttpHeader h = this.headers.get(i);
			if (h.getHeader().equalsIgnoreCase(name))
				return h;
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getHost() {
		return this.getHeader("host") == null ? null : this.getHeader("host").getValue();
	}

	/** {@inheritDoc} */
	@Override
	public String getPath() {
		return this.path;
	}

	/**
	 * Retrieves the current server request as it was received from the browser, as
	 * a string.
	 * 
	 * @return The server request, as a string.
	 */
	public String asString() {
		return this.asString;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return asString();
	}

	/** {@inheritDoc} */
	@Override
	public List<HttpParameter> getParameters() {
		return this.parameters;
	}

	/**
	 * 
	 * Assumes and retrieves the protocol used during the current request.
	 * 
	 * @deprecated This method will assume that if the client's server is an
	 *             {@code instanceof} a {@link PolinuxHttpsServer}, or if the client
	 *             is an {@code instanceof} a {@link PolinuxHttpsClientThread}, that
	 *             the protocol used was {@code https}, which might not necessarily
	 *             be the case. The protocol for a request should be found by other
	 *             means if a sub-class of {@link PolinuxHttpsServer} is being used
	 *             for this class linked server.
	 * 
	 * @return The request protocol. Either {@code http} or {@code https}.
	 */
	@Deprecated
	public String getProtocol() {
		return this.server instanceof PolinuxHttpsServer || this.client instanceof PolinuxHttpsClientThread ? "https"
				: "http";
	}

	/** {@inheritDoc} */
	@Override
	public Socket getSocket() {
		return this.client.getSocket();
	}

	/**
	 * Parses the underlying request to create a {@link PolinuxHttpServerRequest}.
	 * 
	 * @param client  The {@link PolinuxHttpClientThread} linked to this request.
	 * @param request The request to parse.
	 * @return The created {@link PolinuxHttpServerRequest}.
	 */
	public static PolinuxHttpServerRequest parse(final PolinuxHttpClientThread client, final String request) {
		return new PolinuxHttpServerRequest(client, request);
	}

	/**
	 * Parses the underlying request and sets this classes variables to be equal to
	 * the data inside the client request. This method may be overridden by
	 * sub-classes to implements their own parse mechanism.
	 * <p>
	 * If this method was not called in the
	 * {@link #PolinuxHttpServerRequest(PolinuxHttpClientThread, String, boolean)
	 * constructor} (i.e. the request has been parsed for the class yet), this
	 * method MUST be called manually.
	 * 
	 * @param request The request to parse.
	 * @throws PolinuxHttpServerRequestParseException If the underlying client
	 *                                                request is invalid.
	 * @see #PolinuxHttpServerRequest(PolinuxHttpClientThread, String, boolean)
	 */
	protected void parse(String request) throws PolinuxHttpServerRequestParseException {
		if (request == null) {
			PolinuxHttpServerRequestParseException e = new PolinuxHttpServerRequestParseException(
					"Cannot parse null request");
			e.setRequest(request);
			throw e;
		}

		final String lineSeperator = "\r\n";
		final String[] requestSplit = request.split(lineSeperator);

		if (requestSplit.length <= 0) {
			PolinuxHttpServerRequestParseException e = new PolinuxHttpServerRequestParseException("Empty request");
			e.setRequest(request);
			throw e;
		}
		// throw exception & include status code inside exception.

		String method = null;

		String path = null;

		String httpVersion = null;

		final String[] firstLineSplit = requestSplit[0].trim().split(" ");

		List<HttpHeader> headers = new LinkedList<HttpHeader>();

		try {
			method = firstLineSplit[0];
			path = firstLineSplit[1];
			httpVersion = firstLineSplit[2];
			// Check http version
		} catch (RuntimeException e) {
			// throw exception & include status code inside exception.
			throw new PolinuxHttpServerRequestParseException("Invalid method, path, or http version");
		}

		for (int i = 1; i < requestSplit.length; i++) {
			String header = requestSplit[i];
			if (header.trim().length() > 0 && header.trim().contains(":")) {
				HttpHeader realHeader = HttpHeader.parse(header);
				headers.add(realHeader);
			}
		}

		final PolinuxHttpServerRequest r = this;
		r.method = method;
		r.headers = headers;
		r.httpVersion = httpVersion;
		r.path = path;
		r.cookies = r.getHeader("Cookie") == null ? new LinkedList<HttpRequestCookie>()
				: CollectionUtils.toList(HttpRequestCookie.parse(r.getHeader("Cookie").getValue()));

		final HttpRequestCookie requestSessionCookie = r.getCookie(server.getConfiguration().getSessionCookieName());

		PolinuxHttpSession session = PolinuxHttpSession.getSession(requestSessionCookie);

		if (session == null) {
			session = PolinuxHttpSession.createSession(server.getConfiguration());
		}

		r.session = session;

		r.asString = request;

		if (r.getContentType() != null) {
			if (r.getContentType().toLowerCase().contains(HttpServerResponse.ContentType.FORM.toLowerCase())) {
				int index = -1;
				for (int i = 1; i < requestSplit.length; i++) {
					final String dataLine = requestSplit[i];

					if (dataLine.trim().equalsIgnoreCase("")) {
						index = i + 1;
						break;
					}
				}

				if (index <= -1) {
					throw new PolinuxHttpServerRuntimeException(server,
							"Invalid query in request: " + lineSeperator + request);
				}
				String fullQuery;

				try {
					fullQuery = requestSplit[index].trim();
					int currentIndex = index + 1;
					while (currentIndex < requestSplit.length) {
						fullQuery += lineSeperator + requestSplit[currentIndex].trim();
					}
				} catch (IndexOutOfBoundsException e) {
					throw new PolinuxHttpServerRuntimeException(server,
							"Invalid query in request: " + lineSeperator + request);
				}

				final String[] qSplit = fullQuery.split(Pattern.quote("&"));
				URLDecoder decoder = new URLDecoder();
				for (int i = 0; i < qSplit.length; i++) {
					String q = qSplit[i];

					final String name = decoder.decode(q.split("=")[0]);

					String value;
					try {
						value = decoder.decode(q.split("=")[1]);
					} catch (IndexOutOfBoundsException e) {
						value = "";
					}

					HttpFormUrlEncodedParameter param = new HttpFormUrlEncodedParameter(name, value);

					r.parameters.add(param);

				}
			} else if (r.getContentType().toLowerCase()
					.contains(HttpServerResponse.ContentType.FORM_BINARY.toLowerCase())) {
				int index = -1;
				for (int i = 1; i < requestSplit.length; i++) {
					final String dataLine = requestSplit[i];

					if (dataLine.trim().equalsIgnoreCase("")) {
						index = i + 1;
						break;
					}
				}

				if (index <= -1) {
					throw new PolinuxHttpServerRuntimeException(server,
							"Invalid query in request: " + lineSeperator + request);
				}
				String fullQuery;

				try {
					fullQuery = requestSplit[index].trim();
					int currentIndex = index + 1;
					while (currentIndex < requestSplit.length) {
						fullQuery += lineSeperator + requestSplit[currentIndex].trim();
					}
				} catch (IndexOutOfBoundsException e) {
					throw new PolinuxHttpServerRuntimeException(server,
							"Invalid query in request: " + lineSeperator + request);
				}

				final String boundary = r.getContentType().split(";")[1].split("=")[1].trim();

				final String[] qSplit = fullQuery.split(Pattern.quote(boundary));

				for (int i = 1; i < qSplit.length; i++) {
					String q = qSplit[i];

					String[] paramSplit = q.split(lineSeperator);
					HttpHeader contentDisposition = null;
					HttpHeader contentType = null;
					boolean nextContent = false;
					String content = "";

					for (int i1 = 0; i1 < paramSplit.length; i1++) {
						String paramLine = paramSplit[i1];

						if (nextContent) {
							content += paramLine;
							continue;
						}

						String[] paramLineSplit = paramLine.split(":");
						if (paramLine.toLowerCase().startsWith("content-disposition:")) {

							contentDisposition = new HttpHeader("Content-Disposition", paramLineSplit[1].trim());
						}

						if (paramLine.toLowerCase().startsWith("content-type:")) {
							contentType = new HttpHeader("Content-Type", paramLineSplit[1].trim());
						}

						if (paramLine.trim().equalsIgnoreCase("")) {
							nextContent = true;
						}
					}

					String name = "";

					final String[] disSplit = contentDisposition.getValue().split(";");

					for (int i1 = 0; i1 < disSplit.length; i1++) {
						final String part = disSplit[i1].trim();
						if (part.startsWith("name=\"")) {
							name = part.substring(part.indexOf("=\"") + 2, part.length() - 1);
							break;
						}
					}

					HttpMultipartFormParameter param = new HttpMultipartFormParameter(contentDisposition, contentType,
							name, content);
					r.parameters.add(param);
				}
			}
		} else {
			if (r.path.contains("?")) {
				if (r.path.indexOf("?") != r.path.lastIndexOf("?")) {
					throw new PolinuxHttpServerRuntimeException(server,
							"Invalid query in request: " + lineSeperator + request);
				}

				final String fullQuery = r.path.substring(r.path.indexOf("?") + 1);

				final String[] fullQuerySplit = fullQuery.split(Pattern.quote("&"));
				URLDecoder decoder = new URLDecoder();
				for (int i = 0; i < fullQuerySplit.length; i++) {
					final String queryParam = fullQuerySplit[i];

					final String name = decoder.decode(queryParam.split("=")[0]);

					String value;
					try {
						value = decoder.decode(queryParam.split("=")[1]);
					} catch (IndexOutOfBoundsException e) {
						value = "";
					}

					HttpFormUrlEncodedParameter param = new HttpFormUrlEncodedParameter(name, value);

					r.parameters.add(param);

				}

				r.path = r.path.substring(0, r.path.indexOf("?"));

			}

		}

		// r.path = r.path.substring(0, r.path.indexOf('?') - 1);

		// r.contentType = r.getHeader("Content-Type") == null ? null :
		// r.getHeader("Content-Type").getValue();

	}

	/**
	 * Represents a {@link RuntimeException} thrown whenever a client request could
	 * not be properly parsed.
	 */
	public static class PolinuxHttpServerRequestParseException extends PolinuxHttpRuntimeException {
		private static final long serialVersionUID = -3556144298251085728L;

		/**
		 * 
		 */
		protected String request;

		public PolinuxHttpServerRequestParseException() {
			super();
		}

		public PolinuxHttpServerRequestParseException(String message) {
			super(message);
		}

		public PolinuxHttpServerRequestParseException(Throwable cause) {
			super(cause);
		}

		public PolinuxHttpServerRequestParseException(String message, Throwable cause) {
			super(message, cause);
		}

		protected PolinuxHttpServerRequestParseException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		/**
		 * Retrieves the request that caused this error.
		 * 
		 * @return The invalid request.
		 */
		public String getRequest() {
			return request;
		}

		/**
		 * Sets the request that is linked to this error.
		 * 
		 * @param request The new linked request.
		 */
		public void setRequest(String request) {
			this.request = request;
		}

	}
}
