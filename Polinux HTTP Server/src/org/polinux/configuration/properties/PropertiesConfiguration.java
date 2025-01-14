package org.polinux.configuration.properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.polinux.configuration.Configuration;

/**
 * 
 * This class is used to make a {@code .properties} file.
 * 
 * @see org.polinux.configuration.Configuration Configuration
 * @see org.polinux.configuration.ConfigurationSection ConfigurationSection
 * @see org.polinux.configuration.yaml.YamlConfigurationSection
 *      YamlConfigurationSection
 */

@SuppressWarnings({ "resource", "unused" })
public class PropertiesConfiguration extends Configuration {
	private Map<String, String> map;
	private List<String> header, footer;
	public static final String commentSymbol = "#";
	public static final char commentSymbolChar = commentSymbol.charAt(0);

	public PropertiesConfiguration(String path) {
		this(new File(path));
	}

	public PropertiesConfiguration(File f) {
		super(f);
		this.map = loadDataAsString();
		this.header = loadHeader();
		this.footer = loadFooter();
	}

	@Override
	public Object getObject(String node, Object def) {
		return map.get(node) == null ? def : map.get(node);
	}

	@Override
	public void set(String node, Object val) {
		if (val == null) {
			if (!this.containsObject(node))
				return;
			map.remove(node);
			return;
		}
		map.put(node, String.valueOf(val));
	}

	public void save() throws IOException {
		saveToFile(this.f);
	}

	public void saveToFile(File f) throws IOException {
		File parent = f.getParentFile();
		File child = f;
		if (!parent.exists())
			parent.mkdirs();
		if (!child.exists())
			child.createNewFile();
		FileWriter fw = new FileWriter(f);
		BufferedWriter w = new BufferedWriter(fw);
		for (int i = 0; i < this.header.size(); i++) {
			w.write(PropertiesConfiguration.commentSymbol + this.header.get(i) + "\n");
		}
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (containsKeyFile(f, e.getKey()))
				continue;
			if (e.getValue() instanceof String) {
				w.write(e.getKey() + "=\"" + e.getValue() + "\"" + "\n");
				continue;
			}
			w.write(e.getKey() + "=" + e.getValue() + "\n");
		}
		for (int i = 0; i < this.footer.size(); i++) {
			w.write(PropertiesConfiguration.commentSymbol + this.footer.get(i) + "\n");
		}
		w.close();
		fw.close();
	}

	@Override
	public Set<String> getKeys() {
		return map.keySet();
	}

	/**
	 * @deprecated All the values (Objects) from this method are terribly formated
	 *             and are all String. Use if you know what you are doing.
	 */
	@Override
	@Deprecated
	public Map<String, Object> getValues() {
		Map<String, Object> newMap = new LinkedHashMap<String, Object>();
		newMap.putAll(map);
		return newMap;
	}

	@Override
	public String getName() {
		return this.f.getName();
	}

	public void clearHeader() {
		this.header = new ArrayList<String>();
	}

	public void clearFooter() {
		this.footer = new ArrayList<String>();
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public List<String> getFooter() {
		return footer;
	}

	public void setFooter(List<String> footer) {
		this.footer = footer;
	}

	private static boolean isIntPrivate(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isDoublePrivate(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			try {
				Double.parseDouble(s.substring(0, s.length() - 1));
				return true;
			} catch (NumberFormatException e1) {
				return false;
			}
		}
	}

	private static boolean isLongPrivate(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isBooleanPrivate(String s) {
		return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
	}

	private static boolean isShortPrivate(String s) {
		try {
			Short.parseShort(s);
			return true;
		} catch (NumberFormatException e) {
			return false;

		}
	}

	private static boolean isBytePrivate(String s) {
		try {
			Byte.parseByte(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isFloatPrivate(String s) {
		try {
			Float.parseFloat(s);
			return true;
		} catch (NumberFormatException e) {
			if (!s.toLowerCase().endsWith("f"))
				return false;
			try {
				Float.parseFloat(s.substring(0, s.length() - 1));
				return true;
			} catch (NumberFormatException e1) {
				return false;
			}
		}
	}

	private Map<String, Object> loadParsedData() {
		Map<String, Object> data = new HashMap<String, Object>();
		BufferedReader r = null;
		FileReader fr = null;
		try {
			fr = new FileReader(f);
			r = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			return new HashMap<String, Object>();
		}
		if (r == null || fr == null)
			return new HashMap<String, Object>();
		String line;
		int count = 0;
		try {
			while ((line = r.readLine()) != null) {
				if (line.startsWith(PropertiesConfiguration.commentSymbol)) {
					continue;
				}
				if (!line.contains("="))
					continue;
				String key = line.split("=")[0];
				String valS = line.split("=")[1];
				Object val = null;
				List<Object> o = new ArrayList<Object>();
				if (isIntPrivate(valS)) {
					val = Integer.parseInt(valS);
					o.add(val);
				} else if (isLongPrivate(valS)) {
					val = Long.parseLong(valS);
					o.add(val);
				} else if (isBooleanPrivate(valS)) {
					val = valS.equalsIgnoreCase("true") ? true : false;
					o.add(val);
				}

				else if (isBytePrivate(valS)) {
					val = Byte.parseByte(valS);
					o.add(val);
				}

				else if (isShortPrivate(valS)) {
					val = Short.parseShort(valS);
					o.add(val);

				} else if (valS.startsWith("\"") && valS.endsWith("\"")) {
					val = valS.substring(1, valS.length() - 1);
					o.add(val);
				}

				if (isDoublePrivate(valS) && isFloatPrivate(valS)) {
					val = Double.parseDouble(valS);
					o.add(val);
				}

				if (valS.toLowerCase().endsWith("f") && isFloatPrivate(valS)) {
					val = Float.parseFloat(valS);
					o.add(val);
				}
				if (valS.toLowerCase().endsWith("d") && isFloatPrivate(valS)) {
					val = Double.parseDouble(valS);
					o.add(val);
				}

				for (Object ob : o)
					data.put(key, ob);

				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			r.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private Map<String, String> loadDataAsString() {
		Map<String, String> data = new HashMap<String, String>();
		BufferedReader r = null;
		FileReader fr = null;
		try {
			fr = new FileReader(f);
			r = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			return new HashMap<String, String>();
		}
		if (r == null || fr == null)
			return new HashMap<String, String>();
		String line;
		int count = 0;
		try {
			while ((line = r.readLine()) != null) {
				if (line.startsWith(PropertiesConfiguration.commentSymbol)) {
					continue;
				}
				if (!line.contains("="))
					continue;
				String key = line.split("=")[0];
				String valS = line.split("=")[1];
				data.put(key, valS);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			r.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private List<String> loadHeader() {
		BufferedReader r = null;
		FileReader fr = null;
		try {
			fr = new FileReader(this.f);
			r = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			return new ArrayList<String>();
		}
		if (r == null || fr == null)
			return new ArrayList<String>();
		String line;
		boolean headerFound = false;
		List<String> header = new ArrayList<String>();

		try {
			while ((line = r.readLine()) != null) {
				if (line.startsWith(PropertiesConfiguration.commentSymbol)) {
					headerFound = true;
				} else {
					break;
				}
				if (headerFound) {
					header.add(line.replaceFirst(PropertiesConfiguration.commentSymbol, ""));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			r.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return header;
	}

	private List<String> loadFooter() {
		List<String> footer = new ArrayList<String>();
		BufferedReader r = null;
		FileReader fr = null;
		try {
			fr = new FileReader(this.f);
			r = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			return new ArrayList<String>();
		}
		if (r == null || fr == null)
			return new ArrayList<String>();
		String line;
		List<String> lines = new ArrayList<String>();
		boolean hasFooter = false;
		try {
			while ((line = r.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.reverse(lines);
		for (int i = 0; i < lines.size(); i++) {
			String backLine = lines.get(i);
			if (!backLine.startsWith(PropertiesConfiguration.commentSymbol))
				hasFooter = true;
		}
		if (!hasFooter)
			return new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			String backLine = lines.get(i);
			if (backLine.startsWith(PropertiesConfiguration.commentSymbol)) {
				footer.add(backLine.replaceFirst(PropertiesConfiguration.commentSymbol, ""));
				continue;
			} else {
				break;
			}
		}
		try {
			r.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.reverse(footer);
		return footer;
	}

	public boolean containsHeaderLine(int line) {
		return getHeaderLine(line) != null;
	}

	public boolean containsHeaderLine(String lineCaseSentive) {
		return getHeaderLine(lineCaseSentive) > -1;
	}

	public String getHeaderLine(int line) {
		if (line >= this.header.size() || line <= -1)
			return null;
		return this.header.get(line);
	}

	public int getHeaderLine(String lineCaseSentive) {
		for (int i = 0; i < this.header.size(); i++) {
			if (this.header.get(i).equals(lineCaseSentive)) {
				return i;
			}
		}
		return -1;
	}

	public void addHeader(String s) {
		this.header.add(s);
	}

	public boolean headerContains(String s) {
		return this.header.contains(s);
	}

	public void removeHeader(String s) {
		if (!headerContains(s))
			return;
		this.header.remove(s);
	}

	public void addFooter(String s) {
		this.footer.add(s);
	}

	public boolean footerContains(String s) {
		return this.footer.contains(s);
	}

	public void removeFooter(String s) {
		if (!footerContains(s))
			return;
		this.footer.remove(s);
	}

	public boolean containsKey(String s) {
		return map.containsKey(s);
	}

	private boolean containsKeyFile(File f, String s) {
		FileReader fr = null;
		BufferedReader r = null;
		try {
			fr = new FileReader(f);
			r = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			return false;
		}
		if (fr == null || r == null)
			return false;
		String line;
		try {
			while ((line = r.readLine()) != null) {
				if (line.startsWith(PropertiesConfiguration.commentSymbol))
					continue;
				if (line.equals(s))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getInt(String node) {
		return getInt(node, 0);
	}

	@Override
	public int getInt(String node, int def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isIntPrivate(map.get(node)))
			return def;
		return Integer.parseInt(map.get(node));
	}

	@Override
	public short getShort(String node) {
		return getShort(node, (short) 0);
	}

	@Override
	public short getShort(String node, short def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isShortPrivate(map.get(node)))
			return def;
		return Short.parseShort(map.get(node));
	}

	@Override
	public double getDouble(String node) {
		return getDouble(node, 0D);
	}

	@Override
	public double getDouble(String node, double def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isDoublePrivate(map.get(node)))
			return def;
		return Double.parseDouble(map.get(node));
	}

	@Override
	public long getLong(String node) {
		return getLong(node, 0L);
	}

	@Override
	public long getLong(String node, long def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isLongPrivate(map.get(node)))
			return def;
		return Long.parseLong(map.get(node));
	}

	@Override
	public byte getByte(String node) {
		return getByte(node, (byte) 0);
	}

	@Override
	public byte getByte(String node, byte def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isBytePrivate(map.get(node)))
			return def;
		return Byte.parseByte(map.get(node));
	}

	@Override
	public float getFloat(String node) {
		return getFloat(node, 0f);
	}

	@Override
	public float getFloat(String node, float def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isFloatPrivate(map.get(node)))
			return def;
		return Float.parseFloat(map.get(node));
	}

	@Override
	public boolean getBoolean(String node) {
		return getBoolean(node, false);
	}

	@Override
	public boolean getBoolean(String node, boolean def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isBooleanPrivate(map.get(node)))
			return def;
		return map.get(node).equalsIgnoreCase("true") ? true : false;
	}

	@Override
	public boolean isInt(String node) {
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (e.getKey().equals(node)) {
				if (isIntPrivate(map.get(node)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFloat(String node) {
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (e.getKey().equals(node)) {
				if (isFloatPrivate(map.get(node)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDouble(String node) {
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (e.getKey().equals(node)) {
				if (isDoublePrivate(map.get(node)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean isShort(String node) {
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (e.getKey().equals(node)) {
				if (isShortPrivate(map.get(node)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean isByte(String node) {
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (e.getKey().equals(node)) {
				if (isBytePrivate(map.get(node)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean isLong(String node) {
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (e.getKey().equals(node)) {
				if (isLongPrivate(map.get(node)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBoolean(String node) {
		for (Map.Entry<String, String> e : this.map.entrySet()) {
			if (e.getKey().equals(node)) {
				if (isBooleanPrivate(map.get(node)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean isString(String nodeCaseSensitive) {
		String nodeNoTrim = nodeCaseSensitive;
		String node = nodeNoTrim.trim();
		for (Map.Entry<String, String> e : this.map.entrySet()) {

			if (e.getKey().equals(node)) {
				String s = map.get(node);
				if (!isFloatPrivate(s) & !isBytePrivate(s) && !isDoublePrivate(s) && !isIntPrivate(s)
						&& !isShortPrivate(s) && !isLongPrivate(s))
					if (!s.startsWith("\"") && !s.endsWith("\"") && isBooleanPrivate(s)) {
						return false;
					}
				return true;
			}
		}
		return false;
	}

	@Override
	public String getString(String node) {
		return getString(node, "");
	}

	@Override
	public String getString(String node, String def) {
		if (!this.map.containsKey(node))
			return def;
		if (!isString(node))
			return def;
		String s = map.get(node);
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return s.substring(1, s.length() - 1);
		}
		return s;
	}

}
