package org.polinux.utils.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class containing time utilities
 */
public final class TimeUtils {

	/**
	 * Represents the {@link SimpleDateFormat date format} for an {@link #expiry}
	 * date.
	 */
	private static final DateFormat dateHeaderFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

	/**
	 * Initializes the expiry format.
	 */
	static {
		dateHeaderFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	private TimeUtils() {
		// TODO Auto-generated constructor stub
	}

	public static final DateFormat getDateHttpHeaderFormat() {
		return dateHeaderFormat;
	}

	public static final Date now() {
		return new Date();
	}

}
