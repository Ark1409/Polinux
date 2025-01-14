package org.polinux.commands;

import java.util.ArrayList;
import java.util.List;

import org.polinux.exceptions.polinux.commands.PolinuxCommandRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServer;
import org.polinux.https.polinux.server.PolinuxHttpsServer;
import org.polinux.logging.Logger;
import org.polinux.utils.collections.CollectionUtils;

public abstract class PolinuxCommand {
	static final String COMMAND_PREFIX = "";
	static final String UNKNOWN_COMMAND = "Polinux Command API <|> Command does not exist. Type ? for the list of commands";
	static final String ARGUMENT_SPLITTER = " ";

	protected String name;
	protected String description;
	protected String usage;

	protected static final Logger COMMAND_LOGGER = new Logger("PolinuxCommandLogger", System.out);

	protected List<String> aliases = new ArrayList<String>();

	public PolinuxCommand(String name) throws PolinuxCommandRuntimeException {
		this(name, "", COMMAND_PREFIX + name, new String[] {});
	}

	public PolinuxCommand(String name, String description) throws PolinuxCommandRuntimeException {
		this(name, description, COMMAND_PREFIX + name, new String[] {});
	}

	public PolinuxCommand(String name, String description, String usage) throws PolinuxCommandRuntimeException {
		this(name, description, usage, new String[] {});
	}

	public PolinuxCommand(String name, String description, String usage, String... aliases)
			throws PolinuxCommandRuntimeException {
		this(name, description, usage, CollectionUtils.toList(aliases));
	}

	public PolinuxCommand(String name, String description, String usage, List<String> aliases)
			throws PolinuxCommandRuntimeException {
		if (name == null) {
			throw new PolinuxCommandRuntimeException("Cannot create command with null name");
		}
		this.name = name;
		this.description = description == null ? "" : description;
		this.usage = usage == null ? COMMAND_PREFIX + this.name : usage;
		this.aliases = aliases == null ? new ArrayList<String>() : aliases;

	}

	/**
	 * Retrieves the name of the command.
	 * 
	 * @return The command's current name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the description of the command.
	 * 
	 * @return The command's current description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the command.
	 * 
	 * @param description The new command description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Retrieves the usage of the command.
	 * 
	 * @return The command's current usage.
	 */
	public String getUsage() {
		return usage;
	}

	/**
	 * Sets the usage of the command.
	 * 
	 * @param usage The new command usage.
	 */
	public void setUsage(String usage) {
		this.usage = usage;
	}

	/**
	 * Retrieves the aliases for the command.
	 * 
	 * @return The command's currently known aliases.
	 */
	public List<String> getAliases() {
		return aliases;
	}

	/**
	 * Sets the aliases of the command.
	 * 
	 * @param aliases The new command aliases.
	 */
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * Executes the current command.
	 * 
	 * @param server The {@link PolinuxHttpServer} that this command was ran on. May
	 *               also be an {@link PolinuxHttpsServer}.
	 * @param args   The command arguments.
	 */
	public abstract void run(PolinuxHttpServer server, String[] args);

}
