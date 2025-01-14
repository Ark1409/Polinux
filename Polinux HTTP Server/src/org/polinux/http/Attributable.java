package org.polinux.http;

import java.util.Map;
import java.util.Objects;

import org.polinux.utils.collections.CollectionUtils;

/**
 * Interface representing an attributable object.
 * 
 * @see HttpSession
 * @see HttpServerRequest
 * @see HttpServerResponse
 */
public interface Attributable {
	public Map<String, Object> getAttributes();

	public default Object getAttribute(final String name) {
		return getAttributes().get(name);
	}

	public default Object setAttribute(final String name, final Object value) {
		return this.getAttributes().put(name, value);
	}
	
	public default String[] getAttributeNames() {
		return CollectionUtils.toArray(getAttributes().keySet(), String.class);
	}

	public default boolean containsAttribute(final String name) {
		return getAttribute(name) != null;
	}

	public default Object removeAttribute(final String name) {
		return this.getAttributes().remove(name);
	}

	public default Object addAttribute(final String name, final Object value) {
		return setAttribute(name, value);
	}

	public default Object removeAttribute(final String name, final Object value) {
		if (Objects.equals(this.getAttribute(name), value))
			return this.removeAttribute(name);
		return null;
	}

	public default Object deleteAttribute(final String name) {
		return removeAttribute(name);
	}

	public default Object deleteAttribute(final String name, final Object value) {
		return removeAttribute(name, value);
	}

}
