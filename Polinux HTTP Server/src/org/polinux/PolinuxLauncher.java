package org.polinux;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.polinux.commands.PolinuxCommandManager;
import org.polinux.commands.defaults.PolinuxGUICommand;
import org.polinux.commands.defaults.PolinuxHelpCommand;
import org.polinux.commands.defaults.PolinuxStopCommand;
import org.polinux.configuration.server.DefaultPolinuxHttpServerConfiguration;
import org.polinux.configuration.server.DefaultPolinuxHttpsServerConfiguration;
import org.polinux.configuration.server.PolinuxHttpServerConfiguration;
import org.polinux.configuration.server.PolinuxHttpsServerConfiguration;
import org.polinux.gui.PolinuxJFrameGUI;
import org.polinux.http.polinux.server.PolinuxHttpServer;
import org.polinux.https.polinux.server.PolinuxHttpsServer;

/**
 * Main class that launches the Polinux HTTP server
 */
public final class PolinuxLauncher {
	protected transient PolinuxCommandManager commandManager;
	private PolinuxHttpServer httpServer;
	private PolinuxHttpsServer httpsServer;
	private PolinuxHttpsServerConfiguration configHttps;
	private PolinuxHttpServerConfiguration configHttp;
	private PolinuxJFrameGUI gui;

	static PolinuxLauncher instance;
	static boolean running = false;

	private void initDefaultCommands(PolinuxCommandManager manager) {
		manager.addCommand(new PolinuxHelpCommand());
		manager.addCommand(new PolinuxStopCommand());
		manager.addCommand(new PolinuxGUICommand());
	}

	public void openNewGUI() {
		initGUI();
	}

	private PolinuxLauncher(String... args) {
		instance = this;

		List<String> arguments;

		if (args != null)
			arguments = Arrays.asList(args);
		else
			arguments = new ArrayList<String>();

		File configFile = new File(PolinuxHttpServerConfiguration.CONFIGURATION_PATH);

		if (!configFile.exists()) {
			PolinuxHttpServerConfiguration.writeDefaultConfig(configFile);
		}

		PolinuxHttpsServerConfiguration configHttps = new PolinuxHttpsServerConfiguration(configFile);

		PolinuxHttpServerConfiguration configHttp = new PolinuxHttpServerConfiguration(configFile);

		this.configHttp = configHttp;
		this.configHttps = configHttps;

		PolinuxHttpServer httpServer = null;
		PolinuxHttpsServer httpsServer = null;

		this.execCommandLineArg(arguments);

		if (this.configHttps.isUseHttp()) {
			httpServer = new PolinuxHttpServer(this.configHttp);
		}

		if (this.configHttps.isUseHttps()) {
			httpsServer = new PolinuxHttpsServer(this.configHttps);
		}

		PolinuxCommandManager manager = null;

		if (httpServer != null) {
			manager = new PolinuxCommandManager(httpServer);
		}

		if (httpsServer != null) {
			manager = new PolinuxCommandManager(httpsServer);
		}

		this.commandManager = manager;

		this.httpServer = httpServer;
		this.httpsServer = httpsServer;

		System.out.println(new File("./").getAbsoluteFile().getAbsolutePath());
		System.out.println(new File("./").getAbsoluteFile().isDirectory());
		System.out.println(Arrays.toString(new File("./").getAbsoluteFile().listFiles()));
		System.out.println(new File("./").getAbsoluteFile().getName());
		System.out.println(new File("./").getAbsoluteFile().getParentFile().getName());

		if (httpServer != null) {
			httpServer.setCommandManager(manager);
			initDefaultCommands(httpServer.getCommandManager());
			httpServer.init();
			httpServer.run();
		}

		if (httpsServer != null) {
			httpsServer.setCommandManager(manager);
			httpsServer.init();
			httpsServer.run();
		}

//		try {
//			PolinuxWebApplicationConfiguration config = new PolinuxWebApplicationConfiguration(
//					new FileInputStream(new File("web/app.yml")));
//			YamlConfiguration config1 = new YamlConfiguration(
//					config.getServletConfigurationSection().getConfigurationSection("HomeServlet").getName());
//			System.out.println(config1.getName());
//
//			StringOutputStream out = new StringOutputStream(true, CharacterSet.UTF_8);
//
//			PrintStream s = new PrintStream(out, true);
//
//			doStuff(s);
//
//			System.out.println("WRITE HERE");
//			System.out.println(out.getContent());
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// HttpRequestCookie r = new PoiHttpRequestCookie("hi", "bye");

//
//		HttpServlet h = new HttpServlet() {
//
//			@Override
//			public HttpServerResponse METHOD(HttpServerRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public HttpServerResponse GET(HttpServerRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public HttpServerResponse HEAD(HttpServerRequest request) {
//				// TODO Auto-generated method stub
//
//				return null;
//			}
//
//			@Override
//			public HttpServerResponse POST(HttpServerRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public HttpServerResponse PUT(HttpServerRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public HttpServerResponse DELETE(HttpServerRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public HttpServerResponse OPTIONS(HttpServerRequest request) {
//				return null;
//			}
//
//			@Override
//			public HttpServerResponse TRACE(HttpServerRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//		};
//
//		h.DELETE(null);
//
//		Date d = new Date();
//		final SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
//
//		format.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//		System.out.println(format.format(d));
////		boolean good= true;
////		for(Character c : "ffdsargtretggrgsdfsdfg".toCharArray()) {
////			System.out.println("C:: " + c);
////			good = c.toString().matches(COOKIE_REGEX);
////			System.out.println("G:: " + good);
////			System.out.println();
////		}
//		System.out.println(COOKIE_VALUE_REGEX);
////		
//		System.out.println("\"health_inspect_id\"".matches(COOKIE_VALUE_REGEX));
//
//		// Logger l = PolinuxHttpServer.getLogger();
//
//		System.out.println(indexesOf("fewgdfgfdseefghtdfytyhfdeefaawwaee", "ee"));
//
//		HttpResponseCookie c = (HttpResponseCookie) new PolinuxHttpRequestCookie("hi", "stop");
//		c.toString();
	}

	private void initGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PolinuxJFrameGUI frame = new PolinuxJFrameGUI();
					frame.setVisible(true);
					gui = frame;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void execCommandLineArg(List<String> arguments) {
		if (arguments != null) {
			for (int i = 0; i < arguments.size(); i++) {
				String arg = arguments.get(i).trim();
				if (arg.toLowerCase().equalsIgnoreCase("-noconfig")) {
					configHttp = new DefaultPolinuxHttpServerConfiguration();
					configHttps = new DefaultPolinuxHttpsServerConfiguration();
				}
			}

			for (int i = 0; i < arguments.size(); i++) {
				String arg = arguments.get(i).trim();

				if (arg.toLowerCase().startsWith("-gui")) {
					openNewGUI();
				}

				if (arg.toLowerCase().startsWith("-host:")) {
					String valueHost;

					try {
						valueHost = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHost = null;
					}

					if (valueHost != null) {
						configHttp.set("web.host", valueHost);
						configHttps.set("web.host", valueHost);
					}
				}

				if (arg.toLowerCase().startsWith("-root:")) {
					String valueRoot;

					try {
						valueRoot = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueRoot = null;
					}

					if (valueRoot != null) {
						configHttp.set("web.root", valueRoot);
						configHttps.set("web.root", valueRoot);
					}
				}

				if (arg.toLowerCase().startsWith("-backlog:")) {
					String valueBacklog;

					try {
						valueBacklog = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueBacklog = null;
					}

					if (valueBacklog != null) {
						try {
							configHttp.set("web.backlog", Integer.parseInt(valueBacklog));
							configHttps.set("web.backlog", Integer.parseInt(valueBacklog));
						} catch (RuntimeException e) {

						}
					}
				}

				if (arg.toLowerCase().startsWith("-http-enabled:")) {
					String valueHttpEnable;

					try {
						valueHttpEnable = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHttpEnable = null;
					}

					if (valueHttpEnable != null) {
						configHttp.set("web.http.enabled", Boolean.parseBoolean(valueHttpEnable));
						configHttps.set("web.http.enabled", Boolean.parseBoolean(valueHttpEnable));
					}
				}

				if (arg.toLowerCase().startsWith("-http-port:")) {
					String valueHttpPort;

					try {
						valueHttpPort = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHttpPort = null;
					}

					if (valueHttpPort != null) {
						try {
							configHttp.set("web.http.port", Integer.parseInt(valueHttpPort));
							configHttps.set("web.http.port", Integer.parseInt(valueHttpPort));
						} catch (RuntimeException e) {

						}
					}
				}

				if (arg.toLowerCase().startsWith("-https-redirect:")) {
					String valueHttpsRedirect;

					try {
						valueHttpsRedirect = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHttpsRedirect = null;
					}

					if (valueHttpsRedirect != null) {
						try {
							configHttp.set("web.http.https-redirect", Integer.parseInt(valueHttpsRedirect));
							configHttps.set("web.http.https-redirect", Integer.parseInt(valueHttpsRedirect));
						} catch (RuntimeException e) {

						}
					}
				}

				if (arg.toLowerCase().startsWith("-https-enabled:")) {
					String valueHttpsEnable;

					try {
						valueHttpsEnable = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHttpsEnable = null;
					}

					if (valueHttpsEnable != null) {
						configHttp.set("web.https.enabled", Boolean.parseBoolean(valueHttpsEnable));
						configHttps.set("web.https.enabled", Boolean.parseBoolean(valueHttpsEnable));
					}
				}

				if (arg.toLowerCase().startsWith("-https-port:")) {
					String valueHttpsPort;

					try {
						valueHttpsPort = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHttpsPort = null;
					}

					if (valueHttpsPort != null) {
						try {
							configHttp.set("web.https.port", Integer.parseInt(valueHttpsPort));
							configHttps.set("web.https.port", Integer.parseInt(valueHttpsPort));
						} catch (RuntimeException e) {

						}
					}
				}

				if (arg.toLowerCase().startsWith("-https-keystore-enabled:")) {
					String valueHttpsKeystoreEnabled;

					try {
						valueHttpsKeystoreEnabled = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHttpsKeystoreEnabled = null;
					}

					if (valueHttpsKeystoreEnabled != null) {
						configHttp.set("web.https.keystore.use-keystore",
								Boolean.parseBoolean(valueHttpsKeystoreEnabled));
						configHttps.set("web.https.keystore.use-keystore",
								Boolean.parseBoolean(valueHttpsKeystoreEnabled));
					}
				}

				if (arg.toLowerCase().startsWith("-https-keystore-file:")) {
					String valueHttpsKeystoreFile;

					try {
						valueHttpsKeystoreFile = arg.split(":")[1];
					} catch (RuntimeException e) {
						valueHttpsKeystoreFile = null;
					}

					if (valueHttpsKeystoreFile != null) {
						configHttp.set("web.https.keystore.keystore-file",
								Boolean.parseBoolean(valueHttpsKeystoreFile));
						configHttps.set("web.https.keystore.keystore-file",
								Boolean.parseBoolean(valueHttpsKeystoreFile));
					}
				}

			}
		}
	}

	/**
	 * @return the httpServer
	 */
	public PolinuxHttpServer getHttpServer() {
		return httpServer;
	}

	/**
	 * @return the httpsServer
	 */
	public PolinuxHttpsServer getHttpsServer() {
		return httpsServer;
	}

//	private void doStuff(PrintStream out) {
//		out.println("hi");
//		out.print("This is test text");
//		out.print(" that ww");
//		out.println();
//		out.print("show if strign out works");
//	}
//
//	private List<Integer> indexesOf(final String content, final String item) {
//		if (!content.contains(item))
//			return new LinkedList<Integer>();
//
//		String editContent = new String(content);
//
//		List<Integer> items = new LinkedList<Integer>();
//
//		while (editContent.length() > 0) {
//			final Integer index = editContent.indexOf(item);
//			if (index <= -1)
//				break;
//			editContent = editContent.substring(index + item.length(), editContent.length());
//			items.add(index);
//		}
//		return items;
//
//	}
//
	public static void main(String[] args) {
		if (!running) {
			running = true;
			new PolinuxLauncher(args);
		}
	}

	public static final PolinuxLauncher getInstance() {
		return instance;
	}

}
