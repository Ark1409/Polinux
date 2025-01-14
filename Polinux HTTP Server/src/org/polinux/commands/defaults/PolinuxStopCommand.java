package org.polinux.commands.defaults;

import org.polinux.PolinuxLauncher;
import org.polinux.commands.PolinuxCommand;
import org.polinux.exceptions.polinux.commands.PolinuxCommandRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServer;

public class PolinuxStopCommand extends PolinuxCommand {

	public PolinuxStopCommand() throws PolinuxCommandRuntimeException {
		super("stop", "Stops the server.", "stop [http|https]", new String[] { "end", "abort" });
	}

	@Override
	public void run(PolinuxHttpServer server, String[] args) {
		if (args.length <= 0) {

			if (PolinuxLauncher.getInstance().getHttpServer() != null) {
				PolinuxLauncher.getInstance().getHttpServer().getCommandManager().stop();
				PolinuxLauncher.getInstance().getHttpServer().shutdown();
			}
			if (PolinuxLauncher.getInstance().getHttpsServer() != null) {
				PolinuxLauncher.getInstance().getHttpsServer().getCommandManager().stop();
				PolinuxLauncher.getInstance().getHttpsServer().shutdown();
			}
			COMMAND_LOGGER.log("Disabled Polinux HTTP & HTTPS Server!");

			return;
		}

		if (args.length >= 1) {
			COMMAND_LOGGER.log(this.getUsage());
			return;
		}

		final String serverName = args[0];

		if (serverName.equalsIgnoreCase("http")) {
			if (PolinuxLauncher.getInstance().getHttpServer() == null
					|| !PolinuxLauncher.getInstance().getHttpServer().isEnabled()) {
				COMMAND_LOGGER.log("The Polinux HTTP Server is not enabled!");
				return;
			}
			PolinuxLauncher.getInstance().getHttpServer().shutdown();
			
			if (PolinuxLauncher.getInstance().getHttpsServer() != null
					&& !PolinuxLauncher.getInstance().getHttpsServer().isEnabled()) {
				PolinuxLauncher.getInstance().getHttpServer().getCommandManager().stop();
				PolinuxLauncher.getInstance().getHttpsServer().getCommandManager().stop();
			}
			COMMAND_LOGGER.log("Disabled Polinux HTTP Server!");
			return;
		}

		if (serverName.equalsIgnoreCase("https")) {
			if (PolinuxLauncher.getInstance().getHttpsServer() == null
					|| !PolinuxLauncher.getInstance().getHttpsServer().isEnabled()) {
				COMMAND_LOGGER.log("The Polinux HTTPS Server is not enabled!");
				return;
			}
			PolinuxLauncher.getInstance().getHttpsServer().shutdown();

			if (PolinuxLauncher.getInstance().getHttpServer() != null
					&& !PolinuxLauncher.getInstance().getHttpServer().isEnabled()) {
				PolinuxLauncher.getInstance().getHttpsServer().getCommandManager().stop();
				PolinuxLauncher.getInstance().getHttpServer().getCommandManager().stop();
			}
			COMMAND_LOGGER.log("Disabled Polinux HTTPS Server!");
			return;
		}

		COMMAND_LOGGER.log(this.getUsage());
		return;
	}
}
