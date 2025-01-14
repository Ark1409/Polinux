package org.polinux.http.polinux.servlet;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.polinux.utils.io.StringOutputStream;

/**
 * @deprecated Use {@link PolinuxHttpServletWriter}.
 */
@Deprecated
public class PolinuxHttpServletPrintStream extends PrintStream {

	public PolinuxHttpServletPrintStream(StringOutputStream out) {
		super(out);
	}

	public PolinuxHttpServletPrintStream(StringOutputStream out, boolean autoFlush) {
		super(out, autoFlush);
	}

	public PolinuxHttpServletPrintStream(StringOutputStream out, boolean autoFlush, String encoding)
			throws UnsupportedEncodingException {
		super(out, autoFlush, encoding);
	}
	
	public StringOutputStream getOutputStream() {
		return (StringOutputStream) this.out;
	}
	
	public String toString() {
		return getOutputStream().toString();
	}

}
