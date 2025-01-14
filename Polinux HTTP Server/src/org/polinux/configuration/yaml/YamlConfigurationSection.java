package org.polinux.configuration.yaml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.polinux.configuration.ConfigurationSection;

/**
 * 
 * A {@code ConfigurationSection} is a section filled with keys and values of a
 * certain configuration file or normal file.
 * 
 * <p>
 * This interface
 * ({@link org.polinux.configuration.yaml.YamlConfigurationSection
 * YamlConfigurationSection}) is to be used when you were looking for a
 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}
 * in a certain file.
 * </p>
 * 
 * <p>
 * You may be able to everything you would be able to do in the normal
 * {@link org.polinux.configuration.yaml.YamlConfiguration YamlConfiguration}
 * class, except certain things work a bit differently.
 * </p>
 * 
 * <p>
 * For instance, in {@link org.polinux.configuration.yaml.YamlConfiguration
 * YamlConfiguration},
 * {@link org.polinux.configuration.yaml.YamlConfiguration#getParent()
 * YamlConfiguration.getParent()} would return {@code null}, because there is no
 * parent section for the "top" of the configuration.
 * </p>
 * 
 * <p>
 * But in this {@link org.polinux.configuration.yaml.YamlConfigurationSection
 * YamlConfigurationSection}, it would return the parent section of this current
 * section, instead of returning {@code null}.
 * </p>
 * 
 * @see org.polinux.configuration.yaml.YamlConfiguration YamlConfiguration
 * @see org.polinux.configuration.ConfigurationSection ConfigurationSection
 *
 */

public interface YamlConfigurationSection extends ConfigurationSection {
	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, List def)
	 *         YamlConfigurationSection.getObject(String node, Object def)}. If the
	 *         value doesn't exist in the config or for some reason something
	 *         errors, this method will return a {@code null}
	 *         {@link java.lang.Object Object}.
	 *
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	@Override
	public default Object getObject(String node) {
		return getObject(node, null);
	}

	/**
	 * @return Returns the {@code value} from the
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection} as an {@link java.lang.Object Object}. If
	 *         the key cannot be found in the
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection} or for some reason something errors, it
	 *         will return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	@Override
	public Object getObject(String node, Object def);

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getList(String node, List def)
	 *         YamlConfigurationSection.getList(String node, List def)}. If the
	 *         value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<?> getList(String node) {
		return getList(node, new ArrayList<>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)
	 *         YamlConfigurationSection.getObject(String node, Object def)},
	 *         interpreted as a {@link java.util.List List}. If the value doesn't
	 *         exist in the config or for some reason something errors, this method
	 *         will return the {@code def} (default value) inputed.
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<?> getList(String node, List<?> def) {
		if (containsList(node)) {
			return (List<?>) getObject(node);
		} else if (containsArray(node)) {

		}
		return def;
	}

	/**
	 * 
	 * @param node The node to the array.
	 * @return Returns whether the specified node is not {@code null} and it leads
	 *         to an existing Object[].
	 */
	public default boolean containsArray(String node) {
		return getObject(node) != null && getObject(node) instanceof Object[];
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getStringList(String node, List def)
	 *         YamlConfigurationSection.getStringList(String node, List def)}. If
	 *         the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<String> getStringList(String node) {
		return getStringList(node, new ArrayList<String>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)
	 *         YamlConfigurationSection.getObject(String node, Object def)},
	 *         interpreted as a {@code String List}. If the value doesn't exist in
	 *         the config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public default List<String> getStringList(String node, List<String> def) {
		if (containsList(node)) {
			try {
				return (List<String>) getList(node, def);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getIntList(String node, List def)
	 *         YamlConfigurationSection.getIntList(String node, List def)}. If the
	 *         value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<Integer> getIntList(String node) {
		return getIntList(node, new ArrayList<Integer>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)
	 *         YamlConfigurationSection.getObject(String node, Object def)},
	 *         interpreted as a {@code Integer List}. If the value doesn't exist in
	 *         the config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 */
	@SuppressWarnings("unchecked")
	public default List<Integer> getIntList(String node, List<Integer> def) {
		if (containsList(node)) {
			try {
				return (List<Integer>) getList(node, def);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getLongList(String node, List def)
	 *         YamlConfigurationSection.getLongList(String node, List def)}. If the
	 *         value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<Long> getLongList(String node) {
		return getLongList(node, new ArrayList<Long>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)
	 *         YamlConfigurationSection.getObject(String node, Object def)},
	 *         interpreted as a {@code Long List}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 */
	@SuppressWarnings("unchecked")
	public default List<Long> getLongList(String node, List<Long> def) {
		if (containsList(node)) {
			try {
				return (List<Long>) getList(node, def);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getDoubleList(String node, List def)
	 *         YamlConfigurationSection.getDoubleList(String node, List def)}. If
	 *         the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<Double> getDoubleList(String node) {
		return getDoubleList(node, new ArrayList<Double>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)
	 *         YamlConfigurationSection.getObject(String node, Object def)},
	 *         interpreted as a {@code Double List}. If the value doesn't exist in
	 *         the config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 */
	@SuppressWarnings("unchecked")
	public default List<Double> getDoubleList(String node, List<Double> def) {
		if (containsList(node)) {
			try {
				return (List<Double>) getList(node, def);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getShortList(String node, List def)
	 *         YamlConfigurationSection.getShortList(String node, List def)}. If the
	 *         value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<Short> getShortList(String node) {
		return getShortList(node, new ArrayList<Short>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)
	 *         YamlConfigurationSection.getObject(String node, Object def)},
	 *         interpreted as a {@code Short List}. If the value doesn't exist in
	 *         the config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * 
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 */
	@SuppressWarnings("unchecked")
	public default List<Short> getShortList(String node, List<Short> def) {
		if (containsList(node)) {
			try {
				return (List<Short>) getList(node, def);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getByteList(String node, List def)
	 *         YamlConfigurationSection.getByteList(String node, List def)}. If the
	 *         value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<Byte> getByteList(String node) {
		return getByteList(node, new ArrayList<Byte>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)
	 *         YamlConfigurationSection.getObject(String node, Object def)},
	 *         interpreted as a {@code Byte List}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 */
	@SuppressWarnings("unchecked")
	public default List<Byte> getByteList(String node, List<Byte> def) {
		if (containsList(node)) {
			try {
				return (List<Byte>) getList(node, def);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getFloatList(String node, List def)
	 *         YamlConfigurationSection.getFloatList(String node, List def)}. If the
	 *         value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.ArrayList
	 *         ArrayList&lt;&gt;}.
	 * @param node The node to the value (key).
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 * 
	 */
	public default List<Float> getFloatList(String node) {
		return getFloatList(node, new ArrayList<Float>());
	}

	/**
	 * 
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code Float List}. If the value doesn't exist in
	 *         the config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @param node The node to the value (key).
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * 
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String, Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String, Object)
	 */
	@SuppressWarnings("unchecked")
	public default List<Float> getFloatList(String node, List<Float> def) {
		if (containsList(node)) {
			try {
				return (List<Float>) getList(node, def);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * @param node The node of the list.
	 * @return Returns {@code true} if the object desired is a {@link java.util.List
	 *         List}. Returns {@code false} if it is not a {@link java.util.List
	 *         List}.
	 * 
	 * 
	 */
	public default boolean isList(String node) {
		return getObject(node) instanceof List;
	}

	/**
	 * @param node The node of the list.
	 * @return Returns {@code true} if the object desired is a {@link java.util.List
	 *         List} and if the configuration contains the object. Returns
	 *         {@code false} if it is not a {@link java.util.List List} or it
	 *         doesn't contain the object.
	 * 
	 * 
	 */
	public default boolean containsList(String node) {
		return containsObject(node) && isList(node);
	}

	/**
	 * @param node The node of the map.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getMap(String node, Map def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.HashMap
	 *         HashMap}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public default Map getMap(String node) {
		return getMap(node, new LinkedHashMap());
	}

	/**
	 * @param node The node of the map.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@link java.util.Map Map}. If the value doesn't
	 *         exist in the config or for some reason something errors, this method
	 *         will make return the {@code def} (default value) inputed.
	 * 
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 */
	@SuppressWarnings("rawtypes")
	public default Map getMap(String node, Map def) {
		if (isMap(node)) {
			try {
				return (Map) getObject(node);
			} catch (ClassCastException e) {
				return def;
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code Object[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String node, Object[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@link java.util.HashMap
	 *         HashMap}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * 
	 */
	public default Object[] getArray(String node) {
		return getArray(node, new Object[] {});
	}

	/**
	 * @param node The node of the {@code Object[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code Object[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String).
	 */
	public default Object[] getArray(String node, Object[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				return (Object[]) getObject(node);
			} else {
				if (isList(node)) {
					List<?> list = getList(node);
					Object[] obj = new Object[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code String[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getStringArray(String node, String[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@code String[]}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]);
	 * 
	 */
	public default String[] getStringArray(String node) {
		return getStringArray(node, new String[] {});
	}

	/**
	 * @param node The node of the {@code String[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code String[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]).
	 */
	public default String[] getStringArray(String node, String[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				try {
					return (String[]) getObject(node);
				} catch (ClassCastException e) {
					return def;
				}
			} else {
				if (isList(node)) {
					List<String> list = getStringList(node);
					String[] obj = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code double[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getDoubleArray(String node, double[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@code double[]}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]);
	 * 
	 */
	public default double[] getDoubleArray(String node) {
		return getDoubleArray(node, new double[] {});
	}

	/**
	 * @param node The node of the {@code double[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code double[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]).
	 */
	public default double[] getDoubleArray(String node, double[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				try {
					return (double[]) getObject(node);
				} catch (ClassCastException e) {
					return def;
				}
			} else {
				if (isList(node)) {
					List<Double> list = getDoubleList(node);
					double[] obj = new double[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code float[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getFloatArray(String node, float[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@code float[]}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]);
	 * 
	 */
	public default float[] getFloatArray(String node) {
		return getFloatArray(node, new float[] {});
	}

	/**
	 * @param node The node of the {@code float[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code float[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]).
	 */
	public default float[] getFloatArray(String node, float[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				try {
					return (float[]) getObject(node);
				} catch (ClassCastException e) {
					return def;
				}
			} else {
				if (isList(node)) {
					List<Float> list = getFloatList(node);
					float[] obj = new float[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code long[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getLongArray(String node, long[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@code long[]}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]);
	 * 
	 */
	public default long[] getLongArray(String node) {
		return getLongArray(node, new long[] {});
	}

	/**
	 * @param node The node of the {@code long[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code long[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object) YamlConfigurationSection.getObject(String)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object) YamlConfiguration.getObject(String)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]) YamlConfigurationSection.getArray(String,Object[])
	 */
	public default long[] getLongArray(String node, long[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				try {
					return (long[]) getObject(node);
				} catch (ClassCastException e) {
					return def;
				}
			} else {
				if (isList(node)) {
					List<Long> list = getLongList(node);
					long[] obj = new long[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code byte[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getByteArray(String node, byte[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@code byte[]}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[])
	 * 
	 */
	public default byte[] getByteArray(String node) {
		return getByteArray(node, new byte[] {});
	}

	/**
	 * @param node The node of the {@code byte[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code byte[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]).
	 */
	public default byte[] getByteArray(String node, byte[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				try {
					return (byte[]) getObject(node);
				} catch (ClassCastException e) {
					return def;
				}
			} else {
				if (isList(node)) {
					List<Byte> list = getByteList(node);
					byte[] obj = new byte[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code short[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getShortArray(String node, short[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@code short[]}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]);
	 * 
	 */
	public default short[] getShortArray(String node) {
		return getShortArray(node, new short[] {});
	}

	/**
	 * @param node The node of the {@code short[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code short[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]).
	 */
	public default short[] getShortArray(String node, short[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				try {
					return (short[]) getObject(node);
				} catch (ClassCastException e) {
					return def;
				}
			} else {
				if (isList(node)) {
					List<Short> list = getShortList(node);
					short[] obj = new short[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	/**
	 * @param node The node of the {@code int[]}.
	 * @param def  The default value in case the {@code node} specified isn't there
	 *             or another error occurs.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getIntArray(String node, int[] def)}.
	 *         If the value doesn't exist in the config or for some reason something
	 *         errors, this method will return an empty {@code int[]}.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]);
	 * 
	 */
	public default int[] getIntArray(String node) {
		return getIntArray(node, new int[] {});
	}

	/**
	 * @param node The node of the {@code int[]}.
	 * @return Returns
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String node, Object def)},
	 *         interpreted as a {@code int[]}. If the value doesn't exist in the
	 *         config or for some reason something errors, this method will make
	 *         return the {@code def} (default value) inputed.
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfiguration#getObject(String,
	 *      Object)
	 * @see org.polinux.configuration.yaml.YamlConfigurationSection#getArray(String,
	 *      Object[]).
	 */
	public default int[] getIntArray(String node, int[] def) {
		if (containsObject(node)) {
			if (isArray(node)) {
				try {
					return (int[]) getObject(node);
				} catch (ClassCastException e) {
					return def;
				}
			} else {
				if (isList(node)) {
					List<Integer> list = getIntList(node);
					int[] obj = new int[list.size()];
					for (int i = 0; i < list.size(); i++) {
						obj[i] = list.get(i);
					}
					return obj;
				} else {
					return def;
				}
			}
		}
		return def;
	}

	public Map<String, Object> toMap();

	public default Map<String, Object> getMap() {
		return toMap();
	}

	public default Map<String, Object> getData() {
		return getMap();
	}

	public boolean containsMap(String node);

	public default boolean isMap(String node) {
		return getObject(node) instanceof Map;
	}

	public boolean containsConfigurationSection(String node);

	/**
	 * 
	 * Removes the configuration section from the file (if you
	 * {@link org.polinux.configuration.yaml.YamlConfiguration#save() save()}).
	 * Example: <blockquote>
	 * 
	 * <pre>
	 *         -- Top of File --
	 * 
	 *         section1: &lt;----- Method would return this one. 
	 *           section2: &lt;----- Deleting this one. 
	 *             key: value 
	 *             key2: value2
	 * 
	 *         -- Bottom of File --
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * Will return {@code null} if removing a section at the top of the file, since
	 * there wouldn't be a parent section. Example: <blockquote>
	 * 
	 * <pre>
	 *         -- Top of File --
	 * 
	 *         section1: &lt;--- Deleting this would return {@code null}, since it doesn't have any parent sections. 
	 *           key3: value3 
	 *           key4: value4
	 * 
	 *         -- Bottom of File --
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param node The configuration section's nodes
	 * @return Returns the parent section of the one just deleted.
	 * 
	 */
	public YamlConfigurationSection removeConfigurationSection(String node);

	/**
	 * Creates the configuration section from the file (if you
	 * {@link org.polinux.configuration.yaml.YamlConfiguration#save() save()})
	 * 
	 * @param node
	 * @return Returns the
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection} created.
	 */
	public YamlConfigurationSection createConfigurationSection(String node);

	/**
	 * Get the configuration section from the file.
	 * 
	 * @param node
	 * @return Returns the
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection} entered.
	 */
	public YamlConfigurationSection getConfigurationSection(String node);

	/**
	 * 
	 * @return Returns the parent section of this
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection}.
	 */
	public YamlConfigurationSection getParent();

	/**
	 * 
	 * @return Returns the full node used to get to this
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection}.
	 */
	public String getNode();

	/**
	 * 
	 * @return Returns the name of this
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection}.
	 */
	public String getName();

	public default boolean isArray(String node) {
		return getObject(node) instanceof Object[];
	}

	/**
	 * Gets all the direct children of this {@link YamlConfigurationSection}.
	 * 
	 * @return Returns all the
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSections} in this {@code ConfigurationSection}. If
	 *         there are none (only keys and values), then it will return an empty
	 *         {@code YamlConfigurationSection[]}.
	 */
	public YamlConfigurationSection[] getChildren();

	/**
	 * Gets whether this {@link YamlConfigurationSection} has any direct children.
	 * 
	 * @return Returns whether this
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSection} has any
	 *         {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 *         YamlConfigurationSections} as it's children (if this section contains
	 *         at least one other section).
	 */
	public default boolean hasChildren() {
		return getChildren().length > 0;
	}

	/**
	 * Gets the root {@link com.configuration.configuration.yaml.YamlConfiguration
	 * YamlConfiguration} from this
	 * {@link com.configuration.configuration.yaml.YamlConfigurationSection
	 * YamlConfigurationSection}
	 * 
	 * @return The root YamlConfiguration.
	 */
	public YamlConfiguration getRoot();

	public Set<String> getKeys(boolean deep);

	@Override
	public default Set<String> getKeys() {
		return getKeys(false);
	}

	public default Map<String, Object> getValues(boolean deep) {
		return getValues(deep, false);
	}

	@Override
	public default Map<String, Object> getValues() {
		return getValues(false, false);
	}

	public Map<String, Object> getValues(boolean deep, boolean convert);

}
