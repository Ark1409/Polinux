package org.polinux.http;

/**
 * Represents a form {@link org.polinux.utils.enc.URLEncoder url encoded}
 * parameter inside an {@link HttpServerRequest}. Usually also considered a
 * {@code query parameter}.
 * 
 * @see HttpServerRequest
 */
public class HttpFormUrlEncodedParameter extends HttpParameter {

	/**
	 * Constructs an {@code HttpFormUrlEncodedParameter}. The underlying
	 * {@code name} and {@code value} must be decoded with a
	 * {@link org.polinux.utils.enc.URLDecoder URLDecoder}.
	 * 
	 * @param name  The name of the parameter,
	 *              {@link org.polinux.utils.enc.URLDecoder decoded}.
	 * @param value The value for the parameter,
	 *              {@link org.polinux.utils.enc.URLDecoder decoded}.
	 */
	public HttpFormUrlEncodedParameter(String name, String value) {
		super(name, value);
	}
}
