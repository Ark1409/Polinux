package org.polinux.http;

import org.polinux.utils.enc.CharacterSet;
import org.polinux.utils.enc.URLEncoder;

/**
 * Represents a parameter related to either {@link HttpServerRequest
 * HttpServerRequests} or {@link HttpServerResponse HttpServerResponses}.
 * 
 * @see HttpMultipartFormParameter
 * @see HttpFormUrlEncodedParameter
 */
public abstract class HttpParameter {
	/**
	 * Represents the name of the parameter.
	 */
	protected String name;

	/**
	 * Represents the parameter's value.
	 */
	protected String value;

	/**
	 * Constructs an {@code HttpParameter}.
	 * 
	 * @param name  The name of the parameter, preferably <i>not</i> {@code null}.
	 * @param value The value of the parameter, preferably <i>not</i> {@code null}.
	 */
	public HttpParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Retrieves the name of the parameter.
	 * 
	 * @return This parameter's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the value of the parameter.
	 * 
	 * @return This parameter's value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Transforms the current parameter into a string,
	 * {@link URLEncoder#encode(String, java.nio.charset.Charset) url encoded}.
	 * 
	 * @return The query parameter, as a string.
	 */
	@Override
	public String toString() {
		return URLEncoder.encode(name, CharacterSet.UTF_8) + "=" + URLEncoder.encode(value, CharacterSet.UTF_8);
	}

}
