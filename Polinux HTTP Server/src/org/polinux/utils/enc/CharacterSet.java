package org.polinux.utils.enc;

import java.nio.charset.Charset;

/**
 * Character Set Utilities
 */
public final class CharacterSet {
	/**
	 * UTF-8
	 */
	public static final java.nio.charset.Charset UTF_8 = java.nio.charset.StandardCharsets.UTF_8;

	/**
	 * UTF-16
	 */
	public static final java.nio.charset.Charset UTF_16 = java.nio.charset.StandardCharsets.UTF_16;

	/**
	 * ASCII
	 */
	public static final java.nio.charset.Charset ASCII = java.nio.charset.StandardCharsets.US_ASCII;

	/**
	 * ISO_8859_1
	 */
	public static final java.nio.charset.Charset ISO_8859_1 = java.nio.charset.StandardCharsets.ISO_8859_1;

	/**
	 * UTF-16 BE (big-endian byte order)
	 */
	public static final java.nio.charset.Charset UTF_16BE = java.nio.charset.StandardCharsets.UTF_16BE;

	/**
	 * UTF-16 LE (little-endian byte order)
	 */
	public static final java.nio.charset.Charset UTF_16LE = java.nio.charset.StandardCharsets.UTF_16LE;

	/**
	 * Cannot construct CharacterSet
	 */
	private CharacterSet() {
		try {
			throw new IllegalAccessException("Cannot create instance of com.lp.utils.CharacterSet");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the "normal" name of the charset.
	 * 
	 * @param charset The charset.
	 * @return The charset name.
	 */
	public static final String name(java.nio.charset.Charset charset) {
		if (charset.equals(UTF_8))
			return "UTF-8";
		if (charset.equals(UTF_16))
			return "UTF-16";
		if (charset.equals(ASCII))
			return "US-ASCII";
		if (charset.equals(ISO_8859_1))
			return "ISO-8859-1";
		if (charset.equals(UTF_16BE))
			return "UTF-16BE";
		if (charset.equals(UTF_16LE))
			return "UTF-16LE";

		return charset.displayName();
	}

	public static final java.nio.charset.Charset fromName(final String charset) {
		final String realCharset = charset.toUpperCase().trim();

		if (realCharset.equalsIgnoreCase("UTF-8"))
			return UTF_8;
		if (realCharset.equalsIgnoreCase("UTF-16"))
			return UTF_16;
		if (realCharset.equalsIgnoreCase("ASCII") || realCharset.equalsIgnoreCase("US-ASCII"))
			return ASCII;
		if (realCharset.equalsIgnoreCase("ISO-8859-1"))
			return ISO_8859_1;
		if (realCharset.equalsIgnoreCase("UTF-16BE"))
			return UTF_16BE;
		if (realCharset.equalsIgnoreCase("UTF-16LE"))
			return UTF_16LE;

		try {
			return java.nio.charset.Charset.forName(realCharset);
		} catch (RuntimeException e) {
			return null;
		}
	}

	/**
	 * Encodes content with specified charset.
	 * 
	 * @param charset Charset
	 * @param content Content
	 * @return Encoded content
	 */
	public static final java.nio.ByteBuffer encode(java.nio.charset.Charset charset, String content) {
		return charset.encode(content);
	}

	/**
	 * Decodes content with specified charset.
	 * 
	 * @param charset Charset
	 * @param content Content
	 * @return Decoded content
	 */
	public static final java.nio.CharBuffer decode(java.nio.charset.Charset charset, java.nio.ByteBuffer content) {
		return charset.decode(content);
	}

	/**
	 * Retrieves The default charset.
	 * 
	 * @return The system's default character set.
	 */
	public static final java.nio.charset.Charset defaultCharset() {
		return Charset.defaultCharset();
	}
}
