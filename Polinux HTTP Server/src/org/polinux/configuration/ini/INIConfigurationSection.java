package org.polinux.configuration.ini;

import java.util.Map;
import java.util.Set;

import org.polinux.configuration.ConfigurationSection;

/**
 * An {@code INIConfigurationSection} is a section inside of any ini files that
 * have certain keys and values that may be added to removed. An
 * INIConfigurationSection usually starts and ends with a square brackets. <br>
 * <br>
 * This interface is meant to represent a configuration section inside any ini
 * files. <br>
 * <br>
 * It is only possible to access this class from the {@link INIConfiguration
 * INIConfiguration} itself, if trying to read a configuration.<br>
 * <br>
 * Also, something you should take note of is that
 * {@link #getConfigurationSection(String) getting},
 * {@link #createConfigurationSection(String) setting} and
 * {@link #removeConfigurationSection(String) removing} configuration sections
 * inside the underlying ini file will be impossible if accessing this interface
 * from the real configuration section, and not from the {@link INIConfiguration
 * INIConfiguration} class. So if you try to use any of those methods while
 * inside a section, this method will return {@code null}.
 *
 */
public interface INIConfigurationSection extends ConfigurationSection {
	public INIConfigurationSection getConfigurationSection(String node);

	public INIConfigurationSection createConfigurationSection(String node);

	public INIConfigurationSection removeConfigurationSection(String node);

	public Set<String> getKeys(boolean deep);

	public Map<String, Object> getValues(boolean deep);

	public INIConfiguration getRoot();
}
