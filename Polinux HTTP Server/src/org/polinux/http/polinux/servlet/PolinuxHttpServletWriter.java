package org.polinux.http.polinux.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;

public class PolinuxHttpServletWriter extends PrintWriter {
	protected int timesWrriten = 0;

	public PolinuxHttpServletWriter() {
		this(new StringWriter());
	}

	public PolinuxHttpServletWriter(StringWriter out) {
		super(out);
	}

	public StringWriter getWriter() {
		return (StringWriter) this.out;
	}

	public void write(String s, int off, int len) {
		super.write(s, off, len);
		timesWrriten++;
	}

	/**
	 * Transforms what has been written into the writer into a string.
	 * 
	 * @return The written string content.
	 */
	@Override
	public String toString() {
		return getWriter().toString();
	}

	public boolean hasWritten() {
		return timesWrriten > 0;
	}

	public void clearContent() {
		getWriter().getBuffer().delete(0, getWriter().getBuffer().length());
	}

}
