package org.polinux.configuration.yaml;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.polinux.configuration.serialization.yaml.YamlConfigurationSerializable;
import org.polinux.configuration.serialization.yaml.YamlConfigurationSerializableAs;

class YamlConfigurationSectionClass implements YamlConfigurationSection {
	private YamlConfiguration y;
	private Map<String, Object> map;
	private String name;
	private String fullNode;

	YamlConfigurationSectionClass(YamlConfiguration y, Map<String, Object> m, String name, String fullNode) {
		if (y == null) {
			try {
				throw new YamlConfigurationException("Yaml instance given was null!", null);
			} catch (YamlConfigurationException e) {
				e.printStackTrace();
			}

		}
		this.y = y;
		if (m == null) {
			try {
				throw new YamlConfigurationException("Data (Map instance (Map<String, Object>)) given was null!", this.y);
			} catch (YamlConfigurationException e) {
				e.printStackTrace();
			}

		}
		this.map = m;
		this.name = name;
		this.fullNode = fullNode;
	}

	@Override
	public Object getObject(String node, Object def) {
		try {
			return getNoCreating(node, toMap(), node);
		} catch (YamlConfigurationException e) {
			return def;
		}
	}

	@Override
	public void set(String node, Object val) {
		String[] nodes = node.split(YamlConfiguration.seperatorRegex);
		if (nodes.length == 1) {
			if (val == null) {
				// ((Map<String,Object>)toMap().get(nodes[0])).clear();
				this.map.remove(nodes[0]);
				return;
			}
			if (val instanceof YamlConfigurationSerializable) {
				Map<String, Object> data = ((YamlConfigurationSerializable) val).serialize();
				Map<String, Object> data2 = new HashMap<String, Object>();
				if (val.getClass().getAnnotation(YamlConfigurationSerializableAs.class) != null) {
					data2.put(
							YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX
									+ val.getClass().getAnnotation(YamlConfigurationSerializableAs.class).value(),
							data);
					this.map.put(nodes[0], data2);
					return;
				}
				data2.put(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX + val.getClass().getName(), data);
				this.map.put(nodes[0], data2);
				return;
			}
		}
		String stringSec = "";
		for (int i = 0; i < nodes.length; i++) {
			if (i == (nodes.length - 1)) {
				break;
			}

			if (i == (nodes.length - 2)) {
				stringSec += nodes[i];
				continue;
			}
			stringSec += nodes[i] + YamlConfiguration.seperator;
		}
		if (stringSec.equalsIgnoreCase(""))
			stringSec = node;
		Map<String, Object> sec = getSectionCreating(stringSec, this.deserializeAll(), stringSec);
		if (val == null) {
			sec.clear();
			return;
		}
		if (val instanceof YamlConfigurationSerializable) {
			Map<String, Object> data = ((YamlConfigurationSerializable) val).serialize();
			Map<String, Object> data2 = new HashMap<String, Object>();
			if (val.getClass().getAnnotation(YamlConfigurationSerializableAs.class) != null) {
				data2.put(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX
						+ val.getClass().getAnnotation(YamlConfigurationSerializableAs.class).value(), data);
				sec.put(nodes[nodes.length - 1], val);
				return;
			}
			data2.put(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX + val.getClass().getName(), data);
			sec.put(nodes[nodes.length - 1], data2);
			return;
		}
		sec.put(nodes[nodes.length - 1], val);
	}

	@Override
	public YamlConfigurationSection getConfigurationSection(String node) {
		Map<String, Object> sec = null;
		try {
			sec = getSectionNoCreating(node, toMap(), node);
		} catch (YamlConfigurationException e) {
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this.y);
			} catch (YamlConfigurationException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		if (sec == null) {
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this.y);
			} catch (YamlConfigurationException e) {
				e.printStackTrace();
			}
			return null;
		}
		return new YamlConfigurationSectionClass(this.y, sec,
				node.split(YamlConfiguration.seperatorRegex)[node.split(YamlConfiguration.seperatorRegex).length - 1],
				getNode() + YamlConfiguration.seperator + node);
	}

	@Override
	public boolean isInt(String node) {
		return getObject(node) instanceof Integer;
	}

	@Override
	public boolean isString(String node) {
		return getObject(node) instanceof String;
	}

	@Override
	public boolean isDouble(String node) {
		return getObject(node) instanceof Double;
	}

	@Override
	public boolean isBoolean(String node) {
		return getObject(node) instanceof Boolean;
	}

	@Override
	public boolean containsObject(String node) {
		return getObject(node) != null;
	}

	@Override
	public boolean containsBoolean(String node) {
		return containsObject(node) && isBoolean(node);
	}

	@Override
	public boolean containsFloat(String node) {
		return containsObject(node) && isFloat(node);
	}

	@Override
	public boolean containsInt(String node) {
		return containsObject(node) && isInt(node);
	}

	@Override
	public boolean containsDouble(String node) {
		return containsObject(node) && isDouble(node);
	}

	@Override
	public boolean containsLong(String node) {
		return containsObject(node) && isLong(node);
	}

	@Override
	public boolean containsString(String node) {
		return containsObject(node) && isString(node);
	}

	@Override
	public boolean isConfigurationSection(String node) {
		return getObject(node) instanceof Map;
	}

	@Override
	public YamlConfigurationSection createConfigurationSection(String node) {
		String[] nodes = node.trim().split(YamlConfiguration.seperatorRegex);
		String stringSec = "";
		for (int i = 0; i < nodes.length; i++) {
			if (i == (nodes.length - 1)) {
				break;
			}

			if (i == (nodes.length - 2)) {
				stringSec += nodes[i];
				continue;
			}
			stringSec += nodes[i] + YamlConfiguration.seperator;
		}
		if (stringSec.equalsIgnoreCase(""))
			stringSec = node;
		Map<String, Object> sec = getSectionCreating(stringSec, toMap(), stringSec);
		sec.put(nodes[nodes.length - 1], new HashMap<String, Object>());
		return getConfigurationSection(node);
	}

	/**
	 * 
	 * @return Returns the {@code YamlConfiguration} linked with this section.
	 */
	YamlConfiguration getYamlConfiguration() {
		return this.y;
	}

	@Override
	public Map<String, Object> toMap() {
		return this.deserializeMap(this.map);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getMap(String node, Map def) {
		if (containsMap(node)) {
			return (Map) getObject(node);
		}
		return def;
	}

	@Override
	public boolean containsMap(String node) {
		return containsObject(node) && isMap(node);
	}

	@Override
	public boolean isMap(String node) {
		return getObject(node) instanceof Map;
	}

	@SuppressWarnings({ "unchecked" })
	private Map<String, Object> getSectionNoCreating(String node, Map<String, Object> m, String fullNode)
			throws YamlConfigurationException {
		String[] nodes = node.trim().split(YamlConfiguration.seperatorRegex);

		if (nodes.length == 1) {
			if (m.get(nodes[0]) == null || !(m.get(nodes[0]) instanceof Map)) {
				throw new YamlConfigurationException(nodes[0] + " is not a section!", this.y);
			}
			return (Map<String, Object>) m.get(nodes[0]);
		}

		String newNode = "";
		for (int i = 0; i < nodes.length; i++) {
			if (i == 0) {
				continue;
			}
			if (i == (nodes.length - 1)) {
				newNode += nodes[i];
				continue;
			}
			newNode += nodes[i] + YamlConfiguration.seperator;
		}
		if (m.get(nodes[0]) == null || !(m.get(nodes[0]) instanceof Map)) {
			throw new YamlConfigurationException(nodes[0] + " is not a section!", this.y);
		}
		return getSectionNoCreating(newNode, (Map<String, Object>) m.get(nodes[0]), fullNode);

	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getSectionCreating(String node, Map<String, Object> m, String fullNode) {
		String[] nodes = node.trim().split(YamlConfiguration.seperatorRegex);
		if (nodes.length == 1) {
			if (m.get(nodes[0]) == null) {
				m.put(nodes[0], new HashMap<String, Object>());
			} else if (!(m.get(nodes[0]) instanceof Map)) {
				m.remove(nodes[0]);
				m.put(nodes[0], new HashMap<String, Object>());
			}
			return (Map<String, Object>) m.get(nodes[0]);
		}
		String newNode = "";
		for (int i = 0; i < nodes.length; i++) {
			if (i == 0) {
				continue;
			}
			if (i == (nodes.length - 1)) {
				newNode += nodes[i];
				continue;
			}
			newNode += nodes[i] + YamlConfiguration.seperator;
		}

		if (m.get(nodes[0]) == null) {
			m.put(nodes[0], new HashMap<String, Object>());
		}
		if (!(m.get(nodes[0]) instanceof Map)) {
			((Map<String, Object>) m.get(nodes[0])).clear();
			m.put(nodes[0], new HashMap<String, Object>());
		}
		return getSectionCreating(newNode, (Map<String, Object>) m.get(nodes[0]), fullNode);
	}

	@SuppressWarnings("unchecked")
	private Object getNoCreating(String node, Map<String, Object> m, String fullNode) throws YamlConfigurationException {
		String[] nodes = node.trim().split("\\" + YamlConfiguration.seperator);
		if (nodes.length == 1) {
			if (m.get(nodes[0]) == null) {
				throw new YamlConfigurationException(nodes[0] + " is not a value in the config!", this.y);
			}
			return m.get(nodes[0]);
		}

		String newNode = "";
		for (int i = 0; i < nodes.length; i++) {
			if (i == 0) {
				continue;
			}
			if (i == (nodes.length - 1)) {
				newNode += nodes[i];
				continue;
			}
			newNode += nodes[i] + YamlConfiguration.seperator;
		}

		if (m.get(nodes[0]) == null || !(m.get(nodes[0]) instanceof Map)) {
			throw new YamlConfigurationException(nodes[0] + " is not a section!", this.y);
		}

		return getNoCreating(newNode, this.deserializeMap((Map<String, Object>) m.get(nodes[0])), fullNode);
	}

	@Override
	public boolean containsConfigurationSection(String node) {
		Map<String, Object> sec = null;
		try {
			sec = getSectionNoCreating(node, this.deserializeAll(), node);
		} catch (YamlConfigurationException e) {
			return false;
		}
		if (sec == null) {
			return false;
		}
		return true;
	}

	public Map<String, Object> getValues(boolean deep, boolean convert) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!deep) {
			Map<String, Object> data = this.deserializeAll();
			if (convert) {
				Set<String> keys = data.keySet();
				for (String key : keys) {
					Object o = this.getObject(key);
					if (o instanceof Map) {
						YamlConfigurationSection sec = this.getConfigurationSection(key);
						map.put(key, sec);
					} else {
						map.put(key, o);
					}

				}
			} else {
				Set<String> keys = data.keySet();
				for (String key : keys) {
					Object o = this.getObject(key);
					map.put(key, o);

				}
			}
			return map;
		}
		if (convert) {
			Set<String> keys = this.getKeys(true);
			for (String key : keys) {
				Object o = this.getObject(key);
				if (o instanceof Map) {
					YamlConfigurationSection sec = this.getConfigurationSection(key);
					map.put(key, sec);
				} else {
					map.put(key, o);
				}
			}
		} else {
			Set<String> keys = this.getKeys(true);
			for (String key : keys) {
				Object o = this.getObject(key);
				map.put(key, o);
			}
		}

		return map;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YamlConfigurationSection removeConfigurationSection(String node) {
		String[] nodes = node.trim().split(YamlConfiguration.seperatorRegex);
		if (nodes.length == 1) {
			try {
				((Map<String, Object>) this.toMap().get(nodes[0])).clear();
			} catch (ClassCastException e) {
				try {
					throw new YamlConfigurationException("Section " + node + " is not a section, so it could not be removed",
							this.y);
				} catch (YamlConfigurationException e1) {
					e1.printStackTrace();
				}
			}
			this.toMap().remove(nodes[0]);
			return this;
		}

		String newSec = "";
		for (int i = 0; i < nodes.length; i++) {
			if (i == (nodes.length - 1)) {
				break;
			}

			if (i == (nodes.length - 2)) {
				newSec += nodes[i];
				break;
			}
			newSec += nodes[i] + YamlConfiguration.seperator;
		}
		Map<String, Object> sec = null;
		Map<String, Object> secR = null;
		try {
			sec = getSectionNoCreating(node, toMap(), node);
			secR = getSectionNoCreating(newSec, toMap(), newSec);
		} catch (YamlConfigurationException e) {
			e.printStackTrace();
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this.y);
			} catch (YamlConfigurationException e1) {
				e1.printStackTrace();
			}
		}
		if (sec == null) {
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this.y);
			} catch (YamlConfigurationException e) {
				e.printStackTrace();
			}
		}
		sec.clear();
		secR.remove(nodes[nodes.length - 1]);
		return getConfigurationSection(newSec);
	}

	@Override
	public YamlConfigurationSection getParent() {
		String[] nodes = this.fullNode.trim().split(YamlConfiguration.seperatorRegex);
		if (nodes.length == 1)
			return this.y;
		String newSec = "";
		for (int i = 0; i < nodes.length; i++) {
			if (i == (nodes.length - 1)) {
				break;
			}

			if (i == (nodes.length - 2)) {
				newSec += nodes[i];
				break;
			}
			newSec += nodes[i] + YamlConfiguration.seperator;
		}
		if (newSec.equalsIgnoreCase(""))
			newSec = nodes[0];
		return this.y.getConfigurationSection(newSec);
	}

	@Override
	/**
	 * @return Returns the full node leading to this configuration section.
	 */
	public String getNode() {
		return this.fullNode;
	}

	@Override
	public Object[] getArray(String node) {
		return getArray(node, new Object[] {});
	}

	@Override
	public Object[] getArray(String node, Object[] def) {
		if (containsObject(node)) {
			return (Object[]) getObject(node);
		}
		return def;
	}

	public boolean isArray(String node) {
		return getObject(node) instanceof Object[];
	}

	@Override
	public YamlConfigurationSection[] getChildren() {
		List<YamlConfigurationSection> sections = new LinkedList<YamlConfigurationSection>();
		for (Entry<String, Object> e : this.deserializeAll().entrySet()) {
			if (isConfigurationSection(e.getKey()))
				sections.add(this.getConfigurationSection(e.getKey()));
		}
		return sections.toArray(new YamlConfigurationSection[0]);
	}

	@Override
	public YamlConfiguration getRoot() {
		return this.getYamlConfiguration();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getKeys(boolean deep) {
		if (!deep)
			return this.deserializeAll().keySet();
		Set<String> keys = new LinkedHashSet<String>();
		for (Entry<String, Object> e : this.deserializeAll().entrySet()) {
			String key = e.getKey();
			Object val = e.getValue();
			keys.add(key);
			if (val instanceof Map) {
				keys.addAll(getKeysDeep(key, (Map<String, Object>) val));
			}
		}
		return keys;
	}

	@SuppressWarnings("unchecked")
	private Set<String> getKeysDeep(String before, Map<String, Object> map) {
		Set<String> keys = new LinkedHashSet<String>();
		for (Entry<String, Object> e : map.entrySet()) {
			String key = e.getKey();
			Object val = e.getValue();
			keys.add(before + YamlConfiguration.seperator + key);
			if (val instanceof Map) {
				keys.addAll(getKeysDeep(before + YamlConfiguration.seperator + key, (Map<String, Object>) val));
			}
		}
		return keys;
	}

	private Map<String, Object> deserializeAll() {
		return deserializeMap(this.map);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> deserializeMap(Map<String, Object> data) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Entry<String, Object> e : data.entrySet()) {
			String firstKey = e.getKey();
			Object firstValue = e.getValue();
			if (!(firstValue instanceof Map)) {
				map.put(firstKey, firstValue);
				continue;
			}
			Map<String, Object> secondMap = (Map<String, Object>) e.getValue();
			for (Entry<String, Object> e1 : secondMap.entrySet()) {
				String secondKey = e1.getKey();
				Object secondValue = e1.getValue();
				if (secondKey.startsWith(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX)) {
					Class<?> clazz = YamlConfiguration.getYamlConfigurationSerializationAPI()
							.getClass(secondKey.substring(
									YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX.length(),
									secondKey.length()));
					if (clazz == null) {
						if (secondValue instanceof Map) {
							if (((Map<String, Object>) map.get(firstKey)) == null) {
								map.put(firstKey, new LinkedHashMap<String, Object>());
							}
							((Map<String, Object>) map.get(firstKey)).put(secondKey,
									this.deserializeMap((Map<String, Object>) secondValue));
						} else {
							if (((Map<String, Object>) map.get(firstKey)) == null) {
								map.put(firstKey, new LinkedHashMap<String, Object>());
							}
							((Map<String, Object>) map.get(firstKey)).put(secondKey, secondValue);
						}
						continue;
					}
					Object objClass = null;
					try {
						objClass = clazz.getMethod("deserialize", Map.class).invoke(null, secondValue);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e2) {
						e2.printStackTrace();
					}
//					if (((Map<String, Object>) map.get(firstKey)) == null) {
//						map.put(firstKey, new LinkedHashMap<String, Object>());
//					}

					map.put(firstKey, objClass);
					continue;
				} else {
					if (secondValue instanceof Map) {
						if (((Map<String, Object>) map.get(firstKey)) == null) {
							map.put(firstKey, new LinkedHashMap<String, Object>());
						}
						((Map<String, Object>) map.get(firstKey)).put(secondKey,
								this.deserializeMap((Map<String, Object>) secondValue));
					} else {
						if (((Map<String, Object>) map.get(firstKey)) == null) {
							map.put(firstKey, new LinkedHashMap<String, Object>());
						}
						((Map<String, Object>) map.get(firstKey)).put(secondKey, secondValue);
					}
					continue;
				}

			}
		}
		return map;
	}

}
