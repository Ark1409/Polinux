package org.polinux.http.polinux.server;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.polinux.web.PolinuxWebApplication;

/**
 * ClassLoader for loading libraries inside a {@link PolinuxWebApplication}.
 */
class PolinuxLibClassLoader extends ClassLoader {
	// Memory of byte code
	protected final Map<String, byte[]> memClass = new HashMap<String, byte[]>();

	// Plugin file
	protected final File pluginFile;

	/* Package constructors */
	PolinuxLibClassLoader(File pluginFile) {
		this(pluginFile, ClassLoader.getSystemClassLoader());
	}

	PolinuxLibClassLoader(File pluginFile, ClassLoader parent) {
		super(parent);
		this.pluginFile = pluginFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] bytes = null;

		// Check if the class (fully qualified class name) is inside the memory
		final boolean exist = memClass.containsKey(name);

		// If the class is inside the memory, defineClass with the memory bytes
		if (exist) {
			bytes = memClass.get(name);
			return defineClass(name, bytes, 0, bytes.length);
		}

		// If the class is inside the lib memory, load the class from the jar file
		try {
			bytes = loadClassLib(name);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If the no lib classes could be loaded
		if (bytes == null)
			throw new ClassNotFoundException("Unable to find class in polinux lib loader; " + name);

		// If lib class was found, add it to memory
		memClass.put(name, bytes);

		// Define & Return the lib class
		return defineClass(name, bytes, 0, bytes.length);

	}

	/**
	 * Loads a library class from a web application.
	 * 
	 * @param name The fully qualified name if the class.
	 * @return The library class' byte code.
	 * @throws IOException If an I/O error occurs.
	 */
	private byte[] loadClassLib(String name) throws IOException {
		// Initialize the lib class content
		// Will be used to return byte code later
		String classContent = null;

		// Transform file into JarFile so we can read its entries
		final JarFile jar = new JarFile(this.pluginFile);

		// Find the entries inside the jar file
		final Enumeration<JarEntry> entries = jar.entries();

		// Loop through the entries
		while (entries.hasMoreElements()) {
			if (classContent != null)
				break;

			JarEntry jarEntry = entries.nextElement();

			final String jarEntryName = jarEntry.getName().trim();

			// Only look for the class if it is inside the lib folder of the web app & if
			// its a jar file, which we can just assume is a library
			if (jarEntryName.startsWith("lib/") && jarEntryName.endsWith(".jar")) {
				// Transform the jar file, which is the current entry, into a JarInputStream
				final JarInputStream in = new JarInputStream(jar.getInputStream(jarEntry));
				JarEntry libEntry;

				// Loop through the libraries entries now
				while ((libEntry = in.getNextJarEntry()) != null) {
					if (classContent != null)
						break;
					final String libEntryName = libEntry.getName().trim();
					// JarInputStream in = new JarInputStream()

					//
					//
					//
					//
					//
					//
					//
					//
					//
					// This class does not load sub-libs
					//
					//
					//
					//
					//
					//
					//
					//

//					if (libEntryName.startsWith("lib/") && libEntryName.endsWith(".jar")) {
//						String fileContent = "";
//						List<Byte> byteList = new LinkedList<Byte>();
//						byte[] buffer = new byte[2048 * 4];
//
//						int len;
//
//						while ((len = (in.read(buffer))) > 0) {
//							byte[] bytes = new byte[len];
//
//							for (int i = 0; i < len; i++) {
//								bytes[i] = buffer[i];
//							}
//
//							byteList.addAll(toByteList(bytes));
//
//							fileContent += new String(buffer, 0, len);
//						}
//
//						final byte[] finalBytes = toByteArray(byteList);
//
//						JarInputStream subIn = new JarInputStream(new SubJarInputStream(finalBytes));
//
//						byte[] found = loadClassFromSubLib(name, subIn);
//
//						if (found != null)
//							return found;
//
//					}

					// Continue further if the current entry file ends with a .class extension
					if (libEntryName.endsWith(".class")) {

						final String libEntryNameWithDots = libEntryName.replace("/", ".");

						// Checks if the fully qualified class name has the same name & package as the
						// current entry
						if ((name + ".class").equals(libEntryNameWithDots) || name.equals(libEntryNameWithDots
								.substring(0, libEntryNameWithDots.length() - (".class").length()))) {
							// Read content of file if they match
							String fileContent = "";
							byte[] buffer = new byte[2048 * 4];

							int len;

							while ((len = (in.read(buffer))) > 0) {
								fileContent += new String(buffer, 0, len);
							}

							classContent = fileContent;
						}
					}
				}
				// Close the InputStream used to read classes inside the library when finished
				in.close();
			}
		}

		// Close the jar file when finished
		jar.close();

		// If no class content was found, return null, not an empty byte[]
		if (classContent == null)
			return null;

		// Return the bytes from class content, the string equal to the byte code
		return classContent.getBytes();
	}

}
