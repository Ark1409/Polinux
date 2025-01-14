package org.polinux.configuration.serialization.yaml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to set how a
 * {@link org.polinux.configuration.serialization.yaml.YamlConfigurationSerializable
 * YamlConfigurationSerializable} should appear inside the configuration.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YamlConfigurationSerializableAs {
	/**
	 * The alias for this class
	 * 
	 * @return The new serializable value
	 */
	public String value();
}
