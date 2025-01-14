package org.polinux.commands.defaults;

import org.polinux.PolinuxLauncher;
import org.polinux.commands.PolinuxCommand;
import org.polinux.exceptions.polinux.commands.PolinuxCommandRuntimeException;
import org.polinux.http.polinux.server.PolinuxHttpServer;

public class PolinuxGUICommand extends PolinuxCommand {

	public PolinuxGUICommand() throws PolinuxCommandRuntimeException {
		super("gui", "Opens the GUI", "gui", new String[] { "window", "ui" });
	}

	@Override
	public void run(PolinuxHttpServer server, String[] args) {
		PolinuxLauncher.getInstance().openNewGUI();
	}

}
