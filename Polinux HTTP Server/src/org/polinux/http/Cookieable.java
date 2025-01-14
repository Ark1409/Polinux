package org.polinux.http;

/**
 * Interface representing an object that may contain {@link HttpRequestCookie
 * HttpRequestCookies} or {@link HttpResponseCookie HttpResponseCookies}.
 */
public interface Cookieable {
	/**
	 * Retrieves all the known {@link HttpRequestCookie cookies} for this object. If
	 * there are no {@code cookies} associated with this object, this method will
	 * return an empty array (not null).
	 * 
	 * @return An {@link HttpRequestCookie}{@code []} containing all the object's
	 *         cookies.
	 */
	public HttpRequestCookie[] getCookies();

	/**
	 * Retrieves {@link HttpRequestCookie cookies} with the underlying name from the
	 * {@link #getCookies() known cookies}. This method is {@code case sensitive},
	 * so make sure your cookie name matches {@code perfectly} the underlying name.
	 * 
	 * @param name The name of the cookie to find, {@code case sensitive}.
	 * @return The cookie with the underlying name, {@code null} if no cookie is
	 *         found.
	 */
	public default HttpRequestCookie getCookie(final String name) {
		for (int i = 0; i < getCookies().length; i++) {
			HttpRequestCookie cookie = getCookies()[i];

			if (cookie.getName().equals(name))
				return cookie;
		}
		return null;
	}

	/**
	 * Retrieves the names of all the known {@link HttpRequestCookie cookies} for
	 * this object. If there are no {@code cookies} associated with this object,
	 * this method will return an empty array (not null).
	 * 
	 * @return An {@code String[]} array containing all the known
	 *         {@code cookie names}.
	 */
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
	 * Checks whether the {@link HttpRequestCookie} with the underlying name exist
	 * or not.
	 * 
	 * @param name The name of the {@link HttpRequestCookie}.
	 * @return {@code True} if an {@link HttpRequestCookie} has the matching name,
	 *         {@code false} otherwise.
	 */
	public default boolean containsCookie(final String name) {
		return getCookie(name) != null;
	}

	/**
	 * Checks whether the underlying {@link HttpRequestCookie}'s name exists in the
	 * list of {@link #getCookies() known cookies} in this object and whether the
	 * two cookie values match.
	 * 
	 * @param cookie The cookie, preferably <i>not</i> {@code null}.
	 * @return {@code True} if the {@link HttpRequestCookie} "exists", {@code false}
	 *         otherwise.
	 * @see #containsCookie(String)
	 */
	public default boolean containsCookie(final HttpRequestCookie cookie) {
		return containsCookie(cookie.getName()) && getCookie(cookie.getName()).getValue() == cookie.getValue();
	}

}
