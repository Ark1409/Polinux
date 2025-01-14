package org.polinux.utils.enc;

/**
 * Represents an encoder.
 */
public interface Encoder {
	/**
	 * Encodes the underlying string.
	 * 
	 * @param text The string to encode.
	 * @return The encoded string.
	 */
	public String encode(String text);

	/**
	 * Encodes the underlying character.
	 * 
	 * @param character The character to encode.
	 * @return The encoded character.
	 */
	public String encode(char character);

	/**
	 * Encodes the underlying {@link CharSequence}.
	 * 
	 * @param sequence The {@code CharSequence} to encode.
	 * @return The encoded {@link CharSequence}.
	 */
	public String encode(CharSequence sequence);

	/**
	 * Retrieves the {@link java.nio.charset.Charset} for the encoder.
	 * 
	 * @return The encoder's charset.
	 */
	public java.nio.charset.Charset getCharset();
}
