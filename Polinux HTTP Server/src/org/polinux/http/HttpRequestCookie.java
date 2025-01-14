package org.polinux.http;

import org.polinux.exceptions.http.polinux.PolinuxHttpCookieRuntimeException;

/**
 * Represents an {@code HTTP cookie} that was received from a client request.
 * This class should be used when reading a {@code Cookie} from an
 * {@link HttpServer} or an {@link HttpServerRequest}.
 * 
 * @see HttpResponseCookie
 */
public class HttpRequestCookie implements java.io.Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -933705708439384706L;

	/**
	 * Represents the name of the cookie
	 */
	protected String name;

	/**
	 * Represents the value of the cookie
	 */
	protected String value;

	/**
	 * Represents the {@code regular expression} used to determine if the underlying
	 * name is valid for as a cookie's name or not.
	 * 
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-4.1.1">RFC 6265 -
	 *      HTTP State Management Mechanism | Set-Cookie Syntax</a>
	 */
	public static final String COOKIE_NAME_REGEX = "[a-zA-Z0-9\\~\\!#&\\*\\-\\_\\+'\\.\\s]+";

	/**
	 * Represents the {@code regular expression} used to determine if the underlying
	 * name is valid for as a cookie's name or not.
	 * 
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-4.1.1">RFC 6265 -
	 *      HTTP State Management Mechanism | Set-Cookie Syntax</a>
	 */
	public static final String COOKIE_VALUE_REGEX = "(\")?([^\",;\\s\\|\\\\]+)*(\")?"; // \\s (musn't contain)

	/**
	 * Constructs an {@code PolinuxHttpRequestCookie}.
	 * 
	 * @param name  The name of the cookie.
	 * @param value The value (content) of the cookie
	 * @throws PolinuxHttpCookieRuntimeException If the name or value of the cookie
	 *                                           is invalid.
	 * @see #HttpRequestCookie(String, String)
	 */
	public HttpRequestCookie(String name, Object value) throws PolinuxHttpCookieRuntimeException {
		this(name, String.valueOf(value));
	}

	/**
	 * Constructs an {@code PolinuxHttpRequestCookie}.
	 * 
	 * @param name  The name of the cookie.
	 * @param value The value (string content) of the cookie
	 * @throws PolinuxHttpCookieRuntimeException If the name or value of the cookie
	 *                                           is invalid.
	 */
	public HttpRequestCookie(String name, String value) throws PolinuxHttpCookieRuntimeException {
		this.name = name.trim();
		this.value = value;
		if (!isValidName(this.name)) {
			throw new PolinuxHttpCookieRuntimeException(this, "Cookie name \"" + name + "\" is invalid");
		}

		if (!isValidValue(this.value)) {
			throw new PolinuxHttpCookieRuntimeException(this, "Cookie value \"" + value + "\" is invalid");
		}
	}

	/**
	 * Retrieves the name of the current cookie. This should never be {@code null}.
	 * 
	 * @return The name of the current cookie.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the value of the current cookie. This could / can be {@code null}.
	 * 
	 * @return The value of the current cookie.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Transforms the current cookie into a String that can be used inside an
	 * {@link HttpHeader}
	 * 
	 * @return The current cookie, as a String.
	 */
	@Override
	public String toString() {
		return name + "=" + value;
	}

	/**
	 * Checks whether the content string (assumed to be the cookie name) is valid.
	 * 
	 * @param content The content string to check.
	 * @return {@code True} if the content string matches the proper cookie name
	 *         regular expression. {@code False} otherwise.
	 * @see #COOKIE_NAME_REGEX
	 * @see #COOKIE_VALUE_REGEX
	 */
	protected static boolean isValidName(final String content) {
		return content.matches(COOKIE_NAME_REGEX);
	}

	/**
	 * Checks whether the content string (assumed to be the cookie value) is valid.
	 * 
	 * @param content The content string to check.
	 * @return {@code True} if the content string matches the proper cookie value
	 *         regular expression. {@code False} otherwise.
	 * @see #COOKIE_NAME_REGEX
	 * @see #COOKIE_VALUE_REGEX
	 */
	protected static boolean isValidValue(final String content) {
		return content.matches(COOKIE_VALUE_REGEX);
	}

	/**
	 * Transforms the current cookie into an {@link HttpHeader}.
	 * 
	 * @return The transformed cookie.
	 */
	public HttpHeader toHttpHeader() {
		return new HttpHeader("Cookie", this.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return this.getValue() == null || this.getValue().equals("null");

		if (obj instanceof HttpRequestCookie) {
			HttpRequestCookie c = (HttpRequestCookie) obj;
			return (c.value.equals(this.value) && c.name.equals(this.name)) || c == this;
		}

		return (this.getValue().equals(obj.toString()) && this.getName().equals(obj.toString())) || super.equals(obj);
	}

	/**
	 * Retrieves whether the underlying string is equal to the value of this cookie.
	 * This does not do the same thing as {@link #toHttpHeader()
	 * toHttpHeader()}.{@link HttpHeader#toString()
	 * toString()}.{@link String#equalsIgnoreCase(String) equalsIgnoreCase(String)}
	 * or {@link #toString() toString()}.{@link String#equalsIgnoreCase(String)
	 * equalsIgnoreCase(String)}.
	 * 
	 * @param value The string to check.
	 * 
	 * @return {@code True} if the two values are equal. {@code False} otherwise.
	 */
	public boolean equalsIgnoreCase(String value) {
		return this.getValue().equalsIgnoreCase(String.valueOf(value));
	}

	/**
	 * Parses the cookie(s) from an {@link HttpHeader}.
	 * 
	 * @param cookie The cookie data. May be the full {@link HttpHeader} ("Cookie:
	 *               name=value;name1=value1;...") or its data portion
	 *               ("name=value;name1=value1;...").
	 * @return An array of parsed cookies.
	 * @throws PolinuxHttpCookieRuntimeException If the cookie name(s) or value(s)
	 *                                           are invalid or if the full header
	 *                                           starts with "Set-Cookie:" instead of
	 *                                           "Cookie:".
	 */
	public static HttpRequestCookie[] parse(final String cookie) throws PolinuxHttpCookieRuntimeException {
		final String cookieChunck;

		if (cookie.toLowerCase().startsWith("cookie:")) {
			cookieChunck = cookie.substring("cookie:".length(), cookie.length()).trim();
		} else if (cookie.toLowerCase().startsWith("set-cookie:")) {
			throw new PolinuxHttpCookieRuntimeException(null, "\"Set-Cookie\" header is for response cookies.");
		} else {
			cookieChunck = cookie;
		}

		final String[] split = cookieChunck.split(";");

		HttpRequestCookie[] cookies = new HttpRequestCookie[split.length];

		for (int i = 0; i < split.length; i++) {
			final String cookieItem = split[i];

			final String[] cookieItemSplit = cookieItem.split("=");

			if (cookieItemSplit.length <= 0) {
				throw new PolinuxHttpCookieRuntimeException(null,
						"Cookie name is invalid (empty) from cookie: \"" + cookie + "\"");
			}

			final String name = cookieItemSplit[0].trim();

			String value;
			try {
				value = cookieItemSplit[1];
			} catch (RuntimeException e) {
				value = "";
			}
			HttpRequestCookie c = new HttpRequestCookie(name, value);

			cookies[i] = c;
		}

		return cookies;

	}

}
