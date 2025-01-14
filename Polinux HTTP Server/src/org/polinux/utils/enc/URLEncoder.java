package org.polinux.utils.enc;

import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * A {@link URL} encoding utility.
 */
public final class URLEncoder implements Encoder {
	/* Encoding charset */
	private java.nio.charset.Charset charset = CharacterSet.UTF_8;

	/**
	 * Constructs a {@code URLEncoder}, using {@link CharacterSet#UTF_8 UTF-8} as
	 * its {@link java.nio.charset.Charset Charset}.
	 */
	public URLEncoder() {
	}

	/**
	 * Constructs a {@code URLEncoder}.
	 * 
	 * @param charset The {@link java.nio.charset.Charset Charset} for the encoder.
	 */
	public URLEncoder(java.nio.charset.Charset charset) {
		this.charset = charset;
	}

	/**
	 * Encodes the underlying string with the underlying
	 * {@link java.nio.charset.Charset}.
	 * 
	 * @param text    The string to encode.
	 * @param charset The {@link java.nio.charset.Charset Charset} to use when
	 *                encoding. Use {@link CharacterSet#UTF_8 UTF-8} for normal
	 *                {@link URL} encoding.
	 * @return The encoded string.
	 */
	public static String encode(String text, java.nio.charset.Charset charset) {
		return new URLEncoder(charset).encode(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode(String text) {
		try {
			return java.net.URLEncoder.encode(text, this.charset.displayName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode(char character) {
		return encode(Character.toString(character));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode(CharSequence sequence) {
		return encode(sequence.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.nio.charset.Charset getCharset() {
		return this.charset;
	}

}
