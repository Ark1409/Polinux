package org.polinux.configuration;

import java.util.Map;
import java.util.Set;

/**
 * A {@code ConfigurationSection} is a section filled with keys and values of a
 * certain configuration file or normal file. This interface represents a
 * {@code ConfigurationSection} from the file.
 * <p>
 * This interface ({@link org.polinux.configuration.ConfigurationSection
 * ConfigurationSection}) is to be used when you were looking for a
 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
 * in a certain file.
 * </p>
 * <p>
 * You may only be able to access this {@code interface} if you access it from a
 * {@code class} that is a child to the {@code class}
 * {@link org.polinux.configuration.Configuration Configuration}.
 * </p>
 * <p>
 * This {@code interface} has the base methods for most Configuration files.
 * </p>
 * 
 * @see org.polinux.configuration.Configuration
 * @see org.polinux.configuration.yaml.YamlConfiguration
 * @see org.polinux.configuration.yaml.YamlConfigurationSection
 * 
 * @see org.polinux.configuration.ini.INIConfiguration
 * @see org.polinux.configuration.ini.INIConfigurationSection
 * 
 * @see org.polinux.configuration.properties.PropertiesConfiguration
 * 
 */
public interface ConfigurationSection {

	/**
	 * Get's the Object from the specified {@code node}.
	 * 
	 * @param node The node to the value (key).
	 * @return Returns
	 *         {@link org.polinux.configuration.ConfigurationSection#getObject(String node, Object def)
	 *         ConfigurationSection.getObject(String node, Object def)}, with the
	 *         default value being {@code null}.
	 * @see org.polinux.configuration.ConfigurationSection#getObject(String, Object)
	 *      ConfigurationSection.getObject(String, Object)
	 * @see #getObject(String, Object)
	 */
	public default Object getObject(String node) {
		return getObject(node, null);
	}

	/**
	 * Get's the Object from the specified {@code node}. If the key cannot be found
	 * in the {@link org.polinux.configuration.ConfigurationSection
	 * ConfigurationSection} or for some reason something errors, it will return the
	 * {@code def} (default value) inputed.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Object Object}.
	 * 
	 */
	public Object getObject(String node, Object def);

	public default Number getNumber(String node) {
		return getNumber(node, 0);
	}

	public default Number getNumber(String node, Number def) {
		if (!containsNumber(node))
			return def;
		return (Number) getObject(node, def);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as an Integer.
	 * 
	 * @param node The node to the value (key).
	 * @return Returns
	 *         {@link org.polinux.configuration.ConfigurationSection#getInt(String node, int def)
	 *         ConfigurationSection.getInt(String node, int def)}, with the default
	 *         value being {@code 0}.
	 * @see #getInt(String, int)
	 */
	public default int getInt(String node) {
		return getInt(node, 0);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as an Integer.
	 * If the key cannot be found in the
	 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
	 * or for some reason something errors, it will return the {@code def} (default
	 * value) inputed. <br>
	 * <br>
	 * Also, if the underlying value is, for example, a double instead of an
	 * integer, this method will convert the value into an integer before returning
	 * it.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default int value in case the {@code node} specified isn't
	 *             there or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Integer int}.
	 */
	public default int getInt(String node, int def) {
		if (!containsNumber(node))
			return def;
		return getNumber(node, def).intValue();
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as a Long.
	 * 
	 * @param node The node to the value (key).
	 * @return Returns
	 *         {@link org.polinux.configuration.ConfigurationSection#getLong(String, long)
	 *         ConfigurationSection.getLong(String node, long def)}, with the
	 *         default value being {@code 0L}.
	 * @see #getLong(String, long)
	 */
	public default long getLong(String node) {
		return getLong(node, 0L);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as a Long. If
	 * the key cannot be found in the
	 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
	 * or for some reason something errors, it will return the {@code def} (default
	 * value) inputed. <br>
	 * <br>
	 * Also, if the underlying value is, for example, a double instead of a long,
	 * this method will convert the value into a long before returning it.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default long value in case the {@code node} specified isn't
	 *             there or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Long long}.
	 */
	public default long getLong(String node, long def) {
		if (!containsNumber(node))
			return def;
		return getNumber(node, def).longValue();
	}

	public default double getDouble(String node) {
		return getDouble(node, 0D);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as a Double. If
	 * the key cannot be found in the
	 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
	 * or for some reason something errors, it will return the {@code def} (default
	 * value) inputed. <br>
	 * <br>
	 * Also, if the underlying value is, for example, a float instead of a double,
	 * this method will convert the value into a double before returning it.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default double value in case the {@code node} specified isn't
	 *             there or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Double double}.
	 */
	public default double getDouble(String node, double def) {
		if (!containsNumber(node))
			return def;
		return getNumber(node, def).doubleValue();
	}

	public default float getFloat(String node) {
		return getFloat(node, 0f);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as a Float. If
	 * the key cannot be found in the
	 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
	 * or for some reason something errors, it will return the {@code def} (default
	 * value) inputed. <br>
	 * <br>
	 * Also, if the underlying value is, for example, a short instead of a float,
	 * this method will convert the value into a float before returning it.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default float value in case the {@code node} specified isn't
	 *             there or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Float float}.
	 */
	public default float getFloat(String node, float def) {
		if (!containsNumber(node))
			return def;
		return getNumber(node, def).floatValue();
	}

	public default boolean getBoolean(String node) {
		return getBoolean(node, false);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as a Boolean. If
	 * the key cannot be found in the
	 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
	 * or for some reason something errors, it will return the {@code def} (default
	 * value) inputed. <br>
	 * <br>
	 * Also, if the underlying value is, for example, a boolean instead of a double,
	 * unlike other methods, this method will return the {@code def} value if the
	 * value is not a boolean.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default boolean value in case the {@code node} specified
	 *             isn't there or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Boolean boolean}.
	 */
	public default boolean getBoolean(String node, boolean def) {
		if (containsBoolean(node)) {
			return (boolean) getObject(node, def);
		}
		return def;
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as an Byte.
	 * 
	 * @param node The node to the value (key).
	 * @return Returns
	 *         {@link org.polinux.configuration.ConfigurationSection#getByte(String, byte)
	 *         ConfigurationSection.getByte(String node, byte def)}, with the
	 *         default value being {@code 0}.
	 */
	public default byte getByte(String node) {
		return getByte(node, (byte) 0);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as a Byte. If
	 * the key cannot be found in the
	 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
	 * or for some reason something errors, it will return the {@code def} (default
	 * value) inputed. <br>
	 * <br>
	 * Also, if the underlying value is, for example, a double instead of a byte,
	 * this method will convert the value into a byte before returning it.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default byte value in case the {@code node} specified isn't
	 *             there or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Byte byte}.
	 */
	public default byte getByte(String node, byte def) {
		if (!containsNumber(node))
			return def;
		return getNumber(node, def).byteValue();
	}

	public default short getShort(String node) {
		return getShort(node, (short) 0);
	}

	/**
	 * Gets the Object from the specified {@code node}, interpreted as a Short. If
	 * the key cannot be found in the
	 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
	 * or for some reason something errors, it will return the {@code def} (default
	 * value) inputed. <br>
	 * <br>
	 * Also, if the underlying value is, for example, a float instead of a short,
	 * this method will convert the value into a short before returning it.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default short value in case the {@code node} specified isn't
	 *             there or another error occurs.
	 * @return The {@code value} from the
	 *         {@link org.polinux.configuration.ConfigurationSection
	 *         ConfigurationSection} as an {@link java.lang.Short short}.
	 */
	public default short getShort(String node, short def) {
		if (!containsNumber(node))
			return def;
		return getNumber(node, def).shortValue();
	}

	/**
	 * Sets an {@code Object} to the underlying node.
	 * <p>
	 * The {@code value} argument can be set to {@code null}, but will then cause
	 * the removal of the node.
	 * 
	 * @param node The node whose value should be set.
	 * @param val  The new value for the node, {@code null} to remove it.
	 */
	public void set(String node, Object val);

	public default boolean isInt(String node) {
		return getObject(node) instanceof Integer;
	}

	public default String getString(String node) {
		return getString(node, "");
	}

	public default String getString(String node, String def) {
		if (containsString(node)) {
			return (String) getObject(node, def);
		} else if (containsObject(node)) {
			return String.valueOf(getObject(node, def));
		}
		return def;
	}

	public default boolean isString(String node) {
		return getObject(node) != null && getObject(node) instanceof String;
	}

	public default boolean isDouble(String node) {
		return getObject(node) != null && getObject(node) instanceof Double;
	}

	public default boolean isBoolean(String node) {
		return getObject(node) != null && getObject(node) instanceof Boolean;
	}

	public default boolean isLong(String node) {
		return getObject(node) != null && getObject(node) instanceof Long;
	}

	public default boolean isFloat(String node) {
		return getObject(node) != null && getObject(node) instanceof Float;
	}

	public default boolean isByte(String node) {
		return getObject(node) != null && getObject(node) instanceof Byte;
	}

	public default boolean isShort(String node) {
		return getObject(node) != null && getObject(node) instanceof Short;
	}

	public default boolean isNumber(String node) {
		return getObject(node) != null && getObject(node) instanceof Number;
	}

	public default boolean containsObject(String node) {
		return getObject(node) != null;
	}

	public default boolean containsNumber(String node) {
		return containsObject(node) && isNumber(node);
	}

	public default boolean containsBoolean(String node) {
		return containsObject(node) && isBoolean(node);
	}

	public default boolean containsFloat(String node) {
		return containsObject(node) && isFloat(node);
	}

	public default boolean containsInt(String node) {
		return containsObject(node) && isInt(node);
	}

	public default boolean containsDouble(String node) {
		return containsObject(node) && isDouble(node);
	}

	public default boolean containsLong(String node) {
		return containsObject(node) && isLong(node);
	}

	public default boolean containsString(String node) {
		return containsObject(node) && isString(node);
	}

	public default boolean containsByte(String node) {
		return containsObject(node) && isByte(node);
	}

	public default boolean containsShort(String node) {
		return containsObject(node) && isShort(node);
	}

	public default boolean isConfigurationSection(String node) {
		return getObject(node) instanceof Map;
	}

	public default boolean containsConfigurationSection(String node) {
		return getObject(node, null) != null && isConfigurationSection(node);
	}

	public Set<String> getKeys();

	public Map<String, Object> getValues();

	public String getName();

}
