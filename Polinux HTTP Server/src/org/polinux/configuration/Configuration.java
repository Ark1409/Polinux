package org.polinux.configuration;

import java.io.File;

import org.polinux.configuration.serialization.ConfigurationSerialization;

/**
 * The base Configuration class to be used whenever representing any type of
 * configuration file.
 * 
 * @see org.polinux.configuration.yaml.YamlConfiguration YamlConfiguration
 * @see org.polinux.configuration.properties.PropertiesConfiguration
 *      PropertiesConfiguration
 * @see org.polinux.configuration.ini.INIConfiguration INIConfiguration
 */
@SuppressWarnings("deprecation")
public abstract class Configuration implements ConfigurationSection {
	/**
	 * Configuration file
	 */
	protected File f;
	
	/**
	 * {@link ConfigurationSerialization Configuration Serialization API}
	 * 
	 * @deprecated See {@link ConfigurationSerialization}
	 * 
	 * @see {@link ConfigurationSerialization}
	 */
	@Deprecated
	protected static final ConfigurationSerialization serializationAPI = ConfigurationSerialization
			.getDefaultConfigurationSerializationAPI();

	/**
	 * Constructs a configuration.
	 * 
	 * @param path The path (filepath) of the configuration. Cannot be {@code null.}
	 */
	protected Configuration(String path) {
		this(new File(path));
	}

	/**
	 * Constructs a configuration.
	 * 
	 * @param file The file of the configuration. Should not be {@code null}.
	 */
	protected Configuration(File file) {
		this.f = file;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isInt(String node) {
//		if (getObject(node) != null && getObject(node) instanceof Integer)
//			return true;
//		return false;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isString(String node) {
//		if (getObject(node) != null && getObject(node) instanceof String)
//			return true;
//		return false;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isDouble(String node) {
//		if (getObject(node) != null && getObject(node) instanceof Double)
//			return true;
//		return false;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isBoolean(String node) {
//		if (getObject(node) != null && getObject(node) instanceof Boolean)
//			return true;
//		return false;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean containsObject(String node) {
//		return getObject(node) != null;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isLong(String node) {
//		if (getObject(node) != null && getObject(node) instanceof Long)
//			return true;
//		return false;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isFloat(String node) {
//		if (getObject(node) != null && getObject(node) instanceof Float)
//			return true;
//		return false;
//	}
//
	/**
	 * Retrieves the file for this configuration.
	 * 
	 * @return The configuration's file.
	 */
	public File getFile() {
		return this.f;
	}

	/**
	 * Sets the file for this configuration to the underlying file.
	 * 
	 * @param file The new file for the configuration. Should not be {@code null}.
	 */
	public void setFile(File file) {
		this.f = file;
	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean containsBoolean(String node) {
//		return containsObject(node) && isBoolean(node);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean containsFloat(String node) {
//		return containsObject(node) && isFloat(node);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean containsInt(String node) {
//		return containsObject(node) && isInt(node);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean containsDouble(String node) {
//		return containsObject(node) && isDouble(node);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean containsLong(String node) {
//		return containsObject(node) && isLong(node);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean containsString(String node) {
//		return containsObject(node) && isString(node);
//	}

	/**
	 * Gets the Default Configuration Serialization API for the base of most
	 * configurations.
	 * 
	 * @deprecated See
	 *             {@link org.polinux.configuration.serialization.ConfigurationSerialization
	 *             ConfigurationSerialization}.
	 * @return The Default Configuration Serialization API.
	 * @see org.polinux.configuration.serialization.ConfigurationSerialization
	 *      ConfigurationSerialization
	 */
	@Deprecated
	public static ConfigurationSerialization getDefaultConfigurationSerializationAPI() {
		return serializationAPI;
	}

}
