package org.polinux.http.polinux.session;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.polinux.configuration.server.HttpsServerConfiguration;
import org.polinux.configuration.server.PolinuxHttpServerConfiguration;
import org.polinux.exceptions.http.polinux.PolinuxHttpCookieRuntimeException;
import org.polinux.http.HttpRequestCookie;
import org.polinux.http.HttpResponseCookie;
import org.polinux.http.HttpServer;
import org.polinux.http.HttpServerRequest;
import org.polinux.http.HttpSession;
import org.polinux.http.polinux.servlet.PolinuxHttpServerRequest;
import org.polinux.utils.string.StringUtils;

/**
 * Represents a {@link HttpResponseCookie Session Cookie} inside a
 * {@link PolinuxHttpServerRequest}.
 * 
 * @see HttpServerRequest
 * @see HttpResponseCookie
 * @see HttpSession
 */
public class PolinuxHttpSessionCookie extends HttpResponseCookie {
	private static final long serialVersionUID = -2465210072552071753L;

	/**
	 * Represents the {@code name} of the {@code PolinuxHttpSessionCookie}. Always
	 * the same.
	 */
	public static final String DEFAULT_NAME = "PSessionId";

	/**
	 * Represents the {@link PolinuxHttpServerConfiguration} linked to this
	 * {@code PolinuxHttpSessionCookie}.
	 */
	protected PolinuxHttpServerConfiguration config;

	/**
	 * Represents the length of the value of any given
	 * {@link PolinuxHttpSessionCookie}. May changes with later updates.
	 */
	public static final double LENGTH = 128; // Max 2048 if necessary.

	/**
	 * Represents the allowed characters as the value of a
	 * {@link PolinuxHttpSessionCookie}.
	 */
	static final String ALLOWED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_+@/";

	/**
	 * Represents all the cookies created {@link PolinuxHttpSessionCookie
	 * PolinuxHttpSessionCookies}.
	 */
	protected static final Set<PolinuxHttpSessionCookie> knownCookies = new HashSet<PolinuxHttpSessionCookie>();

//	/**
//	 * Constructs an {@code PolinuxHttpSessionCookie}.
//	 * 
//	 * @param value The value (string content) of the cookie
//	 * @throws PolinuxHttpCookieRuntimeException If value of the cookie is invalid.
//	 */
//	public PolinuxHttpSessionCookie(final String value) throws PolinuxHttpCookieRuntimeException {
//		super(DEFAULT_NAME, value);
//	}

	/**
	 * Constructs a {@code PolinuxHttpSessionCookie}.
	 * 
	 * @param config The {@link PolinuxHttpServerConfiguration} to link to this
	 *               cookie.
	 * @param value  The value (string content) of the cookie.
	 * 
	 * @throws PolinuxHttpCookieRuntimeException If value of the cookie is invalid.
	 */
	protected PolinuxHttpSessionCookie(final PolinuxHttpServerConfiguration config, final String value)
			throws PolinuxHttpCookieRuntimeException {
		this(config, value, true);
	}

	/**
	 * Constructs a {@code PolinuxHttpSessionCookie}.
	 * 
	 * @param config The {@link PolinuxHttpServerConfiguration} to link to this
	 *               cookie.
	 * @param value  The value (string content) of the cookie.
	 * @param add    Whether to add it to the list of known cookies.
	 * 
	 * @throws PolinuxHttpCookieRuntimeException If value of the cookie is invalid.
	 */
	protected PolinuxHttpSessionCookie(final PolinuxHttpServerConfiguration config, final String value, boolean add)
			throws PolinuxHttpCookieRuntimeException {
		super(config == null ? DEFAULT_NAME
				: config.getSessionCookieName() == null ? DEFAULT_NAME : config.getSessionCookieName(), value);
		super.setMaxAge(-2);
		super.setSecure((config instanceof HttpsServerConfiguration) ? config.isSessionCookieSecure() : false);
		super.setHttpOnly(config.isSessionCookieHttpOnly());
		super.setSameSitePolicy(config.getSessionCookieSameSitePolicy());
		super.setDomain(config.getSessionCookieDomain().equalsIgnoreCase("") ? null : config.getSessionCookieDomain());

		if (add)
			knownCookies.add(this);
	}

	/**
	 * Does nothing, for the name of this cookie cannot and should not be changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	@Override
	public final void setName(String name) {
		// No-op
	}

	/**
	 * Does nothing, for the value of this cookie cannot and should not be changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	@Override
	public final void setValue(String value) {
		// No-op
	}

	/**
	 * Does nothing, for the security of this cookie cannot and should not be
	 * changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	public void setSecure(boolean secure) {
		// No-op
	}

	/**
	 * Does nothing, for the http-security of this cookie cannot and should not be
	 * changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	public void setHttpOnly(boolean httpOnly) {
		// No-op
	}

	/**
	 * Does nothing, for the maximum age of this cookie cannot and should not be
	 * changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	public void setMaxAge(Integer maxAge) {
		// No-op
	}

	/**
	 * Does nothing, for the path of this cookie cannot and should not be changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	public void setPath(String path) {
		// No-op
	}

	/**
	 * Does nothing, for the domain of this cookie cannot and should not be changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	public void setDomain(String domain) {
		// No-op
	}

	/**
	 * Does nothing, for the same site policy of this cookie cannot and should not
	 * be changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	public void setSameSitePolicy(SameSitePolicy sameSite) {
		// No-op
	}

	/**
	 * Does nothing, for the expiry date of this cookie cannot and should not be
	 * changed.
	 * 
	 * @see PolinuxHttpServerConfiguration PolinuxHttpServerConfiguration - Edit to
	 *      change value.
	 */
	public void setExpiry(Date expiry) {
		// No-op
	}

	/**
	 * Retrieves whether this cookie should be sent over a {@code secure} connection
	 * or not. Insecure sites (http:) can no longer set cookies with the "secure"
	 * directive. If an {@link HttpsServerConfiguration SSL server configuration} is
	 * not being used, this will always return {@code false}.
	 */
	@Override
	public boolean isSecure() {
		// Checks constructor.
		// Although it is set inside the constructor it is better to always return the
		// values from the configuration
		// return super.isSecure();
		return (this.config instanceof HttpsServerConfiguration) ? this.config.isSessionCookieSecure() : false;
	}

	/**
	 * Retrieves the {@link PolinuxHttpServerConfiguration} linked with this
	 * {@code session cookie}.
	 * 
	 * @return The linked {@link PolinuxHttpServerConfiguration}.
	 */
	public PolinuxHttpServerConfiguration getServerConfiguration() {
		return this.config;
	}

	/**
	 * Parses the {@code session cookie} from the underlying
	 * {@link HttpRequestCookie request cookie} and
	 * {@link PolinuxHttpServerConfiguration server configuration}.
	 * 
	 * @param config The configuration for the {@link HttpServer server}.
	 * @param cookie The {@link HttpRequestCookie} that contains the
	 *               {@code session id}.
	 * @return The {@link HttpRequestCookie request cookie}, as a
	 *         {@link PolinuxHttpSessionCookie}.
	 */
	public static PolinuxHttpSessionCookie parseRequestCookie(final PolinuxHttpServerConfiguration config,
			final HttpRequestCookie cookie) {

		try {
			// Return null if cookie name does not match config cookie name
			if (!cookie.getName().equals(config.getSessionCookieName()))
				return null;
		} catch (RuntimeException e) { // NullPointer / any other runtime
			// Will return null if cookie or config is equal to null (NullPointer)
			return null;
		}

		synchronized (knownCookies) {
			for (PolinuxHttpSessionCookie c : knownCookies) {
				if (c.getValue().equals(cookie.getValue()) && c.getName().equals(cookie.getName())) {
					// Return cookie with same value if it already exists
					return c;
				}
			}
		}

		// Return the created cookies
		return new PolinuxHttpSessionCookie(config, cookie.getValue());
	}

	/**
	 * Generates a new, unique {@link PolinuxHttpSessionCookie}. This method blocks
	 * until a new and valid cookie is found.
	 * 
	 * @param config The {@link PolinuxHttpServerConfiguration} (needed for default
	 *               cookie name).
	 * @return The {@link PolinuxHttpSessionCookie} with a {@code unique} session
	 *         id.
	 */
	public static synchronized PolinuxHttpSessionCookie newCookie(final PolinuxHttpServerConfiguration config) {
		PolinuxHttpSessionCookie p;

		// Begin loop to look for new cookie
		while (true) {
			// Create a new cookie with random value
			p = new PolinuxHttpSessionCookie(config, StringUtils.generateRandomString(LENGTH, ALLOWED), false);
			boolean cookieValExist = false;

			// Loops to check if the cookie with the random value exists
			for (PolinuxHttpSessionCookie c : knownCookies) {
				if (c.getValue().equals(p.getValue())) {
					cookieValExist = true;
					break;
				}
			}

			// Continues loop if a cookie with the above value exists
			if (cookieValExist) {
				continue;
			}

			break;
		}

		// Add cookie to list of known cookies.
		knownCookies.add(p);

		// Return unique cookie
		return p;
	}

}
