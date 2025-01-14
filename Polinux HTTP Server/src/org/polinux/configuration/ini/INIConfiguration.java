package org.polinux.configuration.ini;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.polinux.configuration.Configuration;
import org.polinux.configuration.yaml.YamlConfiguration;

public class INIConfiguration extends Configuration implements INIConfigurationSection {
	private Map<String, Map<String, Object>> map;
	public static final String seperator = ".";
	public static final char seperatorChar = seperator.charAt(0);
	public static final String seperatorRegex = "\\" + seperator;
	private Map<String, Map<String, String>> stringMap;

	public INIConfiguration(String path) {
		this(new File(path));
	}

	public INIConfiguration(File f) {
		super(f);
		if (!this.f.exists()) {
			if (!this.f.getParentFile().exists())
				this.f.getParentFile().mkdirs();
			try {
				this.f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			stringMap = new HashMap<String, Map<String, String>>();
			this.map = new HashMap<String, Map<String, Object>>();
			return;
		}
		stringMap = new HashMap<String, Map<String, String>>();
		try {
			this.map = load();
			this.stringMap = loadStringData();
		} catch (IOException e) {
			e.printStackTrace();
			this.map = new HashMap<String, Map<String, Object>>();
			stringMap = new HashMap<String, Map<String, String>>();
			return;
		}
	}

	@Deprecated
	public INIConfiguration(File f, Map<String, Map<String, Object>> data) {
		super(f);
		this.map = data;
		this.stringMap = new HashMap<String, Map<String, String>>();
		for (Entry<String, Map<String, Object>> e : this.map.entrySet()) {
			Map<String, String> stringData = new HashMap<String, String>();
			for (Entry<String, Object> e1 : e.getValue().entrySet()) {
				stringData.put(e1.getKey(), e1.getValue().toString());
			}
			stringMap.put(e.getKey(), stringData);
		}
	}

	@Deprecated
	protected INIConfiguration(InputStream in) {
		super((File) null);
		stringMap = new HashMap<String, Map<String, String>>();
		try {
			this.map = loadInputStream(in);
			this.stringMap = loadStringDataInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
			this.map = new HashMap<String, Map<String, Object>>();
			this.stringMap = new HashMap<String, Map<String, String>>();
			return;
		}
	}

	@Deprecated
	public INIConfiguration(Map<String, Map<String, Object>> data) {
		super((File) null);
		this.map = data;
		this.stringMap = new HashMap<String, Map<String, String>>();
		for (Entry<String, Map<String, Object>> e : this.map.entrySet()) {
			Map<String, String> stringData = new HashMap<String, String>();
			for (Entry<String, Object> e1 : e.getValue().entrySet()) {
				stringData.put(e1.getKey(), e1.getValue().toString());
			}
			stringMap.put(e.getKey(), stringData);
		}
	}

	/**
	 * Gets the object from the specified node in the ini configuration. The format
	 * would be: {section.key}, which would return the value.
	 * 
	 * @deprecated It is not recommended to use this method for, for example, if
	 *             there is a value in the configuration that is a double, this
	 *             method does not properly parse it like {@link #getInt(String)},
	 *             {@link #getByte(String)}, {@link #getFloat(String)} etc
	 */
	@Override
	@Deprecated
	public Object getObject(String node, Object def) {
		String[] nodes = node.split(seperatorRegex);
		if (nodes.length == 1) {
			if (!this.map.containsKey(nodes[0]))
				return def;
			return this.map.get(nodes[0]);
		} else {
			if (!this.map.containsKey(nodes[0]))
				return def;
			if (!this.map.get(nodes[0]).containsKey(nodes[1]))
				return def;
			return this.map.get(nodes[0]).get(nodes[1]);
		}
	}

	@Override
	public INIConfigurationSection getConfigurationSection(String node) {
		if (!containsConfigurationSection(node)) {
			try {
				throw new INIException("Section " + node + " does not exist!", this);
			} catch (INIException e) {
				e.printStackTrace();
			}
			return null;
		}
		// TODO Code classzz
		return new INIConfigurationSectionClass(this, map.get(node), stringMap.get(node), node);
	}

	@Override
	public Set<String> getKeys() {
		return getKeys(false);
	}

	@Override
	public Map<String, Object> getValues() {
		return getValues(false);
	}

	@Deprecated
	public Map<String, Map<String, Object>> toMap() {
		return getData();
	}

	@Deprecated
	public Map<String, Map<String, Object>> getData() {
		return this.map;
	}

	@Override
	public String getName() {
		return null;
	}

	private Map<String, Map<String, Object>> load() throws IOException {
		if (this.map == null)
			this.map = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> m = new HashMap<String, Map<String, Object>>();
		BufferedReader r = new BufferedReader(new FileReader(this.f));
		String line;
		String currentSec = "";
		while ((line = r.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			if (line.startsWith("[") && line.endsWith("]")) {
				currentSec = line.substring(1, line.length() - 1);
				m.put(currentSec, new HashMap<String, Object>());
				continue;
			}
			if (!currentSec.equalsIgnoreCase("")) {
				String key = line.split("=")[0];
				String value = line.split("=")[1];
				if (isIntPrivate(value)) {
					m.get(currentSec).put(key, Integer.parseInt(value));
					continue;
				} else if (isDoublePrivate(value)) {
					if (value.endsWith("f")) {
						m.get(currentSec).put(key, Float.parseFloat(value.substring(0, value.length() - 1)));
						continue;
					}
					m.get(currentSec).put(key, Double.parseDouble(value.substring(0, value.length() - 1)));
					continue;
				} else if (isFloatPrivate(value)) {
					m.get(currentSec).put(key, Float.parseFloat(value));
					continue;
				} else if (isLongPrivate(value)) {
					m.get(currentSec).put(key, Long.parseLong(value));
					continue;
				} else if (isBooleanPrivate(value)) {
					m.get(currentSec).put(key, Boolean.parseBoolean(value));
					continue;
				} else if (isBytePrivate(value)) {
					m.get(currentSec).put(key, Byte.parseByte(value));
					continue;
				} else if (isShortPrivate(value)) {
					m.get(currentSec).put(key, Short.parseShort(value));
					continue;
				} else {
					if ((value.startsWith("\"") && value.endsWith("\""))
							|| (value.startsWith("'") && value.endsWith("'"))) {
						m.get(currentSec).put(key, value.substring(1, value.length() - 1));
					} else {
						m.get(currentSec).put(key, value);
					}
					continue;
				}
			}
		}
		r.close();
		return m;
	}

	private Map<String, Map<String, Object>> loadInputStream(InputStream in) throws NumberFormatException, IOException {
		if (this.map == null)
			this.map = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> m = new HashMap<String, Map<String, Object>>();
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line;
		String currentSec = "";
		while ((line = r.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			if (line.startsWith("[") && line.endsWith("]")) {
				currentSec = line.substring(1, line.length() - 1);
				m.put(currentSec, new HashMap<String, Object>());
				continue;
			}
			if (!currentSec.equalsIgnoreCase("")) {
				String key = line.split("=")[0];
				String value = line.split("=")[1];
				if (isIntPrivate(value)) {
					m.get(currentSec).put(key, Integer.parseInt(value));
					continue;
				} else if (isDoublePrivate(value)) {
					if (value.endsWith("f")) {
						m.get(currentSec).put(key, Float.parseFloat(value.substring(0, value.length() - 1)));
						continue;
					}
					continue;
				} else if (isFloatPrivate(value)) {
					m.get(currentSec).put(key, Float.parseFloat(value));
					continue;
				} else if (isLongPrivate(value)) {
					m.get(currentSec).put(key, Long.parseLong(value));
					continue;
				} else if (isBooleanPrivate(value)) {
					m.get(currentSec).put(key, Boolean.parseBoolean(value));
					continue;
				} else if (isBytePrivate(value)) {
					m.get(currentSec).put(key, Byte.parseByte(value));
					continue;
				} else if (isShortPrivate(value)) {
					m.get(currentSec).put(key, Short.parseShort(value));
					continue;
				} else {
					if ((value.startsWith("\"") && value.endsWith("\""))
							|| (value.startsWith("'") && value.endsWith("'"))) {
						m.get(currentSec).put(key, value.substring(1, value.length() - 1));
					} else {
						m.get(currentSec).put(key, value);
					}
					continue;
				}
			}
		}
		r.close();
		return m;
	}

	@SuppressWarnings("resource")
	private Map<String, Map<String, String>> loadStringData() throws IOException {
		if (this.stringMap == null)
			this.stringMap = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> m = new HashMap<String, Map<String, String>>();
		BufferedReader r = new BufferedReader(new FileReader(this.f));
		String line;
		String currentSec = "";
		while ((line = r.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			if (line.startsWith("[") && line.endsWith("]")) {
				currentSec = line.substring(1, line.length() - 1);
				m.put(currentSec, new HashMap<String, String>());
				continue;
			}
			if (!currentSec.equalsIgnoreCase("")) {
				String key = line.split("=")[0];
				String value = line.split("=")[1];
				m.get(currentSec).put(key, value);
			}
		}
		return m;
	}

	private Map<String, Map<String, String>> loadStringDataInputStream(InputStream in) throws IOException {
		if (this.stringMap == null)
			this.stringMap = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> m = new HashMap<String, Map<String, String>>();
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line;
		String currentSec = "";
		while ((line = r.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			if (line.startsWith("[") && line.endsWith("]")) {
				currentSec = line.substring(1, line.length() - 1);
				m.put(currentSec, new HashMap<String, String>());
				continue;
			}
			if (!currentSec.equalsIgnoreCase("")) {
				String key = line.split("=")[0];
				String value = line.split("=")[1];
				m.get(currentSec).put(key, value);
			}
		}
		r.close();
		return m;
	}

	private boolean isIntPrivate(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isDoublePrivate(String s) {
		if (s.endsWith("f")) {
			return isFloatPrivate(s);
		}
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isFloatPrivate(String s) {
		if (s.endsWith("f")) {
			String s1 = s.substring(0, s.length() - 1);
			try {
				Float.parseFloat(s1);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		try {
			Float.parseFloat(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isBooleanPrivate(String s) {
		return s.equals("true") || s.equals("false");
	}

	private boolean isLongPrivate(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isBytePrivate(String s) {
		try {
			Byte.parseByte(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isShortPrivate(String s) {
		try {
			Short.parseShort(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void set(String node, Object val) {
		String[] nodes = node.split(seperatorRegex);
		if (nodes.length == 1) {
			if (!stringMap.containsKey(nodes[0])) {
				try {
					throw new INIException("The section " + node + " does not exist!", this);
				} catch (INIException e) {
					e.printStackTrace();
				}
			}
			if (val == null) {
				stringMap.remove(nodes[0]);
				map.remove(nodes[0]);
			}
			return;
		}
		if (nodes.length == 2) {
			if (!stringMap.containsKey(nodes[0])) {
				try {
					throw new INIException("The section " + node + " does not exist!", this);
				} catch (INIException e) {
					e.printStackTrace();
				}
			}
			if (val == null) {
				if (stringMap.get(nodes[0]).containsKey(nodes[1])) {
					stringMap.get(nodes[0]).remove(nodes[1]);
					map.get(nodes[0]).remove(nodes[1]);
				}
				return;
			}
			stringMap.get(nodes[0]).put(nodes[1], val.toString());
			map.get(nodes[0]).put(nodes[1], val);
			return;
		}
		try {
			throw new INIException("The node " + node + " is invalid!", this);
		} catch (INIException e) {
			e.printStackTrace();
		}
	}

	@Override
	public INIConfigurationSection createConfigurationSection(String node) {
		map.put(node, new HashMap<String, Object>());
		return getConfigurationSection(node);
	}

	@Override
	public INIConfigurationSection removeConfigurationSection(String node) {
		if (containsConfigurationSection(node)) {

		}
		return null;
	}

	public boolean save() {
		return saveToFile(this.f);
	}

	public boolean saveToFile(File f) {
		return dumpToFile(this.map, f);
	}

	public static boolean dumpToFile(Map<String, Map<String, Object>> data, File f) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			for (Entry<String, Map<String, Object>> e : data.entrySet()) {
				w.write("[" + e.getKey() + "]");
				w.write("\n");
				for (Entry<String, Object> e1 : e.getValue().entrySet()) {
					if (e1.getValue() instanceof String) {
						w.write(e1.getKey() + "=\"" + e1.getValue().toString() + "\"");
					} else {
						w.write(e1.getKey() + "=" + e1.getValue().toString());
					}

					w.write("\n");
				}
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean dump(Map<String, Map<String, Object>> data, File f) {
		return dumpToFile(data, f);
	}

	public static boolean save(Map<String, Map<String, Object>> data, File f) {
		return dumpToFile(data, f);
	}

	public static boolean saveToFile(Map<String, Map<String, Object>> data, File f) {
		return dumpToFile(data, f);
	}

	public static boolean save(INIConfiguration ini) {
		return ini.save();
	}

	public static boolean saveToFile(INIConfiguration ini, File f) {
		return dumpToFile(ini.getData(), f);
	}

	public static INIConfiguration loadConfiguration(File f) {
		return new INIConfiguration(f);
	}

	@Deprecated
	public static INIConfiguration loadConfiguration(InputStream in) {
		return new INIConfiguration(in);
	}

	public static INIConfiguration loadConfiguration(String path) {
		return loadConfiguration(new File(path));
	}

	@Deprecated
	public static INIConfiguration loadConfiguration(Map<String, Map<String, Object>> data) {
		return new INIConfiguration(data);
	}

	@Deprecated
	public static INIConfiguration loadConfiguration(Map<String, Map<String, Object>> data, File f) {
		return new INIConfiguration(f, data);
	}

	@Override
	public Set<String> getKeys(boolean deep) {
		if (!deep)
			return this.map.keySet();
		Set<String> s = new HashSet<String>();
		for (Entry<String, Map<String, Object>> e : map.entrySet()) {
			s.add(e.getKey());
			for (Entry<String, Object> e1 : e.getValue().entrySet()) {
				s.add(e.getKey() + YamlConfiguration.seperator + e1.getKey());
			}
		}
		return s;
	}

	@Override
	public Map<String, Object> getValues(boolean deep) {
		if (!deep) {
			Map<String, Object> m = new HashMap<String, Object>();
			for (String s : getKeys(false)) {
				m.put(s, new HashMap<String, Object>());
			}
			return m;
		}
		Map<String, Object> m = new HashMap<String, Object>();
		for (Entry<String, Map<String, Object>> e : map.entrySet()) {
			m.put(e.getKey(), e.getValue());
			for (Entry<String, Object> e1 : e.getValue().entrySet()) {
				m.put(e.getKey() + YamlConfiguration.seperator + e1.getKey(), e1.getValue());
			}
		}
		return m;
	}

	@Override
	public INIConfiguration getRoot() {
		return this;
	}

//	@Override
//	public synchronized List<String> keys(boolean deep) {
//		if (!deep) {
//			List<String> l = new ArrayList<>();
//			for (String s : getKeys()) {
//				l.add(s);
//			}
//			return l;
//		}
//		List<String> l = new ArrayList<>();
//		for (Entry<String, Map<String, Object>> e : getData().entrySet()) {
//			l.add(e.getKey());
//			for (Entry<String, Object> e1 : e.getValue().entrySet()) {
//				l.add(e1.getKey());
//			}
//		}
//		return l;
//	}
//
//	@Override
//	public synchronized List<Map<String, Object>> get(boolean deep) {
//		if (!deep) {
//			List<Map<String, Object>> l = new ArrayList<>();
//			l.add(getValues());
//			return l;
//		}
//		List<Map<String, Object>> l = new ArrayList<>();
//		int i = 0;
//		for (Entry<String, Map<String, Object>> e : getData().entrySet()) {
//			l.add(getData().get(e.getKey()));
//			for (Entry<String, Object> e1 : e.getValue().entrySet()) {
//				l.get(i).put(e1.getKey(), e1.getValue());
//			}
//			i++;
//		}
//		return l;
//	}

}
