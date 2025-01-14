package org.polinux.utils.io;

import java.io.IOException;
import java.io.OutputStream;

import org.polinux.utils.enc.CharacterSet;

public class StringOutputStream extends OutputStream {
	protected String content = null;
	protected String contentToAdd = "";
	protected java.nio.charset.Charset charset = CharacterSet.UTF_8;

	protected boolean autoFlush = true;
	protected boolean closed = false;

	public StringOutputStream() {
		this(null, true, null);
	}

	public StringOutputStream(String premade) {
		this(premade, true, null);
	}

	public StringOutputStream(boolean autoFlush) {
		this(null, autoFlush, null);
	}

	public StringOutputStream(String premade, java.nio.charset.Charset charset) {
		this(premade, true, charset);
	}

	public StringOutputStream(boolean autoFlush, java.nio.charset.Charset charset) {
		this(null, autoFlush, charset);
	}

	public StringOutputStream(String premade, boolean autoFlush, java.nio.charset.Charset charset) {
		this.content = premade;
		this.autoFlush = autoFlush;
		this.charset = charset == null ? CharacterSet.defaultCharset() : charset;
	}

	@Override
	public void write(int b) throws IOException {
		if (isClosed()) {
			throw new IOException("Stream closed");
		}
		initContentToAdd();
		contentToAdd += new String(new byte[] { (byte) b }, this.getCharset());

		if (this.autoFlush) {
			flush();
		}
	}

	public void write(String string) throws IOException {
		if (isClosed()) {
			throw new IOException("Stream closed");
		}
		write(string.getBytes(this.getCharset()), 0, string.getBytes(this.getCharset()).length);
	}

	public boolean isAutoFlush() {
		return this.autoFlush;
	}

	public String getContent() {
		return this.content;
	}

	public java.nio.charset.Charset getCharset() {
		return this.charset;
	}

	@Override
	public void flush() {
		initContent();
		initContentToAdd();
		this.content += this.contentToAdd;
		this.contentToAdd = "";
	}

	@Override
	public void close() {
		flush();
		this.closed = true;
	}

	@Override
	public String toString() {
		return this.getContent();
	}

	private void initContentToAdd() {
		if (this.contentToAdd == null)
			this.contentToAdd = "";
	}

	private void initContent() {
		if (this.content == null)
			this.content = "";
	}

	public boolean isClosed() {
		return this.closed;
	}

}
