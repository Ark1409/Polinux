package org.polinux.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Calendar;

import org.polinux.utils.enc.CharacterSet;

/**
 * Represents a {@code Logger} (tool used to log items to the console).
 */
public class Logger {

	protected boolean addTime = true, addName = true, autoFlush = true;
	protected String name, prefix, suffix;

	protected OutputStream out;

	protected static final String NEW_LINE = System.lineSeparator();

	protected Charset charset;

	public static final Charset DEFAULT_CHARSET = CharacterSet.defaultCharset();

	public String lineSeperator = System.lineSeparator();

	public static final int LVL_INFO = 0;
	public static final int LVL_WARN = 1;
	public static final int LVL_ERR = 2;

	public Logger(OutputStream out) {
		this("", out);
	}

	public Logger(final String name, OutputStream out) {
		this(name, out, DEFAULT_CHARSET);
	}

	public Logger(final String name, OutputStream out, Charset charset) {
		this.name = name;
		this.out = out;
		this.charset = charset;
	}

	public void log(int level, Object message) {
		log(level, String.valueOf(message));
	}

	public void log(int level, final String message) {
		String finalString = "";

		int before = 0;

		if (prefix != null) {
			finalString = prefix + finalString;
			before++;
		}

		if (addTime) {
			Calendar c = Calendar.getInstance();

			final String full = "["
					+ (c.get(Calendar.HOUR_OF_DAY) >= 10 ? c.get(Calendar.HOUR_OF_DAY)
							: "0" + c.get(Calendar.HOUR_OF_DAY))
					+ ":" + (c.get(Calendar.MINUTE) >= 10 ? c.get(Calendar.MINUTE) : "0" + c.get(Calendar.MINUTE)) + ":"
					+ (c.get(Calendar.SECOND) >= 10 ? c.get(Calendar.SECOND) : "0" + c.get(Calendar.SECOND)) + "]";
			if (before >= 1)
				finalString += " " + full;
			else
				finalString += full;
			before++;
		}

		if (addName) {
			final String full = "[" + this.getName() + "]";
			if (before >= 1)
				finalString += " " + full;
			else
				finalString += full;
			before++;
		}

		if (level == 0) {
			if (before >= 1)
				finalString += " [INFO]";
			else
				finalString += "[INFO]";
			before++;
		}

		if (level == 1) {
			if (before >= 1)
				finalString += " [WARN]";
			else
				finalString += "[WARN]";
			before++;
		}

		if (level == 2) {
			if (before >= 1)
				finalString += " [ERROR]";
			else
				finalString += "[ERROR]";
			before++;
		}

		final String allBefore = finalString + " ";

		finalString += " " + String.valueOf(message);

		if (suffix != null) {
			finalString = finalString + " " + suffix;
		}

		final byte[] bytes = finalString.getBytes(charset);

		final byte[] messageBytes = message.getBytes(charset);

		try {
			final String[] subString = message.split(lineSeperator);

			final byte[] lineBytes = lineSeperator.getBytes(charset);

			if (subString.length >= 1) {
				for (int i = 0; i < subString.length; i++) {
					final byte[] subBytes = new String(allBefore + subString[i]).getBytes(charset);
					out.write(subBytes, 0, subBytes.length);
					out.write(lineBytes, 0, lineBytes.length);
				}
			} else {
				out.write(bytes, 0, bytes.length);
			}

			out.write(lineBytes, 0, lineBytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (autoFlush)
			flush();
	}

	public void log(String message) {
		log(LVL_INFO, message);
	}

	public void log(Object message) {
		log(LVL_INFO, String.valueOf(message));
	}

	public void log(String level, Object message) {
		log(level, String.valueOf(message));
	}

	public void log(String level, String message) {
		String finalString = "";

		if (prefix != null) {
			finalString = prefix + finalString;
		}

		if (addTime) {
			Calendar c = Calendar.getInstance();

			final String full = "[" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":"
					+ c.get(Calendar.SECOND) + "]";
			finalString += " " + full;
		}

		if (addName) {
			final String full = "[" + this.getName() + "]";
			finalString += " " + full;
		}

		if (level != null) {
			finalString += " [" + String.valueOf(level) + "]";
		}
		finalString += " " + String.valueOf(message);

		if (suffix != null) {
			finalString = finalString + " " + suffix;
		}

		final byte[] bytes = finalString.getBytes(charset);

		try {
			out.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (autoFlush)
			flush();
	}

	public void logError(String message) {
		log(LVL_ERR, message);
	}

	public void logWarn(String message) {
		log(LVL_WARN, message);
	}

	public void flush() {
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showTime() {
		return addTime;
	}

	public void showTime(boolean addTime) {
		this.addTime = addTime;
	}

	public boolean showName() {
		return addName;
	}

	public void showName(boolean addName) {
		this.addName = addName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean isAutoFlush() {
		return autoFlush;
	}

	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}

	public OutputStream getOutputStream() {
		return out;
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

}
