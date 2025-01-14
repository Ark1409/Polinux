package org.polinux.configuration.serialization.yaml;

import java.util.ArrayList;
import java.util.List;

import org.polinux.configuration.yaml.YamlConfiguration;

/**
 * Class representing a {@link YamlConfiguration YamlConfiguration's}
 * serialization API.
 * <p>
 * 
 * This class is used to identify a class as a known serializable type, so
 * {@link YamlConfiguration YamlConfigurations} may deserialize the value when
 * fetching it from {@link YamlConfiguration#getObject(String)
 * YamlConfiguration.getObject(String)}.
 * <p>
 * This class goes hand in hand with the following:
 * <ul>
 * <li>{@link YamlConfigurationSerializable}
 * <li>{@link YamlConfigurationSerializableAs}
 * <li>{@link YamlConfiguration#getYamlConfigurationSerializationAPI()}
 * </ul>
 * 
 * @see {@link YamlConfigurationSerializable}
 * @see {@link YamlConfigurationSerializableAs}
 * @see {@link YamlConfiguration#getYamlConfigurationSerializationAPI()}
 */
public final class YamlConfigurationSerialization {
	/**
	 * The name of the current serialization api
	 */
	private String name;
	/**
	 * Known serialization apis
	 */
	private static final List<YamlConfigurationSerialization> apis = new ArrayList<YamlConfigurationSerialization>();
	
	/**
	 * Default serialization api
	 */
	private static final YamlConfigurationSerialization instance = new YamlConfigurationSerialization("DEFAULT");
	private final List<Class<? extends YamlConfigurationSerializable>> classList = new ArrayList<Class<? extends YamlConfigurationSerializable>>();

	private YamlConfigurationSerialization(String name) {
		this.name = name;
	}

	public static YamlConfigurationSerialization getDefaultYamlConfigurationSerializationAPI() {
		return instance;
	}

	public static YamlConfigurationSerialization getYamlConfigurationSerializationAPI(String name) {
		if (name.equals("DEFAULT"))
			return getDefaultYamlConfigurationSerializationAPI();
		if (containsYamlConfigurationSerializationAPI(name)) {
			for (int i = 0; i < apis.size(); i++) {
				YamlConfigurationSerialization api = apis.get(i);
				if (api.getIdentifier().equals(name))
					return api;
			}
		}
		try {
			return createYamlConfigurationSerializationAPI(name);
		} catch (YamlConfigurationSerializationException e) {
			try {
				throw new YamlConfigurationSerializationException(
						"Unable to create a Yaml Configuration Serialization API with the name of " + name);
			} catch (YamlConfigurationSerializationException e1) {
				e1.printStackTrace();
				e.printStackTrace();
			}
		}
		return null;
	}

	private static boolean containsYamlConfigurationSerializationAPI(String name) {
		for (int i = 0; i < apis.size(); i++) {
			YamlConfigurationSerialization api = apis.get(i);
			if (api.getIdentifier().equals(name))
				return true;
		}
		return false;
	}

	private static YamlConfigurationSerialization createYamlConfigurationSerializationAPI(String name)
			throws YamlConfigurationSerializationException {
		if (containsYamlConfigurationSerializationAPI(name)) {
			throw new YamlConfigurationSerializationException(
					"The Yaml Configuration Serialization API " + name + " already exists");
		}
		YamlConfigurationSerialization api = new YamlConfigurationSerialization(name);
		apis.add(api);
		return api;
	}

	/**
	 * Adds a {@literal Class<? extends} {@link YamlConfigurationSerializable
	 * YamlConfigurationSerializable}{@literal>} to the list of classes to search
	 * for while serializing. This method must takes one paramater, the class that
	 * is being added. <br>
	 * <br>
	 * 
	 * The class paramater is used whenever a {@link YamlConfiguration} finds a
	 * serialized Object inside it's configuration. Whenever that happens, the
	 * YamlConfiguration class this specified class's
	 * <i>{@literal deserialize(Map<String, Object> data)}</i> method. This class
	 * will also be used when saving something into the YamlConfiguration, for the
	 * YamlConfiguration will call the specified class's
	 * {@link YamlConfigurationSerializable#serialize() serialize()} method.
	 * 
	 * @param clazz The class to add to the list.
	 * 
	 */
	public void addClass(Class<? extends YamlConfigurationSerializable> clazz) {
		classList.add(clazz);
	}

	/**
	 * Gets the list of {@literal Class<? extends}
	 * {@link YamlConfigurationSerializable
	 * YamlConfigurationSerializable}{@literal>} that the
	 * YamlConfigurationSerialization API should check for.
	 * 
	 * @return The list of classes ({@literal Class<? extends}
	 *         {@link YamlConfigurationSerializable
	 *         YamlConfigurationSerializable}{@literal>}).
	 */
	public List<Class<? extends YamlConfigurationSerializable>> getClasses() {
		return classList;
	}

	public Class<? extends YamlConfigurationSerializable> getClass(String fullClassName) {
		for (int i = 0; i < classList.size(); i++) {
			Class<? extends YamlConfigurationSerializable> clazz = classList.get(i);
			if (clazz.getName().equals(fullClassName)
					|| clazz.getName().equals(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX + fullClassName)
					|| clazz.getName().equals(
							fullClassName.replaceFirst(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX, "")))
				return clazz;
			if (clazz.getAnnotation(YamlConfigurationSerializableAs.class) != null) {
				String val = clazz.getAnnotation(YamlConfigurationSerializableAs.class).value();
				if (val.equals(fullClassName))
					return clazz;
			}
			if (fullClassName.length() > YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX.length()) {
				if (clazz.getName().equals(fullClassName.substring(
						YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX.length(), fullClassName.length()))) {
					return clazz;
				}
			}
//			|| clazz.getName().equals(fullClassName.substring(
//					YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX.length(), fullClassName.length()))
		}
		return null;
	}

	public String getIdentifier() {
		return name;
	}

	/**
	 * Removes the specified class from the list of {@literal Class<? extends}
	 * {@link YamlConfigurationSerializable
	 * YamlConfigurationSerializable}{@literal>} to be checked for whenever a value
	 * in the {@link org.polinux.configuration.yaml.YamlConfiguration
	 * YamlConfiguration} has the YamlConfigurationSerialization prefix
	 * ({@link org.polinux.configuration.serialization.yaml.YamlConfigurationSerializable#CONFIG_SERIALIZATION_PREFIX
	 * YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX}).
	 * 
	 * 
	 * @param clazz The class to be removed.
	 */
	public void removeClass(Class<? extends YamlConfigurationSerializable> clazz) {
		if (classList.contains(clazz))
			classList.remove(clazz);
	}
	
	/**
	 * {@link YamlConfiguration} Serialization Exception
	 */
	public static final class YamlConfigurationSerializationException extends Exception {

		private static final long serialVersionUID = 1L;

		public YamlConfigurationSerializationException() {
			super();
		}

		public YamlConfigurationSerializationException(String message) {
			super(message);
		}

		public YamlConfigurationSerializationException(String message, Throwable cause) {
			super(message, cause);
		}

		public YamlConfigurationSerializationException(Throwable cause) {
			super(cause);
		}

		protected YamlConfigurationSerializationException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

	}

}
