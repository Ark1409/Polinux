package org.polinux.utils.io;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamReader {

	public InputStreamReader() {
		// TODO Auto-generated constructor stub
	}

	public static String read(final InputStream in) throws IOException {
		final byte[] b = new byte[in.available()];
		final int len = in.read(b);
		return new String(b, 0, len);
	}

}
