package org.polinux.configuration.yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.polinux.configuration.Configuration;
import org.polinux.configuration.serialization.yaml.YamlConfigurationSerializable;
import org.polinux.configuration.serialization.yaml.YamlConfigurationSerializableAs;
import org.polinux.configuration.serialization.yaml.YamlConfigurationSerialization;
import org.yaml.snakeyaml.Yaml;

/**
 * 
 * This class is used to make a
 * {@link org.polinux.configuration.yaml.YamlConfigurationSection
 * YamlConfigurationSection} file.
 * 
 * @see org.polinux.configuration.Configuration Configuration
 * @see org.polinux.configuration.ConfigurationSection ConfigurationSection
 * @see org.polinux.configuration.yaml.YamlConfigurationSection
 *      YamlConfigurationSection
 */
public class YamlConfiguration extends Configuration implements YamlConfigurationSection {
	/**
	 * Represents the data map
	 */
	protected Map<String, Object> map;

	/**
	 * {@link org.yaml.snakeyaml} utilities
	 */
	protected org.yaml.snakeyaml.Yaml y;

	/**
	 * Path separator (sub-section separator)
	 */
	public static final String seperator = ".";

	/**
	 * Path separator, as a {@code char} (sub-section separator)
	 */
	public static final char seperatorChar = seperator.charAt(0);

	/**
	 * Path separator, in its {@code regex} form (sub-section separator)
	 */
	public static final String seperatorRegex = "\\" + seperator;

	/**
	 * Name of the Default {@link YamlConfiguration}
	 * {@link YamlConfigurationSerialization serialization api}
	 */
	protected static final String API_NAME = "YamlConfigurationAPI-v1.1";

	/**
	 * Represents the {@link org.yaml.snakeyaml.DumperOptions.FlowStyle}
	 */
	protected org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle;

	/**
	 * Default {@link org.yaml.snakeyaml.DumperOptions.FlowStyle}
	 * 
	 * @see #flowStyle
	 */
	protected static final org.yaml.snakeyaml.DumperOptions.FlowStyle DEFAULT_FLOW = org.yaml.snakeyaml.DumperOptions.FlowStyle.BLOCK;

	/**
	 * {@link YamlConfigurationSerialization YamlConfiguration Serialization API}
	 */
	protected static final YamlConfigurationSerialization yamlSerializationAPI = YamlConfigurationSerialization
			.getYamlConfigurationSerializationAPI(API_NAME);

	public YamlConfiguration(String path) {
		this(path, DEFAULT_FLOW);
	}

	public YamlConfiguration(String path, org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) {
		this(new File(path), flowStyle);
	}

	public YamlConfiguration(File f) {
		this(f, DEFAULT_FLOW);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public YamlConfiguration(File f, InputStream in) {
		super(f);
		boolean found = true;
		if (!this.f.exists()) {
			found = false;
		}

		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(DEFAULT_FLOW);
		o.setPrettyFlow(true);
		this.y = new org.yaml.snakeyaml.Yaml(o);
		if (!found) {
			try {
				InputStreamReader r = new InputStreamReader(in);
				this.map = (Map<String, Object>) this.y.load(r);
				r.close();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				this.map = new HashMap<String, Object>();
			}
			return;
		}
		try {
			InputStreamReader r = new InputStreamReader(in);
			this.map = (Map<String, Object>) this.y.load(r);
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
			this.map = new HashMap<String, Object>();
			return;
		}

		if (this.map == null) {
			this.map = new HashMap<String, Object>();
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public YamlConfiguration(File f, org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) {
		super(f);
		boolean found = true;
		if (!this.f.exists()) {
			found = false;
		}
		this.flowStyle = flowStyle;
		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(this.flowStyle);
		o.setPrettyFlow(true);
		this.y = new org.yaml.snakeyaml.Yaml(o);
		if (!found) {
			this.map = new HashMap<String, Object>();
			return;
		}
		try {
			FileReader r = new FileReader(this.f);
			this.map = (Map<String, Object>) this.y.load(r);
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
			this.map = new HashMap<String, Object>();
			return;
		}

		if (this.map == null) {
			this.map = new HashMap<String, Object>();
			return;
		}

	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public YamlConfiguration(InputStream in) {
		super((File) null);
		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(DEFAULT_FLOW);
		o.setPrettyFlow(true);
		Yaml y = new Yaml(o);
		this.map = (Map<String, Object>) y.load(in);
	}

	/**
	 * @deprecated You will be unable to use stuff like save, clearFile, etc (since
	 *             this file is null). Use this constructor if you want data
	 *             management and not file exporting.
	 * @param map (Map, data)
	 */
	@Deprecated
	public YamlConfiguration(Map<String, Object> map) {
		super((File) null);
		this.map = map;
	}

	/**
	 * @deprecated The instance of this class will be ensure if the data from the
	 *             file lines up with the data from the map. If it doesn't it will
	 *             clear the file and put in the data that is in the map anyways.
	 * @param f   (File)
	 * @param map (Map, data)
	 */
	@Deprecated
	public YamlConfiguration(String path, Map<String, Object> data) {
		this(new File(path), data);
	}

	/**
	 * @deprecated The instance of this class will be ensure if the data from the
	 *             file lines up with the data from the map. If it doesn't it will
	 *             clear the file and put in the data that is in the map anyways.
	 * @param f    (File)
	 * @param data (Map, data)
	 */
	@Deprecated
	public YamlConfiguration(File f, Map<String, Object> data) {
		super(f);
		this.map = data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(String node, Object def) {
		try {
			Object o = getNoCreating(node, this.deserializeAll(), node);

//			if (o instanceof Map) {
//				for (Class<? extends YamlConfigurationSerializable> c1 : getSerializationAPI().getClasses()) {
//					try {
//						for (Entry<String, Object> e : ((Map<String, Object>) o).entrySet()) {
//							Class<? extends YamlConfigurationSerializable> c = c1;
//							String keyName = e.getKey().trim();
//							String modName = (YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX + c.getName())
//									.trim();
//							if (modName.equals(keyName)) {
//								try {
//									// ((Map<String,Object>) o).get(keyName);
//									Object newObj = c.getMethod("deserialize", Map.class).invoke(null,
//											((Map<String, Object>) o).get(keyName));
//									return newObj;
//								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
//										| NoSuchMethodException | SecurityException e1) {
//									e1.printStackTrace();
//								}
//							}
//						}
//					} catch (ClassCastException e) {
//						return o;
//					}
//				}
//			}
			return o;
		} catch (YamlConfigurationException e) {
			return def;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(String node, Object val) {
		String[] nodes = node.trim().split(seperatorRegex);
		if (nodes.length == 1) {
			if (val == null) {
				// ((Map<String,Object>)toMap().get(nodes[0])).clear();
				this.map.remove(nodes[0]);
				return;
			}
			if (val instanceof YamlConfigurationSerializable) {
				Map<String, Object> data = ((YamlConfigurationSerializable) val).serialize();
				Map<String, Object> data2 = new LinkedHashMap<String, Object>();
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
			stringSec += nodes[i] + seperator;
		}
		if (stringSec.equalsIgnoreCase(""))
			stringSec = node;
		Map<String, Object> sec = getSectionCreating(stringSec, this.map, stringSec);
		if (val == null) {
			sec.clear();
			return;
		}
		if (val instanceof YamlConfigurationSerializable) {
			Map<String, Object> data = ((YamlConfigurationSerializable) val).serialize();
			Map<String, Object> data2 = new LinkedHashMap<String, Object>();
			if (val.getClass().getAnnotation(YamlConfigurationSerializableAs.class) != null) {
				data2.put(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX
						+ val.getClass().getAnnotation(YamlConfigurationSerializableAs.class).value(), data);

				sec.put(nodes[nodes.length - 1], data2);
				return;
			}
			data2.put(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX + val.getClass().getName(), data);
			sec.put(nodes[nodes.length - 1], data2);
			return;
		}
		sec.put(nodes[nodes.length - 1], val);

	}

//	public static final class SelectiveConstructor extends Constructor {
//		public SelectiveConstructor() {
//			// define a custom way to create a mapping node
//			yamlClassConstructors.put(NodeId.mapping, new MyPersistentObjectConstruct());
//		}
//
//		class MyPersistentObjectConstruct extends Constructor.ConstructMapping {
//			@Override
//			protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
//				Class<?> type = node.getType();
//				if (!ConfigurationSerialization.getClasses().contains(type)) {
//					return super.constructJavaBean2ndStep(node, object);
//				}
//				for (Class<? extends ConfigurationSerializable> e : ConfigurationSerialization.getClasses()) {
//					if (type.equals(e)) {
//						Map<Object, Object> map = constructMapping(node);
//						try {
//							return type.getDeclaredMethod("deserialize", Map.class).invoke(object, map);
//						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
//								| NoSuchMethodException | SecurityException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//				return null;
////				System.out.println(node.getTag());
////				if (type.equals(TestSerialize.class)) {
////					// create a map
////					Map map = constructMapping(node);
////					int id = (int) map.get("id");
////					Method m = null;
////					try {
////						m = type.getDeclaredMethod("deserialize", Map.class);
////					} catch (NoSuchMethodException | SecurityException e) {
////						e.printStackTrace();
////					}
////					try {
////						return m.invoke(object, map);
////					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
////						e.printStackTrace();
////					}
////					return null;
////				} else {
////					// create JavaBean
////					
//			}
//		}

//			@Override
//			public Object construct(Node nnode) {
//				if (nnode.getTag().toString().equals("tag:yaml.org,2002:com.configuration.configuration.main.TestSerialize")) {
//					Construct dateConstructor = yamlConstructors.get(new Tag("tag:yaml.org,2002:com.configuration.configuration.main.TestSerialize"));
//					//TestSerialize date = (TestSerialize) dateConstructor.construct(nnode);
//					return "==";
//				} else {
//					return super.construct(nnode);
//				}
//			}
	// }
	// }

	/**
	 * 
	 * Removes the configuration section from the file (if you
	 * {@link org.polinux.configuration.yaml.YamlConfiguration#save() save()}).
	 * Example: <blockquote>
	 * 
	 * <pre>
	 *         -- Top of File --
	 * 
	 *         section1: <----- Method would return this one. 
	 *           section2: <----- Deleting this one. 
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
	 *         section1: <--- Deleting this would return {@code null}, since it doesn't have any parent sections. 
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
	@SuppressWarnings("unchecked")
	@Override
	public YamlConfigurationSection removeConfigurationSection(String node) {
		String[] nodes = node.trim().split(seperatorRegex);
		if (nodes.length == 1) {
			try {
				((Map<String, Object>) this.map.get(nodes[0])).clear();
			} catch (ClassCastException e) {
				try {
					throw new YamlConfigurationException(
							"Section " + node + " is not a section, so it could not be removed.", this);
				} catch (YamlConfigurationException e1) {
					e1.printStackTrace();
				}
			}
			this.map.remove(nodes[0]);
			return null;
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
			sec = getSectionNoCreating(node, this.deserializeAll(), node);
			secR = getSectionNoCreating(newSec, this.deserializeAll(), newSec);
		} catch (YamlConfigurationException e) {
			e.printStackTrace();
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this);
			} catch (YamlConfigurationException e1) {
				e1.printStackTrace();
			}
		}
		if (sec == null) {
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this);
			} catch (YamlConfigurationException e) {
				e.printStackTrace();

			}
		}
		sec.clear();
		secR.remove(nodes[nodes.length - 1]);
		return getConfigurationSection(newSec);
	}

	/**
	 * Clears the entire file and all its data. Make sure to close all readers or
	 * writers related to this file before you
	 * {@link org.polinux.configuration.yaml.YamlConfiguration#clear() clear()} .
	 * 
	 * @throws IOException
	 */
	public void clear() throws IOException {
		this.y = null;
		if (this.f.exists())
			this.f.delete();
		this.map = new HashMap<String, Object>();
		if (this.map.size() > 0)
			this.map.clear();
		if (!this.f.exists())
			this.f.createNewFile();
		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(flowStyle);
		o.setPrettyFlow(true);
		this.y = new org.yaml.snakeyaml.Yaml(o);
	}

	public boolean save() {
		try {
			if (!this.f.exists()) {
				if (!this.f.getParentFile().exists()) {
					this.f.getParentFile().mkdirs();
				}
				this.f.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.f));
			String dataFromMap = mapChildrenValues(this.map, "");
			String[] array = dataFromMap.split("\n");
			for (int i = 0; i < array.length; i++) {
				bw.write(array[i] + "\n");
			}
			bw.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

//	private Map<String, Object> getDumperMapVersion(Map<String, Object> data) {
//		Map<String, Object> map = new LinkedHashMap<String, Object>();
//		Map<String, Object> desMap = this.deserializeMap(this.map);
//		for (Entry<String, Object> e : desMap.entrySet()) {
//			String firstKey = e.getKey();
//			Object firstValue = e.getValue();
//			Class<?> clazz = yamlSerializationAPI.getClass(firstValue.getClass().getName());
//
//			if (clazz.getAnnotation(YamlConfigurationSerializableAs.class) == null) {
//				map.put(firstKey, new LinkedHashMap<String, Object>());
//				((Map<String, Object>) map.get(firstKey)).put(YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX
//						+ clazz.getAnnotation(YamlConfigurationSerializableAs.class).value(),);
//			}
//
//			if (firstValue instanceof Map) {
//
//			}
//			if (clazz == null) {
//				map.put(firstKey, firstValue);
//			}
//		}
//		return map;
//	}

	public Map<String, Object> toMap() {
		return this.deserializeMap(this.map);
	}

	@Deprecated
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	private Object getNoCreating(String node, Map<String, Object> m, String fullNode)
			throws YamlConfigurationException {
		String[] nodes = node.trim().split(seperatorRegex);
		if (nodes.length == 1) {
			if (m.get(nodes[0]) == null) {
				throw new YamlConfigurationException(nodes[0] + " is not a value in the config!", this);
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
			newNode += nodes[i] + seperator;
		}

		if (m.get(nodes[0]) == null || !(m.get(nodes[0]) instanceof Map)) {
			throw new YamlConfigurationException(nodes[0] + " is not a section!", this);
		}
		return getNoCreating(newNode, this.deserializeMap((Map<String, Object>) m.get(nodes[0])), fullNode);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getSectionCreating(String node, Map<String, Object> m, String fullNode) {
		String[] nodes = node.trim().split(seperatorRegex);
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
			newNode += nodes[i] + seperator;
		}

		if (m.get(nodes[0]) == null) {
			m.put(nodes[0], new HashMap<String, Object>());
		}
		if (!(m.get(nodes[0]) instanceof Map)) {
			// ((Map<String, Object>) m.get(nodes[0])).clear();
			m.put(nodes[0], new HashMap<String, Object>());
		}
		return getSectionCreating(newNode, (Map<String, Object>) m.get(nodes[0]), fullNode);
	}

	@SuppressWarnings({ "unchecked" })
	private Map<String, Object> getSectionNoCreating(String node, Map<String, Object> m, String fullNode)
			throws YamlConfigurationException {
		String[] nodes = node.trim().split(seperatorRegex);

		if (nodes.length == 1) {
			if (m.get(nodes[0]) == null || !(m.get(nodes[0]) instanceof Map)) {
				throw new YamlConfigurationException(nodes[0] + " is not a section!", this);
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
			newNode += nodes[i] + seperator;
		}
		if (m.get(nodes[0]) == null || !(m.get(nodes[0]) instanceof Map)) {
			throw new YamlConfigurationException(nodes[0] + " is not a section!", this);
		}
		return getSectionNoCreating(newNode, (Map<String, Object>) m.get(nodes[0]), fullNode);

	}

	public YamlConfigurationSection getConfigurationSection(String node) {
		Map<String, Object> sec = null;
		try {
			sec = getSectionNoCreating(node, this.deserializeAll(), node);
		} catch (YamlConfigurationException e) {
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this);
			} catch (YamlConfigurationException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		if (sec == null) {
			try {
				throw new YamlConfigurationException("Section " + node + " does not exist", this);
			} catch (YamlConfigurationException e) {
				e.printStackTrace();
			}
			return null;
		}
		return new YamlConfigurationSectionClass(this, sec,
				node.split(seperatorRegex)[node.split(seperatorRegex).length - 1], node);
	}

	@Override
	public boolean isConfigurationSection(String node) {
		return getObject(node) instanceof Map;
	}

	@Override
	public YamlConfigurationSection createConfigurationSection(String node) {
		String[] nodes = node.trim().split(seperatorRegex);
		if (nodes.length == 1) {
			this.map.put(nodes[0], new HashMap<String, Object>());
			return getConfigurationSection(node);
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
			stringSec += nodes[i] + seperator;
		}
		Map<String, Object> sec = getSectionCreating(stringSec, this.deserializeAll(), stringSec);
		sec.put(nodes[nodes.length - 1], new HashMap<String, Object>());
		return getConfigurationSection(node);
	}

	@Override
	public Map<String, Object> getMap() {
		return toMap();
	}

	@Override
	public Map<String, Object> getData() {
		return getMap();
	}

	@Override
	public List<?> getList(String node, List<?> def) {
		if (containsList(node)) {
			return (List<?>) getObject(node);
		}
		return def;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getStringList(String node, List<String> def) {
		if (containsList(node)) {
			return (List<String>) getList(node, def);
		}
		return def;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getIntList(String node, List<Integer> def) {
		if (containsList(node)) {
			return (List<Integer>) getList(node, def);
		}
		return def;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getLongList(String node, List<Long> def) {
		if (containsList(node)) {
			return (List<Long>) getList(node, def);
		}
		return def;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> getDoubleList(String node, List<Double> def) {
		if (containsList(node)) {
			return (List<Double>) getList(node, def);
		}
		return def;
	}

	@Override
	public boolean isList(String node) {
		return getObject(node) instanceof List;
	}

	@Override
	public boolean containsList(String node) {
		return containsObject(node) && isList(node);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getMap(String node, Map def) {
		if (isMap(node)) {
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

	@Override
	public boolean containsConfigurationSection(String node) {
		Map<String, Object> sec = null;
		try {
			sec = getSectionNoCreating(node, this.deserializeAll(), node);
		} catch (YamlConfigurationException e) {
			return false;
		}
		return sec != null;
	}

	@Override
	public String getName() {
		return null;
	}

	public static YamlConfiguration loadConfiguration(String file) {
		return loadConfiguration(file, DEFAULT_FLOW);
	}

	public static YamlConfiguration loadConfiguration(File file) {
		return loadConfiguration(file, DEFAULT_FLOW);
	}

	@Deprecated
	public static YamlConfiguration loadConfiguration(Map<String, Object> m) {
		return new YamlConfiguration(m);
	}

	@Deprecated
	public static YamlConfiguration loadConfiguration(File f, Map<String, Object> m) {
		return new YamlConfiguration(f, m);
	}

	public static YamlConfiguration loadConfiguration(String file,
			org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) {
		return new YamlConfiguration(file, flowStyle);
	}

	public static YamlConfiguration loadConfiguration(File file, org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) {
		return new YamlConfiguration(file, flowStyle);
	}

	// TODO work on this
	@SuppressWarnings("unused")
	@Deprecated
	private static YamlConfiguration loadConfiguration(String path, InputStream i) {
		String outputFile = "C:\\Users\\arnel.DESKTOP-5SD3OPR\\Desktop\\output.yml";
		try {
			Files.copy(i, Paths.get(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(outputFile);
		return new YamlConfiguration(file, i);
	}

	@Override
	public YamlConfigurationSection getParent() {
		return null;
	}

	@Override
	public String getNode() {
		return null;
	}

	@Override
	public Object[] getArray(String node) {
		return getArray(node, new Object[] {});
	}

	@Override
	public Object[] getArray(String node, Object[] def) {
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

	public void saveToFile(String path) throws IOException {
		saveToFile(new File(path));
	}

	public void saveToFile(File f) throws IOException {
		dumpToFile(f);
	}

	public void dumpToFile(String path) throws IOException {
		dumpToFile(new File(path));
	}

	public void dumpToFile(File f) throws IOException {
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f);
		this.y.dump(this.map, fw);
		fw.close();
	}

	public static YamlConfiguration dumpToFile(Map<String, Object> data, String path) throws IOException {
		return dumpToFile(data, new File(path));
	}

	public static YamlConfiguration dumpToFile(Map<String, Object> data, File f) throws IOException {
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();
		}
		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(DEFAULT_FLOW);
		o.setPrettyFlow(true);
		Yaml y1 = new org.yaml.snakeyaml.Yaml(o);
		FileWriter f1 = new FileWriter(f);
		y1.dump(data, f1);
		f1.close();
		return new YamlConfiguration(f);
	}

	public static YamlConfiguration dumpToFile(YamlConfiguration y, String path) throws IOException {
		return dumpToFile(y, new File(path));
	}

	public static YamlConfiguration dumpToFile(YamlConfiguration y, File f) throws IOException {
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();
		}
		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(DEFAULT_FLOW);
		o.setPrettyFlow(true);
		Yaml y1 = new org.yaml.snakeyaml.Yaml(o);
		FileWriter f1 = new FileWriter(f);
		y1.dump(y.map, f1);
		f1.close();
		return y;
	}

	public static YamlConfiguration dumpToFile(Map<String, Object> data, String path,
			org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) throws IOException {
		return dumpToFile(data, new File(path), flowStyle);
	}

	public static YamlConfiguration dumpToFile(Map<String, Object> data, File f,
			org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) throws IOException {
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();
		}
		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(flowStyle);
		o.setPrettyFlow(true);
		Yaml y1 = new org.yaml.snakeyaml.Yaml(o);
		FileWriter f1 = new FileWriter(f);
		y1.dump(data, f1);
		f1.close();
		return new YamlConfiguration(f);
	}

	public static YamlConfiguration dumpToFile(YamlConfiguration y, String path,
			org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) throws IOException {
		return dumpToFile(y, new File(path), flowStyle);
	}

	public static YamlConfiguration dumpToFile(YamlConfiguration y, File f,
			org.yaml.snakeyaml.DumperOptions.FlowStyle flowStyle) throws IOException {
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();
		}
		org.yaml.snakeyaml.DumperOptions o = new org.yaml.snakeyaml.DumperOptions();
		o.setDefaultFlowStyle(flowStyle);
		o.setPrettyFlow(true);
		Yaml y1 = new org.yaml.snakeyaml.Yaml(o);
		FileWriter f1 = new FileWriter(f);
		y1.dump(y.map, f1);
		f1.close();
		return y;
	}

	public org.yaml.snakeyaml.DumperOptions.FlowStyle getFlowStyle() {
		return this.flowStyle;
	}

	public void setFlowStyle(org.yaml.snakeyaml.DumperOptions.FlowStyle f) {
		this.flowStyle = f;
	}

	@Deprecated
	public void setData(Map<String, Object> data) {
		setMap(data);
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

	@Deprecated
	public static YamlConfiguration emptyConfiguration() {
		return emptyConfiguration(null);
	}

	public static YamlConfiguration emptyConfiguration(File f) {
		YamlConfiguration y = new YamlConfiguration(new HashMap<String, Object>());
		y.setFile(f);
		return y;
	}

//
//	protected static void saveResource(InputStream in, File f) throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(in));
//		if (!f.exists()) {
//			if (!f.getParentFile().exists()) {
//				f.getParentFile().mkdirs();
//			}
//			f.createNewFile();
//		}
//		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//		String data = "";
//		String line;
//		while ((line = br.readLine()) != null) {
//			data += line + "\n";
//		}
//		String[] arrayData = data.split("\n");
//		for (int i = 0; i < arrayData.length; i++) {
//			String toWrite = arrayData[i];
//			bw.write(toWrite + "\n");
//		}
//		br.close();
//		bw.close();
//	}
//
//	public void writeWithComments() throws IOException {
//		BufferedReader br = new BufferedReader(new FileReader(this.f));
//
//		String line;
//		StringBuilder data = new StringBuilder();
//		String dataString = "";
//
//		int lines = 0;
//		Map<Integer, String> comments = new HashMap<>();
//
//		Map<Integer, String> writeMap = new HashMap<>();
//
//		while ((line = br.readLine()) != null) {
//
//			if (line.trim().startsWith("#"))
//				comments.put(lines, line.trim());
//			else
//				writeMap.put(lines, line.split(":")[0]);
//
//			data.append(line).append("\n");
//			lines++;
//		}
//		BufferedWriter bw = new BufferedWriter(new FileWriter(this.f));
//		String[] array = data.toString().split("\n");
//		int lineCount = 0;
//
//		for (int i = 0; i < array.length; i++) {
//			String text = array[i];
//			if (text.startsWith("#")) {
//				bw.write(array[i] + "\n");
//				continue;
//			}
////			if (!text.startsWith(" ")) {
////				System.out.println("doesnttt");
////				writeSec(bw, "", this.map, text);
////			} else if (text.contains(": \n")){
////				
////			}
//			if (!text.startsWith(" ") && text.contains(": \n")) {
//				writeSec(bw, "", this.map, text);
//			} else {
//				bw.write(text + "\n");
//			}
//		}
//
//		br.close();
//		bw.close();
//	}
//
//	@SuppressWarnings("unchecked")
//	private int writeSec(BufferedWriter bw, String whiteSpace, Map<String, Object> data, String text)
//			throws IOException {
//		for (Entry<String, Object> e : data.entrySet()) {
//			if (e.getValue() instanceof Map) {
//				bw.write(whiteSpace + e.getKey() + ": \n");
//				return writeSec(bw, "  " + whiteSpace, (Map<String, Object>) e.getValue(), text);
//			} else {
//				bw.write(whiteSpace + e.getKey() + ": " + e.getValue().toString() + "\n");
//			}
//		}
////			bw.write(whiteSpace + firstParent + ": " + e.getValue().toString());
//		return -1;
//	}
//
//	public int getDataLineCount(Map<String, Object> map) {
//		int count = 0;
//		for (Entry<String, Object> e : map.entrySet()) {
//
//			if (e.getValue() instanceof Map) {
//				count++;
//				return getDataLineCount((Map<String, Object>) e.getValue());
//			}
//			count++;
//		}
//		return count;
//	}
//
//	public void saveKitsConfig() throws IOException {
//		String[] header = { "# Test Command\n", "#########################################\n",
//				"#           Comment Header              #\n", "#########################################\n", "#\n",
//				"#" };
//		BufferedWriter bw = new BufferedWriter(new FileWriter(this.f));
//		for (int i = 0; i < header.length; i++) {
//			bw.write(header[i]);
//		}
//		bw.write("\n");
//		String dataFromMap = mapChildrenValues(this.map, "");
//		String[] array = dataFromMap.split("\n");
//		for (int i = 0; i < array.length; i++) {
//			bw.write(array[i] + "\n");
//		}
//		bw.close();
//
//	}
//
	@SuppressWarnings("unchecked")
	private String mapChildrenValues(Map<String, Object> map, String whiteSpace) {
		String dataMap = "";
		for (Entry<String, Object> e : map.entrySet()) {
			if (e.getValue() instanceof Map) {
				dataMap += whiteSpace + e.getKey() + ": \n";
				dataMap += mapChildrenValues((Map<String, Object>) e.getValue(), "  " + whiteSpace);
				continue;
			}
			if (e.getValue() != null) {
				dataMap += whiteSpace + e.getKey() + ": " + e.getValue().toString() + "\n";
			} else {
				dataMap += whiteSpace + e.getKey() + ": \"null\"\n";
			}
		}
		return dataMap;
	}

	@Deprecated
	public static YamlConfiguration loadConfiguration(InputStream in) {
		return new YamlConfiguration(in);
	}

	@Override
	public YamlConfiguration getRoot() {
		return this;
	}

	/**
	 * Returns the YamlConfiguration file as a string. Each line is seperated with
	 * the code: <i>\n</i>. The string basically represents how it would look in a
	 * configuration file. You may also use the method {@link #saveAsString()}.
	 * 
	 * @return The YamlConfiguration file, as a string.
	 */
	@Override
	public String toString() {
		return saveAsString();
	}

	public String saveAsString() {
//		String data = "";
//		try {
//			File temp = new File(this.f.getParentFile(),
//					f.getName().substring(0, f.getName().lastIndexOf(".")) + "_temp.yml");
//
//			temp.createNewFile();
//			this.saveToFile(temp);
//			BufferedReader br = new BufferedReader(new FileReader(temp));
//			String line;
//			while ((line = br.readLine()) != null) {
//				data += line + "\n";
//			}
//			br.close();
//			temp.delete();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return mapChildrenValues(this.map, "");
	}

	/**
	 * Retrieves the
	 * {@link org.polinux.configuration.serialization.ConfigurationSerialization
	 * ConfigurationSerialization} API for all {@link YamlConfiguration
	 * YamlConfigurations}.
	 * 
	 * @return The ConfigurationSerialization API for all YamlConfigurations
	 */

	public static YamlConfigurationSerialization getYamlConfigurationSerializationAPI() {
		return yamlSerializationAPI;
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
					Class<?> clazz = yamlSerializationAPI.getClass(secondKey.substring(
							YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX.length(), secondKey.length()));
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
						Method m = clazz.getDeclaredMethod("deserialize", Map.class);
						final boolean oldAccess = m.isAccessible();
						m.setAccessible(true);
						objClass = m.invoke(null, secondValue);
						m.setAccessible(oldAccess);
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

	@Override
	public Map<String, Object> getValues(boolean deep) {
		return getValues(deep, false);
	}

	@Override
	public Map<String, Object> getValues(boolean deep, boolean convert) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (!deep) {
			if (convert) {
				Set<String> keys = this.getKeys(false);
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
				Set<String> keys = this.getKeys(false);
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
	public Map<String, Object> getValues() {
		return getValues(false, false);
	}
}
