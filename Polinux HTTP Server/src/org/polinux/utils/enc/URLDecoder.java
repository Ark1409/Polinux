package org.polinux.utils.enc;

import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * A {@link URL} decoding utility.
 */
public final class URLDecoder implements Decoder {
	/* Decoding charset */
	private java.nio.charset.Charset charset = CharacterSet.UTF_8;

	/**
	 * Constructs a {@code URLDecoder}, using {@link CharacterSet#UTF_8 UTF-8} as
	 * its {@link java.nio.charset.Charset Charset}.
	 */
	public URLDecoder() {
	}

	/**
	 * Constructs a {@code URLDecoder}.
	 * 
	 * @param charset The {@link java.nio.charset.Charset Charset} for the decoder.
	 */
	public URLDecoder(java.nio.charset.Charset charset) {
		this.charset = charset;
	}

	/**
	 * Decodes the underlying string with the underlying
	 * {@link java.nio.charset.Charset}.
	 * 
	 * @param text    The string to decode.
	 * @param charset The {@link java.nio.charset.Charset Charset} to use when
	 *                encoding. Use {@link CharacterSet#UTF_8 UTF-8} for normal
	 *                {@link URL} encoding.
	 * @return The decoded string.
	 */
	public static String decode(String text, java.nio.charset.Charset charset) {
		return new URLDecoder(charset).decode(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String decode(String text) {
		try {
			return java.net.URLDecoder.decode(text, this.charset.displayName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String decode(char character) {
		return decode(Character.toString(character));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String decode(CharSequence sequence) {
		return decode(sequence.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.nio.charset.Charset getCharset() {
		return this.charset;
	}

}
