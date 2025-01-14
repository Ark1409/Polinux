package org.polinux.http;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface representing an object that may contain {@link HttpHeader
 * HttpHeaders}.
 */
public interface Headerable {
	/**
	 * Retrieves a collection of all the {@link HttpHeader HttpHeaders} contained
	 * within this object.
	 * 
	 * @return All the object's {@link HttpHeader HttpHeaders}.
	 */
	public Collection<? extends HttpHeader> getHeaders();

	/**
	 * Retrieves all the known {@link HttpHeader} names as a {@code String[]}.
	 * 
	 * @return An array containing all the known {@link HttpHeader} names.
	 */
	public default String[] getHeaderNames() {
		final String[] a = new String[getHeaders().size()];
		final Iterator<? extends HttpHeader> i = getHeaders().iterator();

		int index = 0;

		while (i.hasNext()) {
			a[index++] = i.next().getHeader();
		}
		return a;
	}

	/**
	 * Retrieves the first occurrence of the {@link HttpHeader} containing the
	 * underlying name.
	 * 
	 * @param name The name of the {@link HttpHeader}. Should not be {@code null}.
	 * @return The {@link HttpHeader} found with the underlying name, {@code null}
	 *         otherwise.
	 */
	public default HttpHeader getHeader(final String name) {
		final Iterator<? extends HttpHeader> i = getHeaders().iterator();

		while (i.hasNext()) {
			HttpHeader h = i.next();
			if (h.getHeader().equalsIgnoreCase(name))
				return h;

		}
		return null;
	}

	/**
	 * Checks whether the {@link HttpHeader} with the underlying name exist or not.
	 * 
	 * @param name The name of the {@link HttpHeader}.
	 * @return {@code True} if an {@link HttpHeader} has the matching name,
	 *         {@code false} otherwise.
	 */
	public default boolean containsHeader(final String name) {
		return getHeader(name) != null;
	}

	/**
	 * Checks whether the underlying {@link HttpHeader}'s name exists in the list of
	 * {@link #getHeaders() known headers} in this object and if the header values
	 * both match.
	 * 
	 * @param header The header, preferably <i>not</i> {@code null}.
	 * @return {@code True} if the {@link HttpHeader} exists, {@code false}
	 *         otherwise.
	 * @see #containsHeader(String)
	 */
	public default boolean containsHeader(final HttpHeader header) {
		return containsHeader(header.getHeader()) && getHeader(header.getHeader()).getValue() == header.getValue();
	}
}
