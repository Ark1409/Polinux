package org.polinux.configuration.ini;

import java.util.Map;
import java.util.Set;

class INIConfigurationSectionClass implements INIConfigurationSection {
	private Map<String, Object> map;
	private Map<String, String> stringMap;
	private INIConfiguration ini;
	private String fullNode;

	INIConfigurationSectionClass(INIConfiguration ini, Map<String, Object> data, Map<String, String> stringMap,
			String fullNode) {
		if (ini == null) {
			try {
				throw new INIException("INIConfiguration instance given was null!", null);
			} catch (INIException e) {
				e.printStackTrace();
			}
		}
		this.ini = ini;
		if (data == null) {
			try {
				throw new INIException("Data  given was null!", ini);
			} catch (INIException e) {
				e.printStackTrace();
			}
		}
		this.map = data;

		if (stringMap == null) {
			try {
				throw new INIException("Data given was null!", ini);
			} catch (INIException e) {
				e.printStackTrace();
			}
		}
		this.stringMap = stringMap;
		if (fullNode == null) {
			try {
				throw new INIException("Node given was null!", ini);
			} catch (INIException e) {
				e.printStackTrace();
			}
		}
		this.fullNode = fullNode;
	}

	@Override
	public Object getObject(String node, Object def) {
		return map.get(node) == null ? def : map.get(node);
	}

	@Override
	public void set(String node, Object val) {
		if (!stringMap.containsKey(node)) {
			try {
				throw new INIException("The section " + node + " does not exist!", this.ini);
			} catch (INIException e) {
				e.printStackTrace();
			}
		}
		if (val == null) {
			stringMap.remove(node);
			map.remove(node);
		}
		return;
	}

	@Override
	public Set<String> getKeys() {
		return getKeys(false);
	}

	@Override
	public Map<String, Object> getValues() {
		return getValues(false);
	}

	@Override
	public String getName() {
		return this.fullNode;
	}

	@Override
	public INIConfigurationSection getConfigurationSection(String node) {
		return null;
	}

	@Override
	public INIConfigurationSection createConfigurationSection(String node) {
		return null;
	}

	@Override
	public INIConfigurationSection removeConfigurationSection(String node) {
		return null;
	}

	@Override
	public Set<String> getKeys(boolean deep) {
		return this.map.keySet();
	}

	@Override
	public Map<String, Object> getValues(boolean deep) {
		return this.map;
	}

	INIConfiguration getINIConfiguration() {
		return ini;
	}

	public INIConfiguration getRoot() {
		return getINIConfiguration();
	}

}
