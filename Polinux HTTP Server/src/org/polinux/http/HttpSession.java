package org.polinux.http;

/**
 * Represents a browser session with an {@code HTTP} server.
 */
public interface HttpSession extends Attributable {
	/**
	 * Retrieves the {@link HttpResponseCookie} linked with this
	 * {@code HttpSession}.
	 * 
	 * @return The linked {@link HttpResponseCookie}.
	 */
	public HttpResponseCookie getLinkedCookie();

	/**
	 * Gets whether this session is considered a new session (whether a new cookie
	 * had to be made to create this session).
	 * 
	 * @return The "new" value of the session (True or False).
	 */
	public boolean isNew();
}
