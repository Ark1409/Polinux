package org.polinux.http;

import org.polinux.exceptions.http.polinux.PolinuxHttpRuntimeException;

/**
 * Represents an Http Header in an {@link HttpServerRequest Http Request} or an
 * {@link HttpServerResponse Http Response}.
 * 
 * @see HttpRequestCookie
 */
public class HttpHeader implements java.io.Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4671220644772273540L;

	/**
	 * Represents the name of the header
	 */
	protected String header;

	/**
	 * Represents the header's value.
	 */
	protected String value;

	/**
	 * Constructs an HttpHeader.
	 * 
	 * @param header The name of the header.
	 * @param value  The string value of the header.
	 * @see #HttpHeader(String, String)
	 * @throws InvalidHeaderException If the header name is invalid.
	 */
	public HttpHeader(String header, Object value) {
		this(header, String.valueOf(value));
	}

	/**
	 * Constructs an HttpHeader.
	 * 
	 * @param header The name of the header.
	 * @param value  The string value of the header.
	 * @throws InvalidHeaderException If the header name is invalid.
	 */
	public HttpHeader(String header, String value) throws InvalidHeaderException {
		if (header == null || header.equalsIgnoreCase(""))
			throw new InvalidHeaderException("Invalid header \"" + header + "\"");
		this.header = String.valueOf(header);
		this.value = value;
	}

	/**
	 * Retrieves the name of the current header.
	 * 
	 * @return The name of the header. Never {@code null}.
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Sets the name of the current header. If set to {@code null}, the value will
	 * be put to the {@code null} value, as a String (literal "null" string).
	 * 
	 * @param header The new header. Should not be {@code null}.
	 */
	public void setHeader(String header) {
		this.header = header == null ? "null" : header;
	}

	/**
	 * Retrieves the value of the current header.
	 * 
	 * @return The value of the header. Never {@code null}.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the current header to one specified. If set to
	 * {@code null}, the value will be put to the {@code null} value, as a String
	 * (literal "null" string).
	 * 
	 * @param value The new value for the header. Should not be {@code null}.
	 */
	public void setValue(String value) {
		this.value = value == null ? "null" : value;
	}

	/**
	 * Retrieves this header's content, as a {@code String} that can also be used
	 * inside an {@link HttpServerRequest Http Request} or an
	 * {@link HttpServerResponse Http Response}.
	 * 
	 * @return The string value of this {@code Http Header}.
	 */
	@Override
	public String toString() {
		return header + ": " + value;
	}

	/**
	 * Transforms the underlying header, as a {@code String}, into the corresponding
	 * {@link HttpHeader Http Header}.
	 * 
	 * @param header The header, as a String (from {@link HttpServerRequest Http
	 *               Request} or {@link #toString() the header itself as a valid
	 *               Http Header}).
	 * @return The transformed header.
	 * @throws InvalidHeaderException If the input header is considered invalid.
	 */
	public static HttpHeader parse(String header) throws InvalidHeaderException {
		if (!header.contains(":")) {
			throw new InvalidHeaderException("Invalid header");
		}

		final String name = header.split(":")[0].trim();
		String value;

		try {
			value = header.split(":")[1].trim();
		} catch (RuntimeException e) {
			value = "";
		}

		return new HttpHeader(name, value);
	}

	public static class InvalidHeaderException extends PolinuxHttpRuntimeException {
		private static final long serialVersionUID = -4672142292651850325L;

		public InvalidHeaderException() {
			super();
		}

		public InvalidHeaderException(String message) {
			super(message);
		}

		public InvalidHeaderException(Throwable cause) {
			super(cause);
		}

		public InvalidHeaderException(String message, Throwable cause) {
			super(message, cause);
		}

		protected InvalidHeaderException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

	}

}
