package org.polinux.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.polinux.http.polinux.server.PolinuxHttpServer;

public class PolinuxCommandManager {
	protected List<PolinuxCommand> commands = new ArrayList<PolinuxCommand>();
	protected Thread thread;
	protected boolean running = false;
	private PolinuxCommandThread currentCommandThread;
	protected PolinuxHttpServer server;

	public PolinuxCommandManager(PolinuxHttpServer server) {
		this.server = server;
	}

	public boolean addCommand(PolinuxCommand cmd) {
		if (cmd == null)
			return false;
		if (commands.contains(cmd))
			return false;
		return commands.add(cmd);
	}

	public boolean removeCommand(PolinuxCommand cmd) {
		if (cmd == null)
			return false;
		if (!commands.contains(cmd))
			return false;
		return commands.remove(cmd);
	}

	public boolean removeCommand(String name) {
		return removeCommand(getCommand(name));
	}

	public PolinuxCommand getCommand(String name) {
		if (name == null)
			return null;
		for (int i = 0; i < commands.size(); i++) {
			PolinuxCommand cmd = commands.get(i);

			if (cmd.name.equalsIgnoreCase(name))
				return cmd;
		}
		return null;
	}

	public boolean containsCommand(PolinuxCommand cmd) {
		if (cmd == null)
			return false;
		return containsCommand(cmd.name);
	}

	public boolean containsCommand(String name) {
		if (name == null)
			return false;
		return getCommand(name) != null;
	}

	public List<PolinuxCommand> getCommands() {
		return this.commands;
	}
	
	public PolinuxHttpServer getServer() {
		return this.server;
	}

	public void execute() {
		if (running)
			return;
		this.currentCommandThread = new PolinuxCommandThread(this, System.in, System.out);
		thread = new Thread(this.currentCommandThread);
		running = true;
		thread.start();
	}

	public void start() {
		execute();
	}

	public void stop() {
		thread.stop();
		running = false;
	}

	public void join() throws InterruptedException {
		stop();
		thread.join();
	}

	PolinuxCommand getCommandWithNameOrAli(String text) {
		if (text == null)
			return null;
		PolinuxCommand cmd = getCommand(text);

		if (cmd != null)
			return cmd;

		for (int i = 0; i < commands.size(); i++) {
			PolinuxCommand cmd1 = commands.get(i);

			for (int i1 = 0; i1 < cmd1.aliases.size(); i1++) {
				if (cmd1.aliases.get(i1).equalsIgnoreCase(text)) {
					return cmd1;
				}
			}
		}

		return null;

	}

	private static final class PolinuxCommandThread implements Runnable {
		protected PolinuxCommandManager manager;
		private InputStream in;
		private OutputStream out;

		PolinuxCommandThread(PolinuxCommandManager manager, InputStream in, OutputStream out) {
			this.manager = manager;
			this.in = in;
			this.out = out;
		}

		@Override
		public void run() {
			try {
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				final PrintStream writer = new PrintStream(out);

//				writer.println("+--------------------------+");
//				writer.flush();
//				writer.println("| Polinux Commands Enabled |");
//				writer.flush();
//				writer.println("|                          |");
//				writer.flush();
//				writer.println("|        !Warning!         |");
//				writer.flush();
//				writer.println("|                          |");
//				writer.flush();
//				writer.println("| Command Outputs may mix  |");
//				writer.flush();
//				writer.println("|   with HTTP/HTTPS Logs   |");
//				writer.flush();
//				writer.println("|                          |");
//				writer.flush();
//				writer.println("|        !Warning!         |");
//				writer.flush();
//				writer.println("+--------------------------+");
//				writer.flush();

				while (true) {

					final String fullCommandLine = reader.readLine();

					if (!fullCommandLine.startsWith(PolinuxCommand.COMMAND_PREFIX)) {
						writer.println(PolinuxCommand.UNKNOWN_COMMAND);
						continue;
					}

					final String fullCommandLineNoPrefix = fullCommandLine
							.substring(PolinuxCommand.COMMAND_PREFIX.length(), fullCommandLine.length());

					final String[] fullCommandLineNoPrefixSplit = fullCommandLineNoPrefix
							.split(Pattern.quote(PolinuxCommand.ARGUMENT_SPLITTER));

					final String commandName = fullCommandLineNoPrefixSplit[0];

					List<String> commandArgs = new LinkedList<String>();

					if (fullCommandLineNoPrefixSplit.length >= 2) {
						for (int i = 1; i < fullCommandLineNoPrefixSplit.length; i++) {
							commandArgs.add(fullCommandLineNoPrefixSplit[i]);
						}
					}

					PolinuxCommand cmd = this.manager.getCommandWithNameOrAli(commandName);

					if (cmd == null) {
						writer.println(PolinuxCommand.UNKNOWN_COMMAND);
						continue;
					}
					
					cmd.run(this.manager.server, commandArgs.toArray(new String[0]));
					writer.println();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
