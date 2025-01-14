package org.polinux.utils.string;

import java.util.Random;

public class StringUtils {

	private StringUtils() {
	}

	public static final String generateRandomString(final Number length) {
		final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()`-=~_+[]{}/*";

		return generateRandomString(length, chars);

	}

	public static final String generateRandomString(final Number length, final String allowed) {
		final String chars = allowed;

		String s = "";
		Random r = new Random();

		for (; s.length() < length.doubleValue();) {
			s += chars.toCharArray()[clamp(0, Math.abs(r.nextInt(chars.length())), chars.toCharArray().length - 1).intValue()];
		}

		return s;

	}

	public static final String generateRandomStringRegex(final Number length, final String regex) {

		String allowed = "";

		// i = 32 ; i < 127
		for (int i = 0; i < 256; i++) {
			String charString = Character.toString((char) i);
			if (charString.matches(regex)) {
				allowed += charString;
			}
		}
		return generateRandomString(length, allowed);

	}

	private static final Number clamp(Number min, Number val, Number max) {
		if (val.doubleValue() < min.doubleValue())
			return min;
		if (val.doubleValue() > max.doubleValue())
			return max;
		return val;
	}

}
