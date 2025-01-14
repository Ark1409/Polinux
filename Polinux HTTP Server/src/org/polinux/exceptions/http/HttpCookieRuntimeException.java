package org.polinux.exceptions.http;

import org.polinux.http.HttpRequestCookie;
import org.polinux.http.HttpResponseCookie;

/**
 * Represents an exception with an {@link HttpRequestCookie} or an
 * {@link HttpResponseCookie}.
 * 
 * @see HttpRequestCookie
 * @see HttpResponseCookie
 */
public class HttpCookieRuntimeException extends HttpRuntimeException {
	/**
	 * The {@link HttpRequestCookie} or {@link HttpResponseCookie} linked to this
	 * exception. Could be {@code null}.
	 */
	protected HttpRequestCookie cookie;
	private static final long serialVersionUID = 8624733739007082157L;

	public HttpCookieRuntimeException() {
		super();
	}

	public HttpCookieRuntimeException(String message) {
		super(message);
	}

	public HttpCookieRuntimeException(Throwable cause) {
		super(cause);
	}

	public HttpCookieRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	protected HttpCookieRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Retrieves The {@link HttpRequestCookie} or {@link HttpResponseCookie} linked
	 * to this exception.
	 * 
	 * @return The linked cookie. Could be {@code null}.
	 */
	public HttpRequestCookie getCookie() {
		return this.cookie;
	}

}
