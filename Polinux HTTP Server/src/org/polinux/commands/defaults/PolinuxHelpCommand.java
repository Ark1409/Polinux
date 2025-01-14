package org.polinux.commands.defaults;

import java.util.Arrays;

import org.polinux.commands.PolinuxCommand;
import org.polinux.exceptions.polinux.commands.PolinuxCommandRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServer;

public class PolinuxHelpCommand extends PolinuxCommand {

	public PolinuxHelpCommand() throws PolinuxCommandRuntimeException {
		super("help", "The help command. Retrieves the server's help page or displays help for a specified command.",
				"help [page|command]", new String[] { "?" });
	}

	@Override
	public void run(PolinuxHttpServer server, String[] args) {
		System.out.println("This is the default help page");
		System.out.println("------- Help | Page 0/0 -------");
		System.out.println(
				"help: The help command. Retrieves the server's help page or displays help for a specified command.");
		System.out.println();
		System.out.println("Your args: " + Arrays.toString(args));
		
		System.out.println("Your Server: " + server.getName());
		
	}
}
