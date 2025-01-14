package org.polinux.http.polinux.session;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.polinux.configuration.server.PolinuxHttpServerConfiguration;
import org.polinux.exceptions.http.polinux.PolinuxHttpCookieRuntimeException;
import org.polinux.http.HttpRequestCookie;
import org.polinux.http.HttpSession;
import org.polinux.http.polinux.server.PolinuxHttpServer;

public final class PolinuxHttpSession implements HttpSession {
	protected Map<String, Object> attributes = new LinkedHashMap<String, Object>();
	protected PolinuxHttpSessionCookie cookie;
	protected boolean isNewSession;

	protected static final Set<PolinuxHttpSession> sessions = new LinkedHashSet<PolinuxHttpSession>();

	/**
	 * Constructs a PolinuxHttpSession
	 * 
	 * @param cookie The {@link PolinuxHttpSessionCookie} to link to this session.
	 */
	private PolinuxHttpSession(PolinuxHttpSessionCookie cookie) {
		this(cookie, new HashMap<String, Object>());
	}

	/**
	 * Constructs a PolinuxHttpSession
	 * 
	 * @param cookie     The {@link PolinuxHttpSessionCookie} to link to this
	 *                   session.
	 * @param attributes The attributes for this session.
	 */
	private PolinuxHttpSession(PolinuxHttpSessionCookie cookie, Map<String, Object> attributes) {
		this.attributes = attributes == null ? new HashMap<String, Object>() : attributes;
		this.cookie = cookie;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PolinuxHttpSessionCookie getLinkedCookie() {
		return this.cookie;
	}

	/**
	 * Creates a {@link PolinuxHttpSession} from the underlying
	 * {@link PolinuxHttpServerConfiguration}. This method will automatically
	 * generate a unique linking {@link PolinuxHttpSessionCookie cookie} for the
	 * session, but will {@code block} until the cookie is created.
	 * 
	 * @param configuration The {@link PolinuxHttpServerConfiguration} for the
	 *                      {@link PolinuxHttpServer}.
	 * @return The created {@link PolinuxHttpSession}.\
	 * @see #createSession(PolinuxHttpServerConfiguration, Map)
	 */
	public static synchronized PolinuxHttpSession createSession(PolinuxHttpServerConfiguration configuration) {
		return createSession(configuration, new HashMap<String, Object>());
	}

	/**
	 * Creates a {@link PolinuxHttpSession} from the underlying
	 * {@link PolinuxHttpServerConfiguration}. This method will automatically
	 * generate a unique linking {@link PolinuxHttpSessionCookie cookie} for the
	 * session, but will {@code block} until the cookie is created.
	 * 
	 * @param configuration The {@link PolinuxHttpServerConfiguration} for the
	 *                      {@link PolinuxHttpServer}.
	 * @param attributes    Map containing the attributes for the session.
	 * @return The created {@link PolinuxHttpSession}.
	 */
	public static synchronized PolinuxHttpSession createSession(PolinuxHttpServerConfiguration configuration,
			Map<String, Object> attributes) {
		return createSession(PolinuxHttpSessionCookie.newCookie(configuration), attributes);
	}

	/**
	 * Creates a {@link PolinuxHttpSession} with the underlying
	 * {@link PolinuxHttpSessionCookie}.
	 * 
	 * @param cookie The cookie to link with the session.
	 * @return The created {@link PolinuxHttpSession}. Will be {@code null} if the
	 *         underlying cookie is set to {@code null}.
	 * @throws PolinuxHttpCookieRuntimeException If a session with the underlying
	 *                                           cookie's value already exists.
	 * @see #createSession(PolinuxHttpSessionCookie, Map)
	 */
	public static synchronized PolinuxHttpSession createSession(PolinuxHttpSessionCookie cookie)
			throws PolinuxHttpCookieRuntimeException {
		return createSession(cookie, new HashMap<String, Object>());
	}

	/**
	 * Creates a {@link PolinuxHttpSession} with the underlying
	 * {@link PolinuxHttpSessionCookie}.
	 * 
	 * @param cookie     The cookie to link with the session.
	 * @param attributes Map containing the attributes for the session.
	 * @return The created {@link PolinuxHttpSession}. Will be {@code null} if the
	 *         underlying cookie is set to {@code null}.
	 * @throws PolinuxHttpCookieRuntimeException If a session with the underlying
	 *                                           cookie's value already exists.
	 */
	public static synchronized PolinuxHttpSession createSession(PolinuxHttpSessionCookie cookie,
			Map<String, Object> attributes) throws PolinuxHttpCookieRuntimeException {
		// Return null if cookie is null
		if (cookie == null)
			return null;

		synchronized (sessions) {
			// Checks if session w/ cookie exists
			for (PolinuxHttpSession session : sessions) {
				
				if (session.getLinkedCookie().equals(cookie)
						|| (session.getLinkedCookie().getValue().equals(cookie.getValue())
								&& session.getLinkedCookie().getName().equals(cookie.getName()))) {
					throw new PolinuxHttpCookieRuntimeException(cookie,
							"Session with id " + cookie.getValue() + " already exists!");
				}
			}
		}

		PolinuxHttpSession p = new PolinuxHttpSession(cookie, attributes);

		synchronized (sessions) {
			sessions.add(p);
		}

		p.isNewSession = true;
		p.attributes = attributes;

		return p;
	}

	/**
	 * Retrieves an already created {@link PolinuxHttpSession}. Once this method is
	 * called, the {@link PolinuxHttpSession} found is no longer considered unique
	 * (not considered {@link #isNew() a new session}).
	 * 
	 * @param cookie The {@link PolinuxHttpSessionCookie cookie} to match with the
	 *               session.
	 * @return The {@link PolinuxHttpSession} containing the underlying cookie,
	 *         {@code null} if none was found;
	 */
	public static synchronized PolinuxHttpSession getSession(HttpRequestCookie cookie) {
		// Return null if cookie is null
		if (cookie == null)
			return null;

		// Disable length checks
		// if (cookie.getValue().length() != PolinuxHttpSessionCookie.LENGTH)
		// return null;

		synchronized (sessions) {
			for (PolinuxHttpSession session : sessions) {
				// Checks if cookie are "the same"
				if (session.getLinkedCookie().equals(cookie)
						|| (session.getLinkedCookie().getValue().equals(cookie.getValue())
								&& session.getLinkedCookie().getName().equals(cookie.getName()))) {
					// Session is no longer considered unique.
					session.isNewSession = false;
					return session;
				}
			}
		}

		// Return null if no session are found
		return null;

		// return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNew() {
		return this.isNewSession;
	}

}
