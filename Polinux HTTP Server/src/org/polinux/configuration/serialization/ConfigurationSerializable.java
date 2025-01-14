package org.polinux.configuration.serialization;

import java.util.Map;

/**
 * Class used to store all classes to be used for the ConfigurationSerialization
 * API. Super class used for the serializability of an Object or class. View
 * implementations of this interface for more detail on how to serialize Objects
 * or classes in the specified {@link org.polinux.configuration.Configuration
 * Configuration}.
 * 
 * @deprecated It is better to used an implemented Configuration Serialization
 *             API, like the
 *             {@link org.polinux.configuration.serialization.yaml.YamlConfigurationSerializatble
 *             YamlConfigurationSerializable} class.
 *
 */
@Deprecated
public abstract interface ConfigurationSerializable {
	/**
	 * The default prefix used for creating ConfigurationSections that are meant to
	 * represent a serialized object or class. View View implementations of this
	 * interface for their serialization prefixes.
	 */
	public static final String CONFIG_SERIALIZATION_PREFIX = "==!";

	/**
	 * Method used to serialize the specified class that implements
	 * {@link ConfigurationSerializable ConfigurationSerializable}. The Map returned
	 * by this method will be the same map in the paramater of the
	 * {@literal deserialize(Map<String,Object> data)} method. View implementations
	 * of this interface for more detail.
	 * 
	 * @return The class to be serialized, as a Map (sections).
	 */
	public Map<String, Object> serialize();

}
