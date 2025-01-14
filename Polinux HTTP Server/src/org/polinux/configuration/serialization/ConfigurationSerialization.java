package org.polinux.configuration.serialization;

import java.util.ArrayList;
import java.util.List;

import org.polinux.configuration.Configuration;
import org.polinux.configuration.serialization.yaml.YamlConfigurationSerialization;
import org.polinux.configuration.yaml.YamlConfiguration;

/**
 * Generalized class used to store all classes to be used for a
 * ConfigurationSerialization API. Multiple API may be created with this class,
 * by use of the {@link #getConfigurationSerializationAPI(String)} method. So
 * you may create different APIS for your different configuration. Although, you
 * must also note that a few names are <i>usually</i> reserved for certain
 * classes:
 * <ul>
 * <li><i>DEFAULT</i>
 * <ul>
 * <li>Reserved for the default configuration serialization API.
 * {@link ConfigurationSerialization#getDefaultConfigurationSerializationAPI()
 * getDefaultConfigurationSerializationAPI()} is equivalent to
 * {@link ConfigurationSerialization#getConfigurationSerializationAPI(String)
 * getConfigurationSerializationAPI("<i>DEFAULT</i>")}
 * </ul>
 * <li>
 * </pre>
 * </ul>
 * 
 * @deprecated It is better to used an implemented Configuration Serialization
 *             API, like the
 *             {@link org.polinux.configuration.serialization.yaml.YamlConfigurationSerialization
 *             YamlConfigurationSerialization} class, since this Configuration
 *             Serialization API generalizes the configuration serializable
 *             instance that will be kept inside this class, while other
 *             serialization api (like the YamlConfigurationSerializationAPI)
 *             are better suited for a specified type of configuration.
 *             Furthermore, you may create your own serialization api for your
 *             specific file type. It may not extend this class, since it is
 *             final, but you may copy the source code from this class to create
 *             your own API.
 * 
 * 
 *             So basically, use a serialization api that is well suited for
 *             your file type. Generialized classes are not recommended.
 */
@Deprecated
public final class ConfigurationSerialization {
	private String name;
	private static final List<ConfigurationSerialization> apis = new ArrayList<ConfigurationSerialization>();
	private static final ConfigurationSerialization instance = new ConfigurationSerialization("DEFAULT");
	private final List<Class<? extends ConfigurationSerializable>> classList = new ArrayList<Class<? extends ConfigurationSerializable>>();

	public ConfigurationSerialization(String name) {
		this.name = name;
	}

	public static ConfigurationSerialization getDefaultConfigurationSerializationAPI() {
		return instance;
	}

	public static ConfigurationSerialization getConfigurationSerializationAPI(String name) {
		if (name.equals("DEFAULT"))
			return getDefaultConfigurationSerializationAPI();
		if (containsConfigurationSerializationAPI(name)) {
			for (int i = 0; i < apis.size(); i++) {
				ConfigurationSerialization api = apis.get(i);
				if (api.getIdentifier().equals(name))
					return api;
			}
		}
		try {
			return createConfigurationSerializationAPI(name);
		} catch (ConfigurationSerializationException e) {
			try {
				throw new ConfigurationSerializationException(
						"Unable to create a Configuration Serialization API with the name of " + name);
			} catch (ConfigurationSerializationException e1) {
				e1.printStackTrace();
				e.printStackTrace();
			}
		}
		return null;
	}

	private static boolean containsConfigurationSerializationAPI(String name) {
		for (int i = 0; i < apis.size(); i++) {
			ConfigurationSerialization api = apis.get(i);
			if (api.getIdentifier().equals(name))
				return true;
		}
		return false;
	}

	private static ConfigurationSerialization createConfigurationSerializationAPI(String name)
			throws ConfigurationSerializationException {
		if (containsConfigurationSerializationAPI(name)) {
			throw new ConfigurationSerializationException(
					"The Configuration Serialization API " + name + " already exists");
		}
		ConfigurationSerialization api = new ConfigurationSerialization(name);
		apis.add(api);
		return api;
	}

	/**
	 * Adds a {@literal Class<? extends} {@link ConfigurationSerializable
	 * ConfigurationSerializable}{@literal>} to the list of classes to search for
	 * while serializing. This method must takes one paramater, the class that is
	 * being added. <br>
	 * <br>
	 * 
	 * The class paramater is used whenever a {@link Configuration} finds a
	 * serialized Object inside it's configuration. Whenever that happens, the
	 * Configuration class this specified class's
	 * <i>{@literal deserialize(Map<String, Object> data)}</i> method. This class
	 * will also be used when saving something into the Configuration, for the
	 * Configuration will call the specified class's
	 * {@link ConfigurationSerializable#serialize() serialize()} method.
	 * 
	 * @param clazz The class to add to the list.
	 * 
	 */
	public void addClass(Class<? extends ConfigurationSerializable> clazz) {
		classList.add(clazz);
	}

	/**
	 * Gets the list of {@literal Class<? extends} {@link ConfigurationSerializable
	 * ConfigurationSerializable}{@literal>} that the ConfigurationSerialization API
	 * should check for.
	 * 
	 * @return The list of classes ({@literal Class<? extends}
	 *         {@link ConfigurationSerializable
	 *         ConfigurationSerializable}{@literal>}).
	 */
	public List<Class<? extends ConfigurationSerializable>> getClasses() {
		return classList;
	}

	public String getIdentifier() {
		return name;
	}

	public static YamlConfigurationSerialization getYamlConfigurationSerializationAPI() {
		return YamlConfiguration.getYamlConfigurationSerializationAPI();
	}

	/**
	 * {@link Configuration} Serialization Exception
	 */
	public static class ConfigurationSerializationException extends Exception {

		private static final long serialVersionUID = 1L;

		public ConfigurationSerializationException() {
			super();
		}

		public ConfigurationSerializationException(String message) {
			super(message);
		}

		public ConfigurationSerializationException(String message, Throwable cause) {
			super(message, cause);
		}

		public ConfigurationSerializationException(Throwable cause) {
			super(cause);
		}

		protected ConfigurationSerializationException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

	}
}
