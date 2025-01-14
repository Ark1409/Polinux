package org.polinux.exceptions.http.polinux;

import org.polinux.exceptions.http.HttpCookieRuntimeException;
import org.polinux.http.HttpRequestCookie;
import org.polinux.http.HttpResponseCookie;

/**
 * Represents an exception with an {@link HttpRequestCookie} or an
 * {@link HttpResponseCookie}.
 * 
 * @see HttpRequestCookie
 * @see HttpResponseCookie
 */
public class PolinuxHttpCookieRuntimeException extends HttpCookieRuntimeException {

	private static final long serialVersionUID = -6179089474813991282L;

	public PolinuxHttpCookieRuntimeException(HttpRequestCookie cookie) {
		super();
		this.cookie = cookie;
	}

	public PolinuxHttpCookieRuntimeException(HttpRequestCookie cookie, String message) {
		super(message);
		this.cookie = cookie;
	}

	public PolinuxHttpCookieRuntimeException(HttpRequestCookie cookie, Throwable cause) {
		super(cause);
		this.cookie = cookie;
	}

	public PolinuxHttpCookieRuntimeException(HttpRequestCookie cookie, String message, Throwable cause) {
		super(message, cause);
		this.cookie = cookie;
	}

	protected PolinuxHttpCookieRuntimeException(HttpRequestCookie cookie, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.cookie = cookie;
	}

	/**
	 * Retrieves The {@link HttpRequestCookie} or {@link HttpResponseCookie} linked
	 * to this exception.
	 * 
	 * @return The linked cookie. Could be {@code null}.
	 */
	public HttpRequestCookie getCookie() {
		return (HttpRequestCookie) super.getCookie();
	}

}
