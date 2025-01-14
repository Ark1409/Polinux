package org.polinux.configuration.serialization.yaml;

import java.util.Map;

import org.polinux.configuration.serialization.ConfigurationSerializable;

/**
 * Serializability of a object in a
 * {@link org.polinux.configuration.yaml.YamlConfiguration YamlConfiguration} is
 * enabled by implementing the this interface. <br>
 * <br>
 * By implementing this method, you are forced to implement the abstract method
 * {@link #serialize() serialize()}. That method is called whenever saving an
 * object into the YamlConfiguration. The YamlConfiguration creates a
 * {@link org.polinux.configuration.yaml.YamlConfigurationSection
 * YamlConfigurationSection} for this YamlConfigurationSerializable object, then
 * stores what ever the {@link #serialize() serialize()} method returns. Here's
 * an example of a class serialization:
 * 
 * <pre>
 * private class MyClass implements YamlConfigurationSerializable {
 *    public String className;
 * 
 *    public MyClass(String className) {
 *       this.className = className;
 *    }
 *      
 *    {@literal @Override}
 *    public {@literal Map<String, Object>} serialize() {
 *       {@literal Map<String, Object>} data = new {@literal LinkedHashMap<String, Object>}();
 *       data.put("name", className);
 *       return data;
 *    }
 *      
 * }
 * </pre>
 * 
 * Most classes in the YamlConfiguration that have been serialized will start
 * with {@link #CONFIG_SERIALIZATION_PREFIX
 * YamlConfigurationSerializable.CONFIG_SERIALIZATION_PREFIX}. So, if you change
 * the prefix in the YamlConfiguration, the SerializationAPI will not load the
 * serialized object, and will instead handle it as a normal map (or
 * {@link org.polinux.configuration.ConfigurationSection ConfigurationSection}).
 * <br>
 * <br>
 * Furthermore, the class that implements this interface must include the
 * underlying <i>static</i> method
 * <i>{@literal deserialize(Map<String, Object> data)}</i>, which returns an
 * instance of the class from the data given. Here is an example of a class with
 * the deserialize method:
 * 
 * <pre>
 * private class MyClass implements YamlConfigurationSerializable {
 *    public String className;
 * 
 *    public MyClass(String className) {
 *       this.className = className;
 *    }
 *           
 *    {@literal @Override}
 *    public {@literal Map<String, Object>} serialize() {
 *       {@literal Map<String, Object>} data = new {@literal LinkedHashMap<String, Object>}();
 *       data.put("name", className);
 *       return data;
 *    }
 *      
 *    public static MyClass deserialize({@literal Map<String, Object>} data) {
 *       return new MyClass(data.get("name"));
 *    }
 *      	
 * }
 * </pre>
 * 
 * The final step you must complete is to add the specified class to the
 * ConfigurationSerialization API. To do so, you must access the method
 * {@link org.polinux.configuration.serialization.ConfigurationSerialization#addClass(Class)
 * ConfigurationSerialization.addClass(Class)}. It would be preferable to add
 * the specified class when the class is getting loaded/initialized. Here's how
 * it would go:
 * 
 * <pre>
 * static {
 *    {@link org.polinux.configuration.yaml.YamlConfiguration#getYamlConfigurationSerializationAPI() YamlConfiguration.getYamlConfigurationSerializationAPI()}{@link org.polinux.configuration.serialization.yaml.YamlConfigurationSerialization#addClass(Class) .addClass(MyClass.class)};
 * }
 * </pre>
 * 
 * After that, you should be able to get and set that object or class into a
 * YamlConfiguration and it should load properly. Also note that if you do not
 * use the
 * {@link org.polinux.configuration.yaml.YamlConfiguration#getObject(String)
 * YamlConfiguration.getObject(String)} method or the
 * {@link org.polinux.configuration.yaml.YamlConfiguration#getObject(String, Object)
 * YamlConfiguration.getObject(String, Object)} method, the YamlConfiguration
 * will not deserialize the specified class. Anyway, here's an example of a
 * plugin that would include the YamlConfigurationSerializable API:
 * 
 * <pre>
 * public class MyMainClass {
 * 
 *      static {
 *         YamlConfiguration.getYamlConfigurationSerializationAPI().addClass(MyClass.class);
 *      }
 *      
 *      private static MyClass myClass;
 *          
 *      public static void main(String[] args) {
 *         myClass = new MyClass("Test Class Name");
 *         File f = new File("files/yamlfile.yml"); // Assuming a file at the directory exists
 *         YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
 *         config.set("section.key", myClass);
 *         config.save();
 *         // Loading to make sure it doesn't just store it in the map, 
 *         // that it is actually loading it from the file.
 *         config = YamlConfiguration.loadConfiguration(f);
 *         System.out.println(((MyClass)config.get("section.key")).className);
 *      }
 *      
 *      private class MyClass implements YamlConfigurationSerializable {
 *         public String className;
 * 
 *         public MyClass(String className) {
 *            this.className = className;
 *         }
 *           <br>
 *         {@literal @Override}
 *         public {@literal Map<String, Object>} serialize() {
 *            {@literal Map<String, Object>} data = new {@literal LinkedHashMap<String, Object>}();
 *            data.put("name", className);
 *            return data;
 *         }
 *
 *         public static MyClass deserialize({@literal Map<String, Object>} data) {
 *            return new MyClass(data.get("name"));
 *         }
 *      }
 * }
 * </pre>
 * 
 * The code above prints to the console: <i>Test Class Name</i>
 * 
 *
 */
@SuppressWarnings("deprecation")
public interface YamlConfigurationSerializable extends ConfigurationSerializable {

	/**
	 * The prefix used for creating ConfigurationSections that are meant to
	 * represent a serialized object or class.
	 */
	public static final String CONFIG_SERIALIZATION_PREFIX = "==()!";

	/**
	 * Method used to serialize the specified class that implements
	 * {@link YamlConfigurationSerializable YamlConfigurationSerializable}. The Map
	 * returned by this method will be the same map in the paramater of the
	 * {@literal deserialize(Map<String,Object> data)} method. This Map is put
	 * inside the node specified as a
	 * {@link org.polinux.configuration.yaml.YamlConfigurationSection
	 * YamlConfigurationSection} when
	 * {@link org.polinux.configuration.yaml.YamlConfigurationSection#set(String, Object)
	 * setting} this object or class anywhere in the
	 * {@link org.polinux.configuration.yaml.YamlConfiguration YamlConfiguration}.
	 * 
	 * @return The class to be serialized, as a Map (sections).
	 */
	@Override
	public Map<String, Object> serialize();

}
