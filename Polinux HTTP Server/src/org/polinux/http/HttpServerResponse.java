package org.polinux.http;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;

import org.polinux.http.servlet.HttpServlet;
import org.polinux.https.HttpsServer;

/**
 * Represents a response to an incoming {@link HttpServerRequest client request}
 * on an {@link HttpServer} or an {@link HttpsServer}.
 * 
 * @see HttpServerRequest
 */
public interface HttpServerResponse extends Attributable, Cookieable, Headerable {
	/**
	 * Retrieves the content type for the {@code HttpServerResponse}
	 * 
	 * @return The current content type for the response.
	 */
	public String getContentType();

	/**
	 * Sets the content type for the {@code HttpServerResponse}.
	 * 
	 * @param type The new content type. Cannot and should not be {@code null}.
	 * @see HttpServerResponse.ContentType
	 */
	public void setContentType(String type);

	/**
	 * Adds an {@link HttpHeader} to the list of known headers.
	 * <p>
	 * If a header with the underlying name already exists, it will be replaced with
	 * the value of the new header. Furthermore, If the underlying header is set to
	 * {@code null}, this method will do nothing.
	 * 
	 * @param header The {@link HttpHeader} to add. Should not be {@code null}.
	 * @see #setHeader(HttpHeader)
	 */
	public default void addHeader(HttpHeader header) {
		setHeader(header);
	}

	/**
	 * Adds an {@link HttpHeader} to the list of known headers.
	 * <p>
	 * If a header with the underlying name already exists, it will be replaced with
	 * the value of the new header. Furthermore, If the underlying header is set to
	 * {@code null}, this method will do nothing.
	 * 
	 * @param header The {@link HttpHeader} to add. Should not be {@code null}.
	 */
	public void setHeader(HttpHeader header);

	/**
	 * Adds the underlying {@code header} and {@code value}
	 * 
	 * @param header The name of the {@link HttpHeader} to add.
	 * @param value  The value for the {@link HttpHeader}, set to {@code null} to
	 *               remove the header.
	 * @see #setHeader(HttpHeader)
	 */
	public default void setHeader(String header, String value) {
		setHeader(new HttpHeader(header, value));
	}

	/**
	 * Removes the header with the underlying name. If a header matching that name
	 * does not exist, this method odes nothing.
	 * 
	 * @param header The name of the header to remove.
	 * @see #setHeader(HttpHeader)
	 */
	public default void removeHeader(String header) {
		setHeader(header, null);
	}

	/**
	 * Retrieves the {@link PrintWriter writer} for the
	 * {@code HTTP Server Response}.
	 * 
	 * @return The server response's writer.
	 */
	public PrintWriter getWriter();

	/**
	 * Retrieves all the known {@link HttpResponseCookie cookies} for this object.
	 * If there are no {@code cookies} associated with this object, this method will
	 * return an empty array (not null).
	 * 
	 * @return An {@link HttpResponseCookie}{@code []} containing all the object's
	 *         cookies.
	 */
	@Override
	public HttpResponseCookie[] getCookies();

	/**
	 * Retrieves {@link HttpResponseCookie cookies} with the underlying name from
	 * the {@link #getCookies() known cookies}. This method is
	 * {@code case sensitive}, so make sure your cookie name matches
	 * {@code perfectly} the underlying name.
	 */
	@Override
	public default HttpResponseCookie getCookie(String name) {
		for (int i = 0; i < getCookies().length; i++) {
			HttpResponseCookie cookie = getCookies()[i];

			if (cookie.getName().equals(name))
				return cookie;
		}
		return null;
	}

	/**
	 * Retrieves the names of all the known {@link HttpResponseCookie cookies} for
	 * this object. If there are no {@code cookies} associated with this object,
	 * this method will return an empty array (not null).
	 */
	@Override
	public default String[] getCookieNames() {
		if (getCookies().length <= 0)
			return new String[] {};
		final String[] names = new String[getCookies().length];

		for (int i = 0; i < getCookies().length; i++) {
			names[i] = getCookies()[i].getName();
		}

		return names;
	}

	/**
	 * Adds an {@link HttpResponseCookie} to the list of cookies for the server
	 * response. If the underlying cookie is {@code null}, this method will not do
	 * anything.
	 * 
	 * @param cookie The cookie to add. Should not be {@code null}.
	 */
	public void addCookie(HttpResponseCookie cookie);

	/**
	 * Transforms the underlying values into an {@link HttpResponseCookie} and adds
	 * it to the list of cookies for the server response.
	 * 
	 * @param name     The name of the cookie. Should not be {@code null}.
	 * @param value    The value of the cookie. Should not be {@code null}.
	 * @param maxAge   How long the cookie should last before it expires (in
	 *                 seconds). A zero or negative number will expire the cookie
	 *                 immediately. Should not be {@code null}.
	 * @param httpOnly Whether the cookie should only be able to be viewed in
	 *                 {@link HttpHeader Http Headers}, and not scripts (JavaScript,
	 *                 XSS, XMLHttpRequest, etc.)
	 * @param secure   Whether the current cookie should be sent over a
	 *                 {@code secure} connection or not.
	 * @return The created {@link HttpResponseCookie}
	 * @see #addCookie(HttpResponseCookie)
	 */
	public default HttpResponseCookie addCookie(String name, String value, Integer maxAge, boolean httpOnly,
			boolean secure) {
		HttpResponseCookie cookie = new HttpResponseCookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(httpOnly);
		cookie.setSecure(secure);
		addCookie(cookie);
		return cookie;
	}

	/**
	 * Removes the underlying {@link HttpResponseCookie} from the list of cookies to
	 * send inside the server response if a cookie with the underlying name and
	 * value exists.
	 * 
	 * @param cookie The {@link HttpResponseCookie} to remove. Should not be
	 *               {@code null}.
	 * @see #removeCookie(String)
	 */
	public default void removeCookie(HttpResponseCookie cookie) {
		if (containsCookie(cookie))
			removeCookie(cookie.getName());
	}

	/**
	 * Removes the {@link HttpResponseCookie} with the underlying name from the list
	 * of cookies to send inside the server response if a cookie with the underlying
	 * name exists.
	 * 
	 * @param name The name of the cookie, {@code case sensitive}. Should not be
	 *             {@code null}.
	 */
	public void removeCookie(String name);

	/**
	 * Retrieves the current {@code status code} that will be sent to the client at
	 * the end of the server response.
	 * 
	 * @return The current server response's status code.
	 * @see HttpServerResponse.StatusCode
	 */
	public int getStatus();

	/**
	 * Sets the current {@code status code} that will be sent to the client at the
	 * end of the server response. Changing this also changes the current
	 * {@link #getStatusMessage() status message} to the matching message. If a
	 * {@link HttpServlet servlet} was found, the default status code is
	 * {@link HttpServerResponse.StatusCode#OK 200}.
	 * 
	 * @param status The new server status code.
	 * @see HttpServerResponse.StatusCode
	 * @see #setStatusMessage(String)
	 */
	public void setStatus(int status);

	/**
	 * Retrieves the current status message that will be sent to the client when the
	 * server response is sent. This is usually the status message matching the
	 * {@link #getStatus() current status}.
	 * 
	 * @return The current status message.
	 */
	public String getStatusMessage();

	/**
	 * Sets the current status message. This does not change the current
	 * {@link #getStatus() status code}, and should not be touched to let
	 * {@link #setStatus(int)} change the status message automatically.
	 * 
	 * @param statusMessage The new HTTP status message.
	 * @see HttpServerResponse.StatusCode#getStatusMessage(int)
	 */
	public void setStatusMessage(String statusMessage);

	/**
	 * Retrieves the {@link HttpServer} that the client is sending this request to.
	 * 
	 * @return The linked {@link HttpServer}. Should not be {@code null}.
	 */
	public HttpServer getServer();

	/**
	 * Retrieves the Content-Length of the current HTTP response body.
	 * 
	 * @return The current content length.
	 */
	public Integer getContentLength();

	/**
	 * Retrieves the current charset for the server's response. If no charset has
	 * been set, this method returns {@code null}.
	 * 
	 * @return The response's charset.
	 */
	public java.nio.charset.Charset getCharset();

	/**
	 * Retrieves the {@link Socket} linked with the current server response.
	 * 
	 * @return The {@link Socket} that will receive the current server response.
	 *         Should not be {@code null}.
	 */
	public Socket getSocket();

	/**
	 * Retrieves the current HTTP server response, as a string. This response
	 * contains the current response data.
	 * 
	 * @return The server response, as a string.
	 */
	public String toString();

	/**
	 * Interface containing constants for common Content Type Mime Types.
	 */
	public static interface ContentType {
		/**
		 * Represents HTML content (.html, .htm, .xhtml)
		 * <p>
		 * 
		 * @value "text/html"
		 */
		public static final String HTML = "text/html";

		/**
		 * Represents jpeg image (.jpeg, .jpg)
		 * <p>
		 * 
		 * @value "image/jpeg"
		 */
		public static final String JPEG = "image/jpeg";

		/**
		 * Represents jpeg image (.jpeg, .jpg)
		 * <p>
		 * 
		 * @value "image/jpeg"
		 * 
		 * @see #JPEG
		 */
		public static final String JPG = JPEG;

		/**
		 * Represents png image (.png)
		 * <p>
		 * 
		 * @value "image/png"
		 * 
		 */
		public static final String PNG = "image/png";

		/**
		 * Represents gif image (.gif)
		 * <p>
		 * 
		 * @value "image/gif"
		 */
		public static final String GIF = "image/gif";

		/**
		 * Represents BMP image (.bmp)
		 * <p>
		 * 
		 * @value "image/bmp image/x-bmp"
		 */
		public static final String BMP = "image/bmp image/x-bmp";

		/**
		 * Represents mpeg audio (.mp3)
		 * <p>
		 * 
		 * @value "audio/mpeg"
		 */
		public static final String MP3 = "audio/mpeg";

		/**
		 * Represents mp4 <b><i>audio</i></b> (.mp4)
		 * <p>
		 * 
		 * @value "audio/mp4"
		 * 
		 * @see #MP4_VIDEO
		 */
		public static final String MP4_AUDIO = "audio/mp4";

		/**
		 * Represents mp4 <b><i>video</i></b> (.mp4)
		 * <p>
		 * 
		 * @value "video/mp4"
		 * 
		 * @see #MP4_AUDIO
		 */
		public static final String MP4_VIDEO = "video/mp4";

		/**
		 * Represents wav audio (.wav)
		 * <p>
		 * 
		 * @value "audio/wav"
		 */
		public static final String WAV = "audio/wav";

		/**
		 * Represents wav audio (.wav)
		 * <p>
		 * 
		 * @value "audio/wav-x"
		 * @see #WAV
		 */
		public static final String WAVX = "audio/wav-x";

		/**
		 * Represents jar file (.jar)
		 * <p>
		 * 
		 * @value "application/java"
		 */
		public static final String JAR = "application/java";

		/**
		 * Represents jar file (.jar)
		 * <p>
		 * 
		 * @value "application/java-archive"
		 * @see #JAR
		 */
		public static final String JAR_ARCHIVE = "application/java-archive";

		/**
		 * Represents jar file (.jar)
		 * <p>
		 * 
		 * @value "application/x-java-archive"
		 * @see #JAR_ARCHIVE
		 */
		public static final String JARX_ARCHIVE = "application/x-java-archive";

		/**
		 * Represents plain text (.txt)
		 * <p>
		 * 
		 * @value "text/plain"
		 */
		public static final String PLAIN = "text/plain";

		/**
		 * Represents plain text (.txt)
		 * <p>
		 * 
		 * @value "text/plain"
		 * @see #PLAIN
		 */
		public static final String TXT = PLAIN;

		/**
		 * Represents XHTML content (.xhtml, .xml)
		 * <p>
		 * 
		 * @value "application/xhtml+xml"
		 */
		public static final String XHTML = "application/xhtml+xml";

		/**
		 * Represents XML content (.xml)
		 * <p>
		 * 
		 * @value "application/xml"
		 */
		public static final String XML = "application/xml";

		/**
		 * Represents zip file (.zip)
		 * <p>
		 * 
		 * @value "application/zip"
		 */

		public static final String ZIP = "application/zip";

		/**
		 * Represents JSON content (.json)
		 * <p>
		 * 
		 * @value "application/json"
		 */
		public static final String JSON = "application/json";

		/**
		 * Represents alphanumeric form data.
		 * <p>
		 * 
		 * @value "application/x-www-form-urlencoded"
		 */
		public static final String FORM = "application/x-www-form-urlencoded";

		/**
		 * Represents non-alphanumeric form data.
		 * <p>
		 * 
		 * @value "multipart/form-data"
		 */
		public static final String FORM_BINARY = "multipart/form-data";

	}

	/**
	 * Interface containing constants for commonly used HTTP status codes.
	 * 
	 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
	 *      status codes</a>
	 */
	public static interface StatusCode {

		/* Informational status codes (1xx) */
		/*
		 * This class of status code indicates a provisional response, consisting only
		 * of the Status-Line and optional headers, and is terminated by an empty line.
		 * There are no required headers for this class of status code. Since HTTP/1.0
		 * did not define any 1xx status codes, servers MUST NOT send a 1xx response to
		 * an HTTP/1.0 client except under experimental conditions.
		 * 
		 * A client MUST be prepared to accept one or more 1xx status responses prior to
		 * a regular response, even if the client does not expect a 100 (Continue)
		 * status message. Unexpected 1xx status responses MAY be ignored by a user
		 * agent.
		 * 
		 * Proxies MUST forward 1xx responses, unless the connection between the proxy
		 * and its client has been closed, or unless the proxy itself requested the
		 * generation of the 1xx response. (For example, if a proxy adds a
		 * "Expect: 100-continue" field when it forwards a request, then it need not
		 * forward the corresponding 100 (Continue) response(s).)
		 */
		/**
		 * Represents the <i>CONTINUE</i> informational status code.
		 * <p>
		 * This status code means that the server has received the request headers, and
		 * that the client should proceed to send the request body (in the case of a
		 * request for which a body needs to be sent; for example, a POST request). If
		 * the request body is large, sending it to a server when a request has already
		 * been rejected based upon inappropriate headers is inefficient. To have a
		 * server check if the request could be accepted based on the request's headers
		 * alone, a client must send Expect: 100-continue as a header in its initial
		 * request and check if a 100 Continue status code is received in response
		 * before continuing (or receive 417 Expectation Failed and not continue).
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int CONTINUE = 100;

		/**
		 * Represents the <i>SWITCHING PROTOCOLS</i> informational status code.
		 * <p>
		 * This status code means that the server understands and is willing to comply
		 * with the client's request, via the Upgrade message header field, for a change
		 * in the application protocol being used on this connection. The server will
		 * switch protocols to those defined by the response's Upgrade header field
		 * immediately after the empty line which terminates the 101 response.
		 * <p>
		 * 
		 * The protocol SHOULD be switched only when it is advantageous to do so. For
		 * example, switching to a newer version of HTTP is advantageous over older
		 * versions, and switching to a real-time, synchronous protocol might be
		 * advantageous when delivering resources that use such features.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int SWITCHING_PROTOCOLS = 101;

		/**
		 * Represents the <i>PROCESSING</i> informational status code (for WebDAV).
		 * <p>
		 * The 102 (Processing) status code is an interim response used to inform the
		 * client that the server has accepted the complete request, but has not yet
		 * completed it. This status code SHOULD only be sent when the server has a
		 * reasonable expectation that the request will take significant time to
		 * complete. As guidance, if a method is taking longer than 20 seconds (a
		 * reasonable, but arbitrary value) to process the server SHOULD return a 102
		 * (Processing) response. The server MUST send a final response after the
		 * request has been completed.
		 * <p>
		 * 
		 * Methods can potentially take a long period of time to process, especially
		 * methods that support the Depth header. In such cases the client may time-out
		 * the connection while waiting for a response. To prevent this the server may
		 * return a 102 (Processing) status code to indicate to the client that the
		 * server is still processing the method.
		 * <p>
		 * <i>As a WebDAV request may contain many sub-requests involving file
		 * operations, it may take a long time to complete the request. This code
		 * indicates that the server has received and is processing the request, but no
		 * response is available yet. This prevents the client from timing out and
		 * assuming the request was lost.</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int PROCESSING_WEBDAV = 102;

		/*
		 * Success status codes (2xx).
		 */
		/*
		 * This class of status code indicates that the client's request was
		 * successfully received, understood, and accepted.
		 */

		/**
		 * Represents the <i>OK</i> success status code.
		 * <p>
		 * This status code means that the request has succeeded. The information
		 * returned with the response is dependent on the method used in the request,
		 * for example:
		 * <ul>
		 * <li>GET an entity corresponding to the requested resource is sent in the
		 * response;
		 * <li>HEAD the entity-header fields corresponding to the requested resource are
		 * sent in the response without any message-body;
		 * <li>POST an entity describing or containing the result of the action;
		 * <li>TRACE an entity containing the request message as received by the end
		 * server.
		 * </ul>
		 * <i>&#10022; This is a general status code. Most common code used to indicate
		 * success.</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int OK = 200;

		/**
		 * Represents the <i>CREATED</i> success status code.
		 * <p>
		 * This status code means that the request has been fulfilled and resulted in a
		 * new resource being created. The newly created resource can be referenced by
		 * the URI(s) returned in the entity of the response, with the most specific URI
		 * for the resource given by a Location header field. The response SHOULD
		 * include an entity containing a list of resource characteristics and
		 * location(s) from which the user or user agent can choose the one most
		 * appropriate. The entity format is specified by the media type given in the
		 * Content-Type header field. The origin server MUST create the resource before
		 * returning the 201 status code. If the action cannot be carried out
		 * immediately, the server SHOULD respond with {@link #ACCEPTED 202} (Accepted)
		 * response instead.
		 * <p>
		 * 
		 * A 201 response MAY contain an ETag response header field indicating the
		 * current value of the entity tag for the requested variant just created.
		 * <p>
		 * <i>&#10022; Successful creation occurred (via either POST or PUT). Set the
		 * Location header to contain a link to the newly-created resource (on POST).
		 * Response body content may or may not be present.</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int CREATED = 201;

		/**
		 * Represents the <i>ACCEPTED</i> success status code.
		 * <p>
		 * This status code means that the request has been accepted for processing, but
		 * the processing has not been completed. The request might or might not
		 * eventually be acted upon, as it might be disallowed when processing actually
		 * takes place. There is no facility for re-sending a status code from an
		 * asynchronous operation such as this.
		 * <p>
		 * 
		 * The 202 response is intentionally non-committal. Its purpose is to allow a
		 * server to accept a request for some other process (perhaps a batch-oriented
		 * process that is only run once per day) without requiring that the user
		 * agent's connection to the server persist until the process is completed. The
		 * entity returned with this response SHOULD include an indication of the
		 * request's current status and either a pointer to a status monitor or some
		 * estimate of when the user can expect the request to be fulfilled.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int ACCEPTED = 202;

		/**
		 * Represents the <i>NON AUTHORITATIVE INFORMATION</i> success status code.
		 * <p>
		 * This status code means the returned metainformation in the entity-header is
		 * not the definitive set as available from the origin server, but is gathered
		 * from a local or a third-party copy. The set presented MAY be a subset or
		 * superset of the original version. For example, including local annotation
		 * information about the resource might result in a superset of the
		 * metainformation known by the origin server. Use of this response code is not
		 * required and is only appropriate when the response would otherwise be 200
		 * (OK).
		 * <p>
		 * <i>&#10022; Not present in HTTP/1.0: available since HTTP/1.1</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int NON_AUTHORITATIVE_INFORMATION = 203;

		/**
		 * Represents the <i>NO CONTENT</i> success status code.
		 * <p>
		 * This status code means that the server has fulfilled the request but does not
		 * need to return an entity-body, and might want to return updated
		 * metainformation. The response MAY include new or updated metainformation in
		 * the form of entity-headers, which if present SHOULD be associated with the
		 * requested variant.
		 * <p>
		 * 
		 * If the client is a user agent, it SHOULD NOT change its document view from
		 * that which caused the request to be sent. This response is primarily intended
		 * to allow input for actions to take place without causing a change to the user
		 * agent's active document view, although any new or updated metainformation
		 * SHOULD be applied to the document currently in the user agent's active view.
		 * <p>
		 * 
		 * The 204 response MUST NOT include a message-body, and thus is always
		 * terminated by the first empty line after the header fields.
		 * <p>
		 * 
		 * <i>&#10022; Status when wrapped responses (e.g. JSEND) are not used and
		 * nothing is in the body (e.g. DELETE).</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int NO_CONTENT = 204;

		/**
		 * Represents the <i>RESET CONTENT</i> success status code.
		 * <p>
		 * This status code means that the server has fulfilled the request and the user
		 * agent SHOULD reset the document view which caused the request to be sent.
		 * This response is primarily intended to allow input for actions to take place
		 * via user input, followed by a clearing of the form in which the input is
		 * given so that the user can easily initiate another input action. The response
		 * MUST NOT include an entity.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int RESET_CONTENT = 205;

		/**
		 * Represents the <i>PARTIAL CONTENT</i> success status code.
		 * <p>
		 * This status code means that the server has fulfilled the partial GET request
		 * for the resource. The request MUST have included a Range header field
		 * indicating the desired range, and MAY have included an If-Range header field
		 * to make the request conditional.
		 * <p>
		 * 
		 * The response MUST include the following header fields:
		 * <p>
		 * <ul>
		 * 
		 * <li>Either a Content-Range header field indicating the range included with
		 * this response, or a multipart/byteranges Content-Type including Content-Range
		 * fields for each part. If a Content-Length header field is present in the
		 * response, its value MUST match the actual number of OCTETs transmitted in the
		 * message-body.
		 * <li>Date
		 * <li>ETag and/or Content-Location, if the header would have been sent in a 200
		 * response to the same request
		 * <li>Expires, Cache-Control, and/or Vary, if the field-value might differ from
		 * that sent in any previous response for the same variant
		 * </ul>
		 * <p>
		 * If the 206 response is the result of an If-Range request that used a strong
		 * cache validator (see section 13.3.3), the response SHOULD NOT include other
		 * entity-headers. If the response is the result of an If-Range request that
		 * used a weak validator, the response MUST NOT include other entity-headers;
		 * this prevents inconsistencies between cached entity-bodies and updated
		 * headers. Otherwise, the response MUST include all of the entity-headers that
		 * would have been returned with a 200 (OK) response to the same request.
		 *
		 * <p>
		 * 
		 * A cache MUST NOT combine a 206 response with other previously cached content
		 * if the ETag or Last-Modified headers do not match exactly.
		 * <p>
		 * 
		 * A cache that does not support the Range and Content-Range headers MUST NOT
		 * cache 206 (Partial) responses.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int PARTIAL_CONTENT = 206;

		/**
		 * Represents the <i>MULTI STATUS</i> success status code (for WebDAV).
		 * <p>
		 * This status code means that the message body that follows is an XML message
		 * and can contain a number of separate response codes, depending on how many
		 * sub-requests were made.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int MULTI_STATUS_WEBDAV = 207;

		/**
		 * Represents the <i>ALREADY REPORTEDS</i> success status code (for WebDAV).
		 * <p>
		 * The 208 (Already Reported) status code can be used inside a DAV: propstat
		 * response element to avoid enumerating the internal members of multiple
		 * bindings to the same collection repeatedly. For each binding to a collection
		 * inside the request's scope, only one will be reported with a 200 status,
		 * while subsequent DAV:response elements for all other bindings will use the
		 * 208 status, and no DAV:response elements for their descendants are included.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int ALREADY_REPORTED_WEBDAV = 208;

		/**
		 * Represents the <i>IM USED</i> success status code.
		 * <p>
		 * This status code means that the server has fulfilled a GET request for the
		 * resource, and the response is a representation of the result of one or more
		 * instance-manipulations applied to the current instance. The actual current
		 * instance might not be available except by combining this response with other
		 * previous or future responses, as appropriate for the specific
		 * instance-manipulation(s). If so, the headers of the resulting instance are
		 * the result of combining the headers from the status-226 response and the
		 * other instances.
		 * <p>
		 * 
		 * The request MUST have included an A-IM header field listing at least one
		 * instance-manipulation. The response MUST include an Etag header field giving
		 * the entity tag of the current instance.
		 * <p>
		 * 
		 * A response received with a status code of 226 MAY be stored by a cache and
		 * used in reply to a subsequent request, subject to the HTTP expiration
		 * mechanism and any Cache-Control headers.
		 * <p>
		 * 
		 * A response received with a status code of 226 MAY be used by a cache, in
		 * conjunction with a cache entry for the base instance, to create a cache entry
		 * for the current instance.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int IM_USED = 226;

		/* Redirection status codes (3xx) */
		/*
		 * This class of status code indicates that further action needs to be taken by
		 * the user agent in order to fulfill the request. The action required MAY be
		 * carried out by the user agent without interaction with the user if and only
		 * if the method used in the second request is GET or HEAD. A client SHOULD
		 * detect infinite redirection loops, since such loops generate network traffic
		 * for each redirection.
		 * 
		 * Note: previous versions of this specification recommended a maximum of five
		 * redirections. Content developers should be aware that there might be clients
		 * that implement such a fixed limitation.
		 */
		/**
		 * Represents the <i>MULTIPLE CHOICES</i> redirection status code.
		 * <p>
		 * This status code means the requested resource corresponds to any one of a set
		 * of representations, each with its own specific location, and agent- driven
		 * negotiation information is being provided so that the user (or user agent)
		 * can select a preferred representation and redirect its request to that
		 * location.
		 * <p>
		 * 
		 * Unless it was a HEAD request, the response SHOULD include an entity
		 * containing a list of resource characteristics and location(s) from which the
		 * user or user agent can choose the one most appropriate. The entity format is
		 * specified by the media type given in the Content- Type header field.
		 * Depending upon the format and the capabilities of the user agent, selection
		 * of the most appropriate choice MAY be performed automatically. However, this
		 * specification does not define any standard for such automatic selection.
		 * <p>
		 * 
		 * If the server has a preferred choice of representation, it SHOULD include the
		 * specific URI for that representation in the Location field; user agents MAY
		 * use the Location field value for automatic redirection. This response is
		 * cacheable unless indicated otherwise.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int MULTIPLE_CHOICES = 300;

		/**
		 * Represents the <i>MOVED PERMANENTLY</i> redirection status code.
		 * <p>
		 * This status code means that the requested resource has been assigned a new
		 * permanent URI and any future references to this resource SHOULD use one of
		 * the returned URIs. Clients with link editing capabilities ought to
		 * automatically re-link references to the Request-URI to one or more of the new
		 * references returned by the server, where possible. This response is cacheable
		 * unless indicated otherwise.
		 * <p>
		 * 
		 * The new permanent URI SHOULD be given by the Location field in the response.
		 * Unless the request method was HEAD, the entity of the response SHOULD contain
		 * a short hypertext note with a hyperlink to the new URI(s).
		 * <p>
		 * 
		 * If the 301 status code is received in response to a request other than GET or
		 * HEAD, the user agent MUST NOT automatically redirect the request unless it
		 * can be confirmed by the user, since this might change the conditions under
		 * which the request was issued.
		 * <p>
		 * 
		 * <blockquote> <b>Note:</b> When automatically redirecting a POST request after
		 * receiving a 301 status code, some exist </blockquote>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int MOVED_PERMANENTLY = 301;

		/**
		 * Represents the <i>FOUND</i> redirection status code.
		 * <p>
		 * This status code means the requested resource resides temporarily under a
		 * different URI. Since the redirection might be altered on occasion, the client
		 * SHOULD continue to use the Request-URI for future requests. This response is
		 * only cacheable if indicated by a Cache-Control or Expires header field.
		 * <p>
		 * 
		 * The temporary URI SHOULD be given by the Location field in the response.
		 * Unless the request method was HEAD, the entity of the response SHOULD contain
		 * a short hypertext note with a hyperlink to the new URI(s).
		 * <p>
		 * 
		 * If the 302 status code is received in response to a request other than GET or
		 * HEAD, the user agent MUST NOT automatically redirect the request unless it
		 * can be confirmed by the user, since this might change the conditions under
		 * which the request was issued.
		 * <p>
		 * <blockquote> <b>Note:</b> RFC 1945 and RFC 2068 specify that the client is
		 * not allowed to change the method on the redirected request. However, most
		 * existing user agent implementations treat 302 as if it were a
		 * {@link #SEE_OTHER 303} response, performing a GET on the Location field-value
		 * regardless of the original request method. The status codes {@link #SEE_OTHER
		 * 303} and {@link #TEMPORARY_REDIRECT 307} have been added for servers that
		 * wish to make unambiguously clear which kind of reaction is expected of the
		 * client. </blockquote>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int FOUND = 302;

		/**
		 * Represents the <i>SEE OTHER</i> redirection status code.
		 * <p>
		 * This status code means that The response to the request can be found under a
		 * different URI and SHOULD be retrieved using a GET method on that resource.
		 * This method exists primarily to allow the output of a POST-activated script
		 * to redirect the user agent to a selected resource. The new URI is not a
		 * substitute reference for the originally requested resource. The 303 response
		 * MUST NOT be cached, but the response to the second (redirected) request might
		 * be cacheable.
		 * <p>
		 * 
		 * The different URI SHOULD be given by the Location field in the response.
		 * Unless the request method was HEAD, the entity of the response SHOULD contain
		 * a short hypertext note with a hyperlink to the new URI(s).
		 * <p>
		 * <blockquote> <b>Note:</b> Many pre-HTTP/1.1 user agents do not understand the
		 * 303 status. When interoperability with such clients is a concern, the
		 * {@link #FOUND 302} status code may be used instead, since most user agents
		 * react to a {@link #FOUND 302} response as described here for 303.
		 * </blockquote>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int SEE_OTHER = 303;

		/**
		 * Represents the <i>NOT MODIFIED</i> redirection status code.
		 * <p>
		 * This status code means: If the client has performed a conditional GET request
		 * and access is allowed, but the document has not been modified, the server
		 * SHOULD respond with this status code. The 304 response MUST NOT contain a
		 * message-body, and thus is always terminated by the first empty line after the
		 * header fields.
		 * <p>
		 * 
		 * The response MUST include the following header fields:
		 * <p>
		 * <ul>
		 * Date, unless its omission is required by section 14.18.1 (RFC)
		 * </ul>
		 * <p>
		 * If a clockless origin server obeys these rules, and proxies and clients add
		 * their own Date to any response received without one (as already specified by
		 * [RFC 2068], section 14.19), caches will operate correctly.
		 * <p>
		 * <ul>
		 * <li>ETag and/or Content-Location, if the header would have been sent in a
		 * {@link #OK 200} response to the same request
		 * <li>Expires, Cache-Control, and/or Vary, if the field-value might differ from
		 * that sent in any previous response for the same variant
		 * </ul>
		 * <p>
		 * If the conditional GET used a strong cache validator, the response SHOULD NOT
		 * include other entity-headers. Otherwise (i.e., the conditional GET used a
		 * weak validator), the response MUST NOT include other entity-headers; this
		 * prevents inconsistencies between cached entity-bodies and updated headers.
		 * <p>
		 * 
		 * If a 304 response indicates an entity not currently cached, then the cache
		 * MUST disregard the response and repeat the request without the conditional.
		 * <p>
		 * 
		 * If a cache uses a received 304 response to update a cache entry, the cache
		 * MUST update the entry to reflect any new field values given in the response.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int NOT_MODIFIED = 304;

		/**
		 * Represents the <i>USE PROXY</i> redirection status code.
		 * <p>
		 * The status code means that the requested resource MUST be accessed through
		 * the proxy given by the Location field. The Location field gives the URI of
		 * the proxy. The recipient is expected to repeat this single request via the
		 * proxy. 305 responses MUST only be generated by origin servers.
		 * <p>
		 * <blockquote> <b>Note:</b> RFC 2068 was not clear that 305 was intended to
		 * redirect a single request, and to be generated by origin servers only. Not
		 * observing these limitations has significant security
		 * consequences.</blockquote>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int USE_PROXY = 305;

		/**
		 * Represents the <i>SWITCH PROXY</i> redirection status code.
		 * <p>
		 * No longer used. Originally meant "Subsequent requests should use the
		 * specified proxy."
		 * 
		 * @deprecated The 306 status code was used in a previous version of the
		 *             specification, is no longer used, and the code is reserved.
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int SWITCH_PROXY = 306;

		/**
		 * Represents the <i>TEMPORARY REDIRECT</i> redirection status code.
		 * <p>
		 * This status code indicates that, in this case, the request should be repeated
		 * with another URI; however, future requests should still use the original URI.
		 * In contrast to how {@link #FOUND 302} was historically implemented, the
		 * request method is not allowed to be changed when reissuing the original
		 * request. For example, a POST request should be repeated using another POST
		 * request.
		 * 
		 * @see <a href=
		 *      "https://en.wikipedia.org/wiki/List_of_HTTP_status_codes#307">List of
		 *      HTTP Status codes - Wikipedia</a>
		 */
		public static final int TEMPORARY_REDIRECT = 307;

		/**
		 * Represents the <i>PERMANENT REDIRECT</i> redirection status code.
		 * <p>
		 * This status code indicates that the request and all future requests should be
		 * repeated using another URI. {@link #TEMPORARY_REDIRECT 307} and
		 * {@link #PERMANENT_REDIRECT 308} parallel the behaviors of {@link #FOUND 302}
		 * and {@link #MOVED_PERMANENTLY 301}, but do not allow the HTTP method to
		 * change. So, for example, submitting a form to a permanently redirected
		 * resource may continue smoothly.
		 * 
		 * @see <a href=
		 *      "https://en.wikipedia.org/wiki/List_of_HTTP_status_codes#308">List of
		 *      HTTP Status codes - Wikipedia</a>
		 */
		public static final int PERMANENT_REDIRECT = 308;

		/* Client error status codes (4xx) */
		/*
		 * This class of status code indicates that further action needs to be taken by
		 * the user agent in order to fulfill the request. The action required MAY be
		 * carried out by the user agent without interaction with the user if and only
		 * if the method used in the second request is GET or HEAD. A client SHOULD
		 * detect infinite redirection loops, since such loops generate network traffic
		 * for each redirection.
		 * 
		 * Note: previous versions of this specification recommended a maximum of five
		 * redirections. Content developers should be aware that there might be clients
		 * that implement such a fixed limitation.
		 */
		/**
		 * Represents the <i>BAD REQUEST</i> client error status code.
		 * <p>
		 * This status code means the the request could not be understood by the server
		 * due to malformed syntax. The client SHOULD NOT repeat the request without
		 * modifications.
		 * <p>
		 * 
		 * <i>&#10022; General error when fulfilling the request would cause an invalid
		 * state. Domain validation errors, missing data, etc. are some examples.</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int BAD_REQUEST = 400;

		/**
		 * Represents the <i>UNAUTHORIZED</i> client error status code.
		 * <p>
		 * This status code means that The request requires user authentication. The
		 * response MUST include a WWW-Authenticate header field containing a challenge
		 * applicable to the requested resource. The client MAY repeat the request with
		 * a suitable Authorization header field. If the request already included
		 * Authorization credentials, then the 401 response indicates that authorization
		 * has been refused for those credentials. If the 401 response contains the same
		 * challenge as the prior response, and the user agent has already attempted
		 * authentication at least once, then the user SHOULD be presented the entity
		 * that was given in the response, since that entity might include relevant
		 * diagnostic information. HTTP access authentication is explained in "HTTP
		 * Authentication: Basic and Digest Access Authentication".
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int UNAUTHORIZED = 401;

		/**
		 * Represents the <i>PAYMENT REQUIRED</i> client error status code.
		 * <p>
		 * This status code is reserved for future use. The original intention was that
		 * this code might be used as part of some form of digital cash or micropayment
		 * scheme, but that has not happened, and this code is not usually used. As an
		 * example of its use, however, Apple's MobileMe service generates a 402 error
		 * ("httpStatusCode:402" in the Mac OS X Console log) if the MobileMe account is
		 * delinquent.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int PAYMENT_REQUIRED = 402;

		/**
		 * Represents the <i>FORBIDDEN</i> client error status code.
		 * <p>
		 * The server understood the request, but is refusing to fulfill it.
		 * Authorization will not help and the request SHOULD NOT be repeated. If the
		 * request method was not HEAD and the server wishes to make public why the
		 * request has not been fulfilled, it SHOULD describe the reason for the refusal
		 * in the entity. If the server does not wish to make this information available
		 * to the client, the status code {@link #NOT_FOUND 404} (Not Found) can be used
		 * instead.
		 * <p>
		 * <i>&#10022; Error code for user not authorized to perform the operation or
		 * the resource is unavailable for some reason (e.g. time constraints,
		 * etc.).</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int FORBIDDEN = 403;

		/**
		 * Represents the <i>NOT FOUND</i> client error status code (404).
		 * <p>
		 * The server has not found anything matching the Request-URI. No indication is
		 * given of whether the condition is temporary or permanent. The {@link #GONE
		 * 410} (Gone) status code SHOULD be used if the server knows, through some
		 * internally configurable mechanism, that an old resource is permanently
		 * unavailable and has no forwarding address. This status code is commonly used
		 * when the server does not wish to reveal exactly why the request has been
		 * refused, or when no other response is applicable.
		 * <p>
		 * <i>&#10022; Used when the requested resource is not found, whether it doesn't
		 * exist or if there was a {@link #UNAUTHORIZED 401} or {@link #FORBIDDEN 403}
		 * that, for security reasons, the service wants to mask.</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int NOT_FOUND = 404;

		/**
		 * Represents the <i>METHOD NOT ALLOWED</i> client error status code.
		 * <p>
		 * This status code is indicating that a request was made of a resource using a
		 * request method not supported by that resource; for example, using GET on a
		 * form which requires data to be presented via POST, or using PUT on a
		 * read-only resource.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int METHOD_NOT_ALLOWED = 405;

		/**
		 * Represents the <i>NOT ACCEPTABLE</i> client error status code.
		 * <p>
		 * The status code indicates that the resource identified by the request is only
		 * capable of generating response entities which have content characteristics
		 * not acceptable according to the accept headers sent in the request.
		 * <p>
		 * 
		 * Unless it was a HEAD request, the response SHOULD include an entity
		 * containing a list of available entity characteristics and location(s) from
		 * which the user or user agent can choose the one most appropriate. The entity
		 * format is specified by the media type given in the Content-Type header field.
		 * Depending upon the format and the capabilities of the user agent, selection
		 * of the most appropriate choice MAY be performed automatically. However, this
		 * specification does not define any standard for such automatic selection.
		 * <p>
		 * 
		 * <blockquote><b>Note:</b> HTTP/1.1 servers are allowed to return responses
		 * which are not acceptable according to the accept headers sent in the request.
		 * In some cases, this may even be preferable to sending a 406 response. User
		 * agents are encouraged to inspect the headers of an incoming response to
		 * determine if it is acceptable.</blockquote>
		 * <p>
		 * 
		 * If the response could be unacceptable, a user agent SHOULD temporarily stop
		 * receipt of more data and query the user for a decision on further actions.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int NOT_ACCEPTABLE = 406;

		/**
		 * Represents the <i>PROXY AUTHENTICATION REQUIRED</i> client error status code.
		 * <p>
		 * This code is similar to {@link #UNAUTHORIZED 401} (Unauthorized), but
		 * indicates that the client must first authenticate itself with the proxy. The
		 * proxy MUST return a Proxy-Authenticate header field containing a challenge
		 * applicable to the proxy for the requested resource. The client MAY repeat the
		 * request with a suitable Proxy-Authorization header field. HTTP access
		 * authentication is explained in "HTTP Authentication: Basic and Digest Access
		 * Authentication".
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int PROXY_AUTHENTICATION_REQUIRED = 407;

		/**
		 * Represents the <i>REQUEST TIMEOUT</i> client error status code.
		 * <p>
		 * This status code indicates that the server timed out waiting for the request.
		 * According to W3 HTTP specifications: "The client did not produce a request
		 * within the time that the server was prepared to wait. The client MAY repeat
		 * the request without modifications at any later time."
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int REQUEST_TIMEOUT = 408;

		/**
		 * Represents the <i>CONFLICT</i> client error status code.
		 * <p>
		 * This status code indicates that the request could not be completed due to a
		 * conflict with the current state of the resource. This code is only allowed in
		 * situations where it is expected that the user might be able to resolve the
		 * conflict and resubmit the request. The response body SHOULD include enough
		 * information for the user to recognize the source of the conflict. Ideally,
		 * the response entity would include enough information for the user or user
		 * agent to fix the problem; however, that might not be possible and is not
		 * required.
		 * <p>
		 * 
		 * Conflicts are most likely to occur in response to a PUT request. For example,
		 * if versioning were being used and the entity being PUT included changes to a
		 * resource which conflict with those made by an earlier (third-party) request,
		 * the server might use the 409 response to indicate that it can't complete the
		 * request. In this case, the response entity would likely contain a list of the
		 * differences between the two versions in a format defined by the response
		 * Content-Type.
		 * <p>
		 * 
		 * <i>&#10022; Whenever a resource conflict would be caused by fulfilling the
		 * request. Duplicate entries and deleting root objects when cascade-delete is
		 * not supported are a couple of examples.</i>
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int CONFLICT = 409;

		/**
		 * Represents the <i>GONE</i> client error status code.
		 * <p>
		 * This status code indicates that the requested resource is no longer available
		 * at the server and no forwarding address is known. This condition is expected
		 * to be considered permanent. Clients with link editing capabilities SHOULD
		 * delete references to the Request-URI after user approval. If the server does
		 * not know, or has no facility to determine, whether or not the condition is
		 * permanent, the status code 404 (Not Found) SHOULD be used instead. This
		 * response is cacheable unless indicated otherwise.
		 * <p>
		 * 
		 * The 410 response is primarily intended to assist the task of web maintenance
		 * by notifying the recipient that the resource is intentionally unavailable and
		 * that the server owners desire that remote links to that resource be removed.
		 * Such an event is common for limited-time, promotional services and for
		 * resources belonging to individuals no longer working at the server's site. It
		 * is not necessary to mark all permanently unavailable resources as "gone" or
		 * to keep the mark for any length of time -- that is left to the discretion of
		 * the server owner.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int GONE = 410;

		/**
		 * Represents the <i>LENGTH REQUIRED</i> client error status code.
		 * <p>
		 * This status code indicates that the server refuses to accept the request
		 * without a defined Content- Length. The client MAY repeat the request if it
		 * adds a valid Content-Length header field containing the length of the
		 * message-body in the request message.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int LENGTH_REQUIRED = 411;

		/**
		 * Represents the <i>PRECONDITION FAILED</i> client error status code.
		 * <p>
		 * This status code indicates that the precondition given in one or more of the
		 * request-header fields evaluated to false when it was tested on the server.
		 * This response code allows the client to place preconditions on the current
		 * resource metainformation (header field data) and thus prevent the requested
		 * method from being applied to a resource other than the one intended.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int PRECONDITION_FAILED = 412;

		/**
		 * Represents the <i>REQUEST ENTITY TOO LARGE</i> client error status code.
		 * <p>
		 * This status code indicates that the server is refusing to process a request
		 * because the request entity is larger than the server is willing or able to
		 * process. The server MAY close the connection to prevent the client from
		 * continuing the request.
		 * <p>
		 * 
		 * If the condition is temporary, the server SHOULD include a Retry- After
		 * header field to indicate that it is temporary and after what time the client
		 * MAY try again.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int REQUEST_ENTITY_TOO_LARGE = 413;

		/**
		 * Represents the <i>REQUEST URI TOO LONG</i> client error status code.
		 * <p>
		 * This status code indicates that the server is refusing to service the request
		 * because the Request-URI is longer than the server is willing to interpret.
		 * This rare condition is only likely to occur when a client has improperly
		 * converted a POST request to a GET request with long query information, when
		 * the client has descended into a URI "black hole" of redirection (e.g., a
		 * redirected URI prefix that points to a suffix of itself), or when the server
		 * is under attack by a client attempting to exploit security holes present in
		 * some servers using fixed-length buffers for reading or manipulating the
		 * Request-URI.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int REQUEST_URI_TOO_LONG = 414;

		/**
		 * Represents the <i>UNSUPPORTED MEDIA TYPE</i> client error status code.
		 * <p>
		 * This status code indicates that the request entity has a media type which the
		 * server or resource does not support. For example, the client uploads an image
		 * as image/svg+xml, but the server requires that images use a different format.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int UNSUPPORTED_MEDIA_TYPE = 415;

		/**
		 * Represents the <i>REQUESTED_RANGE_NOT_STATISFIABLE</i> client error status
		 * code.
		 * <p>
		 * A server SHOULD return a response with this status code if a request included
		 * a Range request-header field, and none of the range-specifier values in this
		 * field overlap the current extent of the selected resource, and the request
		 * did not include an If-Range request-header field. (For byte-ranges, this
		 * means that the first- byte-pos of all of the byte-range-spec values were
		 * greater than the current length of the selected resource.)
		 * <p>
		 * 
		 * When this status code is returned for a byte-range request, the response
		 * SHOULD include a Content-Range entity-header field specifying the current
		 * length of the selected resource. This response MUST NOT use the
		 * multipart/byteranges content- type.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int REQUESTED_RANGE_NOT_STATISFIABLE = 416;

		/**
		 * Represents the <i>EXPECTION FAILED</i> client error status code.
		 * <p>
		 * This status code ind could not be met by this server, or, if the server is a
		 * proxy, the server has unambiguous evidence that the request could not be met
		 * by the next-hop server.
		 * 
		 * @see <a href="https://www.restapitutorial.com/httpstatuscodes.html">HTTP
		 *      status codes</a>
		 */
		public static final int EXPECTION_FAILED = 417;

		/**
		 * Represents the <i>IM A TEAPOT</i> client error status code.
		 * <p>
		 * This status code is meant as an April fools' joke and should not be put in
		 * practical use.
		 */
		public static final int IM_A_TEAPOT = 418;

		/**
		 * Represents the <i>ENHANCE_YOUR_CALM</i> client error status code.
		 * <p>
		 * This status is returned by the Twitter Search and Trends API when the client
		 * is being rate limited. The text is a quote from 'Demolition Man' and the
		 * '420' code is likely a reference to this number's association with marijuana.
		 * Other services may wish to implement the {@link #TOO_MANY_REQUESTS 429} Too
		 * Many Requests response code instead. {@link #UNPROCESSABLE_ENTITY_WEBDAV 422}
		 * Unprocessable Entity (WebDAV)
		 * <p>
		 * 
		 * The {@link #UNPROCESSABLE_ENTITY_WEBDAV 422} (Unprocessable Entity) status
		 * code means the server understands the content type of the request entity
		 * (hence a {@link #UNSUPPORTED_MEDIA_TYPE 415} (Unsupported Media Type) status
		 * code is inappropriate), and the syntax of the request entity is correct (thus
		 * a {@link #BAD_REQUEST 400} (Bad Request) status code is inappropriate) but
		 * was unable to process the contained instructions. For example, this error
		 * condition may occur if an XML request body contains well-formed (i.e.,
		 * syntactically correct), but semantically erroneous, XML instructions.
		 * Wikipedia <i> The request was well-formed but was unable to be followed due
		 * to semantic errors.</i>
		 * 
		 */
		public static final int ENHANCE_YOUR_CALM_TWITTER = 420;

		/**
		 * Represents the <i>UNPROCESSABLE ENTITY</i> client error status code (for
		 * WebDAV).
		 * <p>
		 * This status code means the server understands the content type of the request
		 * entity (hence a 415(Unsupported Media Type) status code is inappropriate),
		 * and the syntax of the request entity is correct (thus a 400 (Bad Request)
		 * status code is inappropriate) but was unable to process the contained
		 * instructions. For example, this error condition may occur if an XML request
		 * body contains well-formed (i.e., syntactically correct), but semantically
		 * erroneous, XML instructions.
		 */
		public static final int UNPROCESSABLE_ENTITY_WEBDAV = 422;

		/**
		 * Represents the <i>LOCKED</i> client error status code (for WebDAV).
		 */
		public static final int LOCKED_WEBDAV = 423;

		/**
		 * Represents the <i>FAILED DEPENDANCY</i> client error status code (for
		 * WebDAV).
		 */
		public static final int FAILED_DEPENDANCY_WEBDAV = 424;

		/**
		 * Represents the <i>RESERVED_FOR_WEBDAV</i> client error status code.
		 */
		public static final int RESERVED_FOR_WEBDAV = 424;

		/**
		 * Represents the <i>UPGRADE REQUIRED</i> client error status code.
		 */
		public static final int UPGRADE_REQUIRED = 426;

		/**
		 * Represents the <i>PRECONDITION REQUIRED</i> client error status code.
		 */
		public static final int PRECONDITION_REQUIRED = 428;

		/**
		 * Represents the <i>TOO MANY REQUESTS</i> client error status code.
		 */
		public static final int TOO_MANY_REQUESTS = 429;

		/**
		 * Represents the <i>REQUEST HEADER FIELDS TOO LARGE</i> client error status
		 * code.
		 */
		public static final int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;

		/**
		 * Represents the <i>NO RESPONSE</i> client error status code.
		 * <p>
		 * This status code is an Nginx HTTP server extension. The server returns no
		 * information to the client and closes the connection (useful as a deterrent
		 * for malware).
		 */
		public static final int NO_RESPONSE_NGINX = 444;

		/**
		 * Represents the <i>RETRY WITH</i> client error status code.
		 * <p>
		 * This status code is a Microsoft extension. The request should be retried
		 * after performing the appropriate action.
		 */
		public static final int RETRY_WITH_MICROSOFT = 449;

		/**
		 * Represents the <i>BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS</i> client error
		 * status code. A Microsoft extension. This status code is given when Windows
		 * Parental Controls are turned on and are blocking access to the given webpage.
		 */
		public static final int BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS_WINDOWS = 450;

		/**
		 * Represents the <i>UNAVAILABLE FOR LEGAL REASONS</i> client error status code.
		 * <p>
		 * This status code is intended to be used when resource access is denied for
		 * legal reasons, e.g. censorship or government-mandated blocked access. A
		 * reference to the 1953 dystopian novel Fahrenheit 451, where books are
		 * outlawed, and the autoignition temperature of paper, 451°F.
		 */
		public static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451;

		/**
		 * Represents the <i>TOO MANY REQUESTS</i> client error status code.
		 * <p>
		 * This status code is an Nginx HTTP server extension. This code is introduced
		 * to log the case when the connection is closed by client while HTTP server is
		 * processing its request, making server unable to send the HTTP header back.
		 */
		public static final int CLIENT_CLOSED_REQUEST = 499;

		/* Server error status codes (5xx) */
		/**
		 * Represents the <i>INTERNAL SERVER ERROR</i> server error status code.
		 */
		public static final int INTERNAL_SERVER_ERROR = 500;

		/**
		 * Represents the <i>NOT IMPLEMENTED</i> server error status code.
		 */
		public static final int NOT_IMPLEMENTED = 501;

		/**
		 * Represents the <i>BAD GATEWAY</i> server error status code.
		 */
		public static final int BAD_GATEWAY = 502;

		/**
		 * Represents the <i>SERVICE UNAVAILABLE</i> server error status code.
		 */
		public static final int SERVICE_UNAVAILABLE = 503;

		/**
		 * Represents the <i>GATEWAY TIMEOUT</i> server error status code.
		 */
		public static final int GATEWAY_TIMEOUT = 504;

		/**
		 * Represents the <i>HTTP VERSION NOT SUPPORTED</i> server error status code.
		 */
		public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

		/**
		 * Represents the <i>VARIANT ALSO NEGOTIATES</i> server error status code.
		 * <p>
		 * The 506 status code indicates that the server has an internal configuration
		 * error: the chosen variant resource is configured to engage in transparent
		 * content negotiation itself, and is therefore not a proper end point in the
		 * negotiation process.
		 * 
		 * @deprecated Experimental
		 */
		@Deprecated
		public static final int VARIANT_ALSO_NEGOTIATES = 506;

		/**
		 * Represents the <i>INSUFFICIENT STORAGE</i> server error status code.
		 */
		public static final int INSUFFICIENT_STORAGE_WEBDAV = 507;

		/**
		 * Represents the <i>LOOP DETECTED</i> server error status code.
		 */
		public static final int LOOP_DETECTED_WEBDAV = 508;

		/**
		 * Represents the <i>BANDWITDTH LIMIT EXCEEDED</i> server error status code.
		 */
		public static final int BANDWITDTH_LIMIT_EXCEEDED_APACHE = 509;

		/**
		 * Represents the <i>NOT EXTENDED</i> server error status code.
		 */
		public static final int NOT_EXTENDED = 510;

		/**
		 * Represents the <i>NETWORK AUTHENTICATION REQUIRED</i> server error status
		 * code.
		 */
		public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;

		/**
		 * Represents the <i>NETWORK READ TIMEOUT ERROR</i> server error status code.
		 */
		public static final int NETWORK_READ_TIMEOUT_ERROR = 598;

		/**
		 * Represents the <i>NETWORK CONNECT TIMEOUT ERROR</i> server error status code.
		 */
		public static final int NETWORK_CONNECT_TIMEOUT_ERROR = 599;

		/**
		 * Retrieves the matching status message for the underlying status code. If
		 * there are no status message matching the status code, {@code null} is
		 * returned.
		 * 
		 * @param status The status to check.
		 * @return The matching status message.
		 */
		public static String getStatusMessage(final int status) {
			Field[] fields = HttpServerResponse.StatusCode.class.getFields();

			for (int i2 = 0; i2 < fields.length; i2++) {
				Field f = fields[i2];

				final boolean oldAccess = f.isAccessible();

				f.setAccessible(true);

				try {
					final Integer statusFromField = f.getInt(null);
					f.setAccessible(oldAccess);

					if (statusFromField == status) {
						if (f.getName().equalsIgnoreCase("BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS_WINDOWS"))
							return "Blocked By Windows Parental Controls";
						if (f.getName().equalsIgnoreCase("REQUEST_URI_TOO_LONG"))
							return "Request-URI Too Long";

						final String message = f.getName().replace("_WINDOWS", "").replace("_NGINX", "")
								.replace("_WEBDAV", "").replace("_MICROSOFT", "").replace("_APACHE", "")
								.replace("_TWITTER", "").replace("_", " ");

						final String trimMessage = message.trim();

						if (trimMessage.equalsIgnoreCase("OK") && status == 200)
							return "OK";

						final String[] split = trimMessage.split(" ");
						String newMessage = "";

						for (int i = 0; i < split.length; i++) {
							final String part = split[i];
							char[] chars = part.toCharArray();
							for (int i1 = 0; i1 < chars.length; i1++) {
								final char partChar = chars[i1];
								if (i1 == 0) {
									newMessage += Character.toUpperCase(partChar);
									continue;
								}
								newMessage += Character.toLowerCase(partChar);
							}
							newMessage += " ";
						}

						return newMessage.trim();

					}

				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				} finally {
					f.setAccessible(oldAccess);
				}

			}

			return null;
		}
	}

}
