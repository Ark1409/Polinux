package org.polinux.http;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface representing an object that may have {@link HttpParameter
 * parameters}.
 * 
 * @see HttpServerRequest
 */
public interface Parametable {
	/**
	 * Retrieves all the known {@link HttpParameter parameters} for this object.
	 * 
	 * @return This object's parameters.
	 */
	public Collection<? extends HttpParameter> getParameters();

	/**
	 * Retrieves the {@link HttpParameter parameter} with the underlying name.
	 * 
	 * @param name The name of the parameter, {@code case sensitive}.
	 * @return The parameter matching the underlying name, {@code null} if a
	 *         parameter with the matching name is not found.
	 */
	public default HttpParameter getParameter(final String name) {
		if (this.getParameters() == null)
			return null;
		Iterator<? extends HttpParameter> iter = getParameters().iterator();
		if (iter != null) {
			synchronized (iter) {
				while (iter.hasNext()) {
					HttpParameter param = iter.next();

					if (param.getName().equals(name))
						return param;
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether the {@link HttpParameter parameter} with the underlying name
	 * exist, or not.
	 * 
	 * @param name The name of the parameter, {@code case sensitive}.
	 * @return {@code True} if a parameter has the matching name, {@code false}
	 *         otherwise.
	 */
	public default boolean containsParameter(final String name) {
		return getParameter(name) != null;
	}

	/**
	 * Checks whether the underlying {@link HttpParameter parameter}'s name exists
	 * in the list of {@link #getParameters() known parameters} in this object and
	 * whether the two parameter values match.
	 * 
	 * @param param The parameter, preferably <i>not</i> {@code null}.
	 * @return {@code True} if the parameter exists, {@code false} otherwise.
	 * @see #containsParameter(String)
	 */
	public default boolean containsParameter(final HttpParameter param) {
		return containsParameter(param.getName()) && getParameter(param.getName()).getValue() == param.getValue();
	}

	/**
	 * Retrieves the name of all the {@link #getParameters() known parameters} in
	 * this object.
	 * 
	 * @return The names of all parameters.
	 */
	public default String[] getParameterNames() {
		if (getParameters() == null)
			return new String[] {};

		String[] names = new String[getParameters().size()];

		Iterator<? extends HttpParameter> iter = getParameters().iterator();
		if (iter != null) {
			synchronized (iter) {
				int i = 0;
				while (iter.hasNext()) {
					names[i++] = iter.next().getName();
				}
			}
		} else {
			return new String[] {};
		}

		return names;
	}
}
