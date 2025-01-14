package org.polinux.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.polinux.exceptions.http.HttpCookieRuntimeException;
import org.polinux.exceptions.http.polinux.PolinuxHttpCookieRuntimeException;
import org.polinux.utils.date.TimeUtils;

/**
 * Represents an {@link HttpRequestCookie HTTP cookie} that will be sent to a
 * client. This class should be used when sending a {@code Cookie} to an
 * {@link HttpServer} or an {@link HttpServerResponse}.
 * 
 * @see HttpRequestCookie
 */
public class HttpResponseCookie extends HttpRequestCookie {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6077473197702776752L;

	/**
	 * Represents whether this cookie should be sent over a {@code secure}
	 * connection or not. Insecure sites (http:) can no longer set cookies with the
	 * "secure" directive.
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 */
	protected boolean secure = false;

	/**
	 * Represents whether the cookie should only be able to be viewed in
	 * {@link HttpHeader Http Headers}, and not scripts (JavaScript, XSS,
	 * XMLHttpRequest, etc.)
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 */
	protected boolean httpOnly = false;

	/**
	 * Represents the maximum age for this cookie when the browser receives it. If
	 * both {@code Max-Age} and {@link #expiry Expires} properties are set,
	 * {@code Max-Age} has precedence.
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 */
	protected Integer maxAge = 0;

	/**
	 * Represents the path of the cookie.
	 */
	protected String path = "/";

	/**
	 * Represents the domain of the cookie.
	 */
	protected String domain;

	/**
	 * Represents the "Same Site Policy" of the cookie. The default value is
	 * {@link SameSitePolicy#LAX}.
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 * @see HttpResponseCookie.SameSitePolicy
	 */
	protected SameSitePolicy sameSite = SameSitePolicy.LAX;

	/**
	 * Represents the expiry date for this cookie when the browser receives it. If
	 * both {@code Max-Age} and {@link #expiry Expires} properties are set,
	 * {@code Max-Age} has precedence. If unspecified, the cookie becomes a session
	 * cookie. A session finishes when the client shuts down, not necessary when the
	 * {@link HttpServerRequest#getSession() session cookie} is changed.
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 */
	protected Date expiry;

	/**
	 * Represents the {@link DateFormat date format} for an {@link #expiry} date.
	 */
	protected static final DateFormat expiryFormat = TimeUtils.getDateHttpHeaderFormat();

	/**
	 * Constructs an {@code HttpResponseCookie}.
	 * 
	 * @param name  The name of the cookie.
	 * @param value The value (content) of the cookie
	 * @throws PolinuxHttpCookieRuntimeException If the name or value of the cookie
	 *                                           is invalid.
	 * @see #HttpResponseCookie(String, String)
	 */
	public HttpResponseCookie(String name, Object value) throws PolinuxHttpCookieRuntimeException {
		this(name, String.valueOf(value));
	}

	/**
	 * Constructs an {@code HttpResponseCookie}.
	 * 
	 * @param name  The name of the cookie.
	 * @param value The value (string content) of the cookie
	 * @throws PolinuxHttpCookieRuntimeException If the name or value of the cookie
	 *                                           is invalid.
	 */
	public HttpResponseCookie(String name, String value) throws PolinuxHttpCookieRuntimeException {
		super(name, value);
	}

	/**
	 * Retrieves whether this cookie should be sent over a {@code secure} connection
	 * or not. Insecure sites (http:) can no longer set cookies with the "secure"
	 * directive.
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 * @return {@code True} if this cookie is considered an {@code Secure} cookie.
	 *         {@code False} otherwise.
	 */
	public boolean isSecure() {
		return secure;
	}

	/**
	 * Sets whether the current cookie should be sent over a {@code secure}
	 * connection or not.
	 * 
	 * @optional The parameter is optional.
	 * 
	 * @param secure {@code True} if the current cookie should be sent over a
	 *               {@code secure} connection, {@code false} otherwise.
	 */
	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	/**
	 * Retrieves whether the cookie should only be able to be viewed in
	 * {@link HttpHeader Http Headers}, and not scripts (JavaScript, XSS,
	 * XMLHttpRequest, etc.)
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 * @return {@code True} if this cookie is considered an {@code HttpOnly} cookie.
	 *         {@code False} otherwise.
	 */
	public boolean isHttpOnly() {
		return httpOnly;
	}

	/**
	 * Sets whether the cookie should only be able to be viewed in {@link HttpHeader
	 * Http Headers}, and not scripts (JavaScript, XSS, XMLHttpRequest, etc.)
	 * 
	 * @optional The parameter is optional.
	 * @param httpOnly {@code True} if the current cookie should only be able to be
	 *                 viewed in {@link HttpHeader Http Headers}, {@code false}
	 *                 otherwise.
	 *
	 */
	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}

	/**
	 * Retrieves the maximum age for this cookie when the browser receives it. If
	 * both {@code Max-Age} and {@link HttpResponse} properties are set,
	 * {@code Max-Age} has precedence.
	 * 
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 * @return The maximum age for this cookie, {@code null} if it is a session
	 *         cookie.
	 */
	public Integer getMaxAge() {
		return maxAge;
	}

	/**
	 * Sets the maximum age for this cookie when the browser receives it. A zero or
	 * negative number will expire the cookie immediately. If both {@code Max-Age}
	 * and {@link HttpResponse} properties are set, {@code Max-Age} has precedence.
	 * 
	 * @optional The parameter is optional.
	 * @param maxAge The new age for the cookie. {@code null} to ignore this
	 *               parameter when sending the cookie.
	 * @see <a href=
	 *      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">Set-Cookie
	 *      - HTTP | MDN</a>
	 *
	 */
	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	/**
	 * Retrieves the path of the cookie. If this value is set to {@code null}, it
	 * will be ignored in the {@link HttpHeader Http Header}.
	 * 
	 * @return The path for this cookie
	 * @see #setPath(String)
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path of the cookie. If this value is set to {@code null}, it will be
	 * ignored in the {@link HttpHeader Http Header}.
	 * <p>
	 * Paths are used to tell a browser when specific cookies should be sent back to
	 * the server. <br>
	 * For example, a cookie with the path "/docs" would be sent back by the browser
	 * by a request with the following links:
	 * <ul>
	 * <li>[http|https]://[domain]/docs
	 * <li>[http|https]://[domain]/docs/web
	 * <li>[http|https]://[domain]/docs/users/amig1
	 * <li>[http|https]://[domain]/docs/Web/HTTP/Headers/Set-Cookie
	 * </ul>
	 * <p>
	 * And would not be sent back by a request with <i>these</i> following links:
	 * <ul>
	 * <li>[http|https]://[domain]/home
	 * <li>[http|https]://[domain]/forums/posts
	 * <li>[http|https]://[domain]/shop
	 * <li>[http|https]://[domain]/users/amig1
	 * </ul>
	 * 
	 * A value of {@code null} will not be added to the {@link HttpHeader Http
	 * Header} and will then be interpreted as the global path ("/").
	 * 
	 * @optional The parameter is optional.
	 * 
	 * @param path The path value for this cookie.
	 * 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Retrieves the hosts to where this cookie will be sent. A few specifications
	 * about this property:
	 * <ul>
	 * <li>If omitted, defaults to the host of the current document URL, not
	 * including subdomains.
	 * <li>Contrary to earlier specifications, leading dots in domain names
	 * (.example.com) are ignored.
	 * <li>If a domain is specified, subdomains are always included.
	 * </ul>
	 * 
	 * @return The hosts to where this cookie will be sent.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the hosts to where this cookie will be sent. A few specifications about
	 * this property:
	 * <ul>
	 * <li>If omitted, defaults to the host of the current document URL, not
	 * including subdomains.
	 * <li>Contrary to earlier specifications, leading dots in domain names
	 * (.example.com) are ignored.
	 * <li>If a domain is specified, subdomains are always included.
	 * </ul>
	 * 
	 * @optional The parameter is optional.
	 * 
	 * @param domain The new host(s) that should receive this cookie.
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Retrieves the {@link SameSitePolicy} for this cookie.
	 * <p>
	 * The {@link SameSitePolicy} is the {@code policy} that is used to determine
	 * whether the current cookie should or should not be sent with cross-origin
	 * requests, providing some protection against cross-site request forgery
	 * attacks.
	 * 
	 * @return The {@link SameSitePolicy} for this cookie.
	 */
	public SameSitePolicy getSameSitePolicy() {
		return sameSite;
	}

	/**
	 * Sets the {@link SameSitePolicy} for this cookie.
	 * <p>
	 * The {@link SameSitePolicy} is the {@code policy} that is used to determine
	 * whether the current cookie should or should not be sent with cross-origin
	 * requests, providing some protection against cross-site request forgery
	 * attacks.
	 * 
	 * @optional The parameter is optional. The default for most browsers is
	 *           {@link HttpResponseCookie.SameSitePolicy#LAX}
	 * 
	 * @param sameSite The new {@link SameSitePolicy} for this cookie.
	 * @see HttpResponseCookie.SameSitePolicy
	 */
	public void setSameSitePolicy(SameSitePolicy sameSite) {
		this.sameSite = sameSite;
	}

	/**
	 * Retrieves the {@code expiry date} for the cookie.
	 * <p>
	 * {@link #setMaxAge(Integer)} has been preferred over this method, for if both
	 * values are present, {@link #setMaxAge(Integer) max age} has precedence.
	 * 
	 * @return The expiry date for the current cookie. Does not return same value as
	 *         {@link #getMaxAge()}.
	 */
	public Date getExpiry() {
		return expiry;
	}

	/**
	 * Sets the {@code expiry date} for the cookie.
	 * <p>
	 * {@link #setMaxAge(Integer)} has been preferred over this method, for if both
	 * values are present, {@link #setMaxAge(Integer) max age} has precedence.
	 * 
	 * @param expiry The new expiry date. {@code null} to ignore this parameter when
	 *               sending the cookie.
	 * @optional The parameter is optional.
	 */
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpHeader toHttpHeader() {
		return new HttpHeader("Set-Cookie", this.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final String superString = super.toString();
		String addString = "";

		if (path != null) {
			addString += "; Path=" + path;
		}

		if (domain != null) {
			addString += "; Domain=" + domain;
		}

		if (maxAge != null) {
			if (maxAge != -2)
				addString += "; Max-Age=" + maxAge;
		}

		if (expiry != null) {
			final String expireString = expiryFormat.format(expiry);
			addString += "; Expires=" + expireString;
		}

		if (sameSite != null) {
			addString += "; SameSite=" + sameSite.toString();
		}

		if (httpOnly) {
			addString += "; HttpOnly";
		}

		if (secure) {
			addString += "; Secure";
		}

		return superString + addString;

	}

	/**
	 * Sets the name of the current cookie. This should never be {@code null}.
	 * 
	 * @param name The new name for the cookie.
	 * @throws HttpCookieRuntimeException If the new cookie name is invalid.
	 */
	public void setName(String name) throws PolinuxHttpCookieRuntimeException {
		if (isValidName(name)) {
			this.name = name;
		} else {
			throw new PolinuxHttpCookieRuntimeException(this, "Cookie name \"" + name + "\" is invalid");
		}
	}

	/**
	 * Sets the value of the current cookie. This could / can be {@code null}.
	 * 
	 * @param value The new value for the cookie.
	 * @throws HttpCookieRuntimeException If the new cookie value is invalid.
	 */
	public void setValue(String value) throws PolinuxHttpCookieRuntimeException {
		if (isValidValue(value)) {
			this.value = value;
		} else {
			throw new PolinuxHttpCookieRuntimeException(this, "Cookie value \"" + value + "\" is invalid");
		}
	}

	/**
	 * Parses the cookie from an {@link HttpHeader}. This method returns only
	 * {@link HttpResponseCookie one cookie} but must return an array of
	 * {@link HttpResponseCookie} for the method overrides the
	 * {@link HttpRequestCookie#parse} method from {@link HttpRequestCookie}
	 * 
	 * @param cookie The cookie data. May be the full {@link HttpHeader}
	 *               ("Set-Cookie: name=value; ...") or this data portion
	 *               ("name=value; ...").
	 * @return The parsed cookie. (One cookie)
	 * @throws PolinuxHttpCookieRuntimeException If the cookie name and value are
	 *                                           invalid or if the full header stars
	 *                                           with "Cookie:" instead of
	 *                                           "Set-Cookie:".
	 */
	public static HttpResponseCookie[] parse(String cookie) throws PolinuxHttpCookieRuntimeException {
		final String cookieChunk;

		if (cookie.toLowerCase().startsWith("set-cookie:")) {
			cookieChunk = cookie.substring("set-cookie:".length(), cookie.length()).trim();
		} else if (cookie.toLowerCase().startsWith("cookie:")) {
			throw new PolinuxHttpCookieRuntimeException(null, "\"Cookie header\" is for request cookies.");
		} else {
			cookieChunk = cookie;
		}

		final int firstEqualIndex = cookieChunk.indexOf('=');

		String valueArgs;
		try {
			valueArgs = cookieChunk.substring(firstEqualIndex + 1, cookieChunk.length());
		} catch (RuntimeException e) {
			valueArgs = "";
		}

		final String[] valueArgsSplit = valueArgs.split(";");

		final String name = cookieChunk.substring(0, firstEqualIndex);

		final String value = valueArgsSplit[0];

		boolean secure = false, httpOnly = false;

		Integer maxAge = 0;

		String path = null, domain = null;

		SameSitePolicy policy = SameSitePolicy.LAX;

		java.util.Date expiry = null;

		for (int i = 1; i < valueArgsSplit.length; i++) {
			final String arg = valueArgsSplit[i].trim();

			if (arg.equalsIgnoreCase("Secure")) {
				secure = true;
			}

			if (arg.equalsIgnoreCase("HttpOnly")) {
				httpOnly = true;
			}

			if (arg.equalsIgnoreCase("Max-Age")) {
				maxAge = Integer.parseInt(arg.split("=")[1]);
			}

			if (arg.equalsIgnoreCase("Path")) {
				path = arg.split("=")[1];
			}

			if (arg.equalsIgnoreCase("Domain")) {
				domain = arg.split("=")[1];
			}

			if (arg.equalsIgnoreCase("SameSite")) {
				policy = SameSitePolicy.valueOf(arg.split("=")[1].toUpperCase());
			}

			if (arg.equalsIgnoreCase("Expires")) {
				try {
					expiry = expiryFormat.parse(arg.split("=")[1]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		}
		HttpResponseCookie c = new HttpResponseCookie(name, value);

		c.setSecure(secure);
		c.setHttpOnly(httpOnly);
		c.setMaxAge(maxAge);
		c.setPath(path);
		c.setDomain(domain);
		c.setSameSitePolicy(policy);
		c.setExpiry(expiry);

		return new HttpResponseCookie[] { c };
	}

	/**
	 * Enum containing constants for the {@code Same Site Policy} of a
	 * {@link HttpResponseCookie}.
	 */
	public static enum SameSitePolicy {
		/**
		 * Restricts cross-site cookie sharing, even between different domains that are
		 * owned by the same publisher.
		 */
		STRICT("Strict"),

		/**
		 * Cookies are only set when the domain in the URL of the browser matches the
		 * domain of the cookie — a first-party cookie. The {@code default} for most
		 * browsers.
		 */
		LAX("Lax"),

		/**
		 * Cookies can be set across any domain, which allows third-party cookies to
		 * track users across sites. If this directive is set, the
		 * {@link HttpResponseCookie} should also be set to
		 * {@link HttpResponseCookie#setSecure(boolean) secure}.
		 */
		NONE("None");

		/**
		 * Name
		 */
		private String name;

		/**
		 * Construct SameSitePolicy
		 * 
		 * @param string Name.
		 */
		private SameSitePolicy(String string) {
			this.name = string;
		}

		/**
		 * Transforms the {@code SameSitePolicy} into a string.
		 * 
		 * @return The name of the {@code SameSitePolicy}.
		 */
		public String toString() {
			return name;
		}

	}
}
