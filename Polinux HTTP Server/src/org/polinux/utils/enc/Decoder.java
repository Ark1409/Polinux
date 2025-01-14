package org.polinux.utils.enc;

/**
 * Represents a decoder.
 */
public interface Decoder {
	/**
	 * Decodes the underlying string.
	 * 
	 * @param text The string to decode.
	 * @return The decoded string.
	 */
	public String decode(String text);

	/**
	 * Decodes the underlying character.
	 * 
	 * @param letter The character to decode.
	 * @return The decoded character.
	 */
	public String decode(char character);

	/**
	 * Decodes the underlying {@link CharSequence}.
	 * 
	 * @param sequence The {@code CharSequence} to decode.
	 * @return The decoded {@link CharSequence}.
	 */
	public String decode(CharSequence sequence);

	/**
	 * Retrieves the {@link java.nio.charset.Charset} for the decoder.
	 * 
	 * @return The decoder's charset.
	 */
	public java.nio.charset.Charset getCharset();
}
