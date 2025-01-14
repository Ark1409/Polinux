package org.polinux.configuration.version;

import org.polinux.exceptions.version.VersionFormatException;

/**
 * The {@code final} class representing a plugin's version. This class loads a
 * specified string
 * {@link net.plugin.plugin.version.Version#parseVersion(String)
 * Version.parseVersion(String)} and transforms it into this class ,where you
 * may be able to read data from it. <br>
 * This class also checks for {@code beta} and {@code alpha}, so you may check
 * for beta and alpha version. This class checks for beta and alpha version with
 * the following code:
 * 
 * <pre>
 * if (this.version.toLowerCase().contains("beta") || this.version.toLowerCase().contains("b")) {
 * 	this.beta = true;
 * } else {
 * 	this.beta = false;
 * }
 * if (this.version.toLowerCase().contains("alpha") || this.version.toLowerCase().contains("a")) {
 * 	this.alpha = true;
 * } else {
 * 	this.alpha = false;
 * }
 * </pre>
 *
 */
public final class Version {
	/**
	 * The version's value, as a String.
	 */
	private String version;

	/**
	 * Whether the version is a beta version or not.
	 */
	private boolean beta;

	/**
	 * Whether the version is an alpha version or not.
	 */
	private boolean alpha;

	/**
	 * Message used whenever there is a version format error in this class.
	 */
	private static final String VERSION_FORMAT_ERROR = "Cannot parse null or empty version";

	/**
	 * Generates a {@link net.plugin.plugin.version.Version Version} from the
	 * requested String. Note that this method will not get the exact same instance
	 * as another Version with the same string value. Instead, it will create an
	 * entirely new instance with the String as it's string value, which means you
	 * may be able to have multiple instances of the version class that both have
	 * the same string value.
	 * 
	 * @param s The Version string.
	 * @throws VersionFormatException If the specified version is {@code null} or if
	 *                                the version has no characters (empty).
	 */
	public Version(String version) throws VersionFormatException {
		if (version == null || version.trim().equalsIgnoreCase("")
				|| (version.length() == 1 && (version.charAt(0) <= 32 || version.charAt(0) == 127))) {
			throw new VersionFormatException(VERSION_FORMAT_ERROR);
		}
		this.version = version;
		update();
	}

	/**
	 * Generates a {@link net.plugin.plugin.version.Version Version} from the
	 * requested String. Note that this method will not get the exact same instance
	 * as another Version with the same string value. Instead, it will create an
	 * entirely new instance with the String as it's string value, which means you
	 * may be able to have multiple instances of the version class that both have
	 * the same string value.
	 * 
	 * @deprecated The values {@code beta} and {@code alpha} may not reflect the
	 *             real version's string value and therefore will be discarded after
	 *             the next Version {@link #update() update()}.
	 * @param s     The Version string.
	 * @param beta  Whether the version should be considered as a beta version or
	 *              not.
	 * @param alpha Whether the version should be considered as a alpha version or
	 *              not.
	 * @return The generated {@link net.plugin.plugin.version.Version Version}.
	 * @throws VersionFormatException If the specified version is {@code null} or if
	 *                                the version has no characters (empty).
	 */
	@Deprecated
	public Version(String version, boolean beta, boolean alpha) throws VersionFormatException {
		if (version == null || version.trim().equalsIgnoreCase("")) {
			throw new VersionFormatException(VERSION_FORMAT_ERROR);
		}
		this.version = version;
		this.beta = beta;
		this.alpha = alpha;
	}

	/**
	 * Updates the {@code beta} and {@code alpha} variables for this Version. This
	 * method is called after:
	 * 
	 * <pre>
	 * - A version instance is created ({@link #parseVersion(String) Version.parseVersion(String)})
	 * - The version is changed ({@link #setVersion(String) setVersion(String)})
	 * </pre>
	 */
	protected void update() {
		if (this.version.toLowerCase().contains("beta") || this.version.toLowerCase().contains("b")) {
			this.beta = true;
		} else {
			this.beta = false;
		}
		if (this.version.toLowerCase().contains("alpha") || this.version.toLowerCase().contains("a")) {
			this.alpha = true;
		} else {
			this.alpha = false;
		}
	}

	/**
	 * 
	 * Gets this version's full value, as a string. Note that the version as string
	 * may be a {@link java.lang.Number Number} which as been parsed into a String.
	 * 
	 * @return This version's full value, as a string.
	 */
	public String getValue() {
		return version;
	}

	/**
	 * Gets whether the version is an beta version or not. This variable is
	 * determined if the string value contains the character <i>b</i> or <i>B</i>,
	 * or if it contains the word <i>beta</i>, <i>ignore the case</i>.
	 * 
	 * <pre>
	 * if (this.version.toLowerCase().contains("beta") || this.version.toLowerCase().contains("b")) {
	 * 	this.beta = true;
	 * } else {
	 * 	this.beta = false;
	 * }
	 * </pre>
	 * 
	 * @return Whether the version is an beta version or not.
	 */
	public boolean isBeta() {
		return beta;
	}

	/**
	 * Sets the version to beta or not. <br>
	 * If {@code beta} is true, this version will further be considered as a beta
	 * version. <br>
	 * If {@code beta} is false, this version will not further be considered as a
	 * beta version.
	 * 
	 * @deprecated After this next {@link #update() update()}, this value will be
	 *             discarded.
	 * 
	 * @param beta Whether this version should be a beta version or not (true|false)
	 */
	@Deprecated
	public void setBeta(boolean beta) {
		this.beta = beta;
	}

	/**
	 * Sets the version string to be used for most method in this
	 * {@link net.plugin.plugin.version.Version Version}.
	 * 
	 * @deprecated Is it not recommended to change a plugin's version. If you do
	 *             decide to change it, I would advise you to keep the old version
	 *             string in a variable.
	 * @param version The version as a string
	 * @throws VersionFormatException If the specified version is {@code null} or if
	 *                                the version has no characters (empty).
	 */
	@Deprecated
	public void setVersion(String version) throws VersionFormatException {
		if (version == null || version.trim().equalsIgnoreCase("")
				|| (version.length() == 1 && (version.charAt(0) <= 32 || version.charAt(0) == 127))) {
			throw new VersionFormatException(VERSION_FORMAT_ERROR);
		}
		this.version = version;
		update();
	}

	/**
	 * Gets whether the version is an alpha version or not. This variable is
	 * determined if the string value contains the character <i>a</i> or <i>A</i>,
	 * or if it contains the word <i>alpha</i>, <i>ignore the case</i>.
	 * 
	 * <pre>
	 * if (this.version.toLowerCase().contains("alpha") || this.version.toLowerCase().contains("a")) {
	 * 	this.alpha = true;
	 * } else {
	 * 	this.alpha = false;
	 * }
	 * </pre>
	 * 
	 * 
	 * 
	 * @return Whether the version is an alpha version or not.
	 */
	public boolean isAlpha() {
		return alpha;
	}

	/**
	 * Sets the version to alpha or not. <br>
	 * If {@code alpha} is true, this version will further be considered as a alpha
	 * version. <br>
	 * If {@code alpha} is false, this version will not further be considered as a
	 * alpha version.
	 * 
	 * @deprecated After this next {@link #update() update()}, this value will be
	 *             discarded.
	 * 
	 * @param alpha Whether this version should be a alpha version or not
	 *              (true|false)
	 */
	@Deprecated
	public void setAlpha(boolean alpha) {
		this.alpha = alpha;
	}

	/**
	 * Generates a {@link net.plugin.plugin.version.Version Version} from the
	 * requested String. Note that this method will not get the exact same instance
	 * as another Version with the same string value. Instead, it will create an
	 * entirely new instance with the String as it's string value, which means you
	 * may be able to have multiple instances of the version class that both have
	 * the same string value.
	 * 
	 * @param s The Version string.
	 * @return The generated {@link net.plugin.plugin.version.Version Version}.
	 * @throws VersionFormatException If the specified version is {@code null} or if
	 *                                the version has no characters (empty).
	 * @see #Version(String) Version(String)
	 */
	public static final Version parseVersion(String s) throws VersionFormatException {
		return new Version(s);
	}

	/**
	 * Gets this version's value, as a string. Note that you may also use the method
	 * {@link #getValue() getValue()}.
	 * 
	 * @return This version's value, as a string.
	 * @see #getValue() getValue()
	 */
	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * Returns {@code true} If the specified object has the same Version value
	 * string as this version, returns {@code false} otherwise. This method is
	 * {@code case sensitive} and will only return true if both versions have the
	 * same string value and the same case. Note that if the specified Object is not
	 * an instance of the Version class, this method will get the object as a String
	 * and compare that to the string value of this Version. If it has the same case
	 * and both strings are the same, this method returns {@code true}, otherwise,
	 * {@code false}. If you do not which for it to be case sensitive, you may use
	 * {@link #equalsIgnoreCase(Object) equalsIgnoreCase(Object)}.
	 * 
	 * @param obj The Version to compare.
	 * @return If the specified object has the same Version value string as this
	 *         version.
	 * @see #equalsIgnoreCase(Object) equalsIgnoreCase(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Version)) {
			String objString = obj.toString();
			return this.version.equals(objString);
		}
		Version v = (Version) obj;
		return v.version.equals(this.version);
	}

	/**
	 * Returns {@code true} If the specified object has the same Version value
	 * string as this version, returns {@code false} otherwise. This method is
	 * <i>not</i> {@code case sensitive} and will only return true if both versions
	 * have the same string value, no matter the case. Note that if the specified
	 * Object is not an instance of the Version class, this method will get the
	 * object as a String and compare that to the string value of this Version,
	 * ignore the case. If you which for it to be case sensitive, you may use
	 * {@link #equals(Object) equals(Object)}.
	 * 
	 * @param obj The Version to compare.
	 * @return If the specified object has the same Version value string as this
	 *         version.
	 * @see #equals(Object) equals(Object)
	 */
	public boolean equalsIgnoreCase(Object obj) {
		if (equals(obj))
			return true;
		if (!(obj instanceof Version)) {
			String objString = obj.toString();
			return this.version.equalsIgnoreCase(objString);
		}
		Version v = (Version) obj;
		return v.version.equalsIgnoreCase(this.version);
	}

	/**
	 * If the specified {@link net.plugin.plugin.version.Version Version} has either
	 * the same {@code string value}, the same {@code beta value} or the same
	 * {@code alpha value}.
	 * 
	 * @param v The Version to compare.
	 * @return If the two Version's are similar.
	 */
	public boolean isSimilar(Version v) {
		return equalsIgnoreCase(v) || v.alpha == this.alpha || v.beta == this.beta;
	}

	/**
	 * Checks if this version is a decimal version, and not text or an integer
	 * version. This method removes the {@code alpha} and {@code beta} parameters
	 * attached to the version's string value before making this check, so if you
	 * want to know if it is {@code 100%} a decimal (or double), you may get this
	 * version value to check that.
	 * 
	 * @return If this version is a decimal version.
	 */
	public boolean isDecimalVersion() {
		String newVersion = version.toLowerCase().replace("alpha", "").replace("a", "").replace("b", "").replace("beta",
				"");
		try {
			Double.parseDouble(newVersion);
		} catch (NumberFormatException e) {
			return false;
		}
		return newVersion.toLowerCase().contains(".")
				&& newVersion.toLowerCase().endsWith("." + newVersion.substring(newVersion.length() - 1));
	}

	/**
	 * Checks if this version is a integer version, and not text or an decimal
	 * version that ends with <i>.*</i>This method removes the {@code alpha} and
	 * {@code beta} parameters attached to the version's string value before making
	 * this check, so if you want to know if it is {@code 100%} a whole number (or
	 * integer), you may get this version value to check that.
	 * 
	 * @return If this version is a integer version.
	 */
	public boolean isIntegerVersion() {
		String newVersion = version.toLowerCase().replace("alpha", "").replace("a", "").replace("beta", "").replace("b",
				"");
		try {
			Integer.parseInt(newVersion);
		} catch (NumberFormatException e) {
			return false;
		}
		return !(newVersion.toLowerCase().contains("."))
				&& !(newVersion.toLowerCase().endsWith("." + newVersion.substring(newVersion.length() - 1)));
	}

}
