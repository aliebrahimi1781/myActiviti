package my.server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import my.kit.PathKit;
import my.kit.StringKit;
import my.log.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer implements IServer {
	
	private Logger log = Logger.getLogger(getClass());

	private String webAppDir;
	private int port;
	private String context;
	private int scanIntervalSeconds;
	private boolean running = false;
	private Server server;
	private WebAppContext webApp;
	
	
	JettyServer(String webAppDir, int port, String context, int scanIntervalSeconds) {
		if (webAppDir == null)
			throw new IllegalStateException("Invalid webAppDir of web server: " + webAppDir);
		if (port < 0 || port > 65536)
			throw new IllegalArgumentException("Invalid port of web server: " + port);
		if (StringKit.isBlank(context))
			throw new IllegalStateException("Invalid context of web server: " + context);
		
		this.webAppDir = webAppDir;
		this.port = port;
		this.context = context;
		this.scanIntervalSeconds = scanIntervalSeconds;
	}


	@Override
	public void start() {
		if (!running) {
			try {
				doStart();
			} catch (Exception e) {
				e.printStackTrace();
			}
			running = true;
		}
	}

	@Override
	public void stop() {
		if (running) {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			running = false;
		}
	}

	private void doStart() {
		if (!available(port))
			throw new IllegalStateException("port: " + port + " already in use!");

		log.info("starting..........");
		
		server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.addConnector(connector);
		webApp = new WebAppContext();
		webApp.setContextPath(context);
		webApp.setResourceBase(webAppDir);
		// webApp.setWar(webAppDir);
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
		// webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));
		
		persistSession(webApp);

		server.setHandler(webApp);

		// configureScanner
		if (scanIntervalSeconds > 0) {
			Scanner scanner = new Scanner(PathKit.getRootClassPath(), scanIntervalSeconds) {
				public void onChange() {
					try {
						log.info("Loading chages..........");
						
						webApp.stop();
						webApp.start();
						log.info("Loading complete.");
					} catch (Exception e) {
						log.error("Error reconfiguring/restarting webapp after change in watched files");
						e.printStackTrace();
					}
				}
			};
			log.info("Starting scanner at interval of " + scanIntervalSeconds + " seconds.");
			scanner.start();
		}

		try {
			log.info("Starting web server on port: " + port);
			server.start();
			log.info("Starting Complete.");
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
		return;
	}

	private static boolean available(int port) {
		if (port <= 0) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					// should not be thrown, just detect port available.
				}
			}
		}
		return false;
	}
	
	private void persistSession(WebAppContext webApp) {
		String storeDir = PathKit.getWebRootPath() + "/../../session_data" + context;
		if ("\\".equals(File.separator))
			storeDir = storeDir.replaceAll("/", "\\\\");
		
		SessionManager sm = webApp.getSessionHandler().getSessionManager();
		if (sm instanceof HashSessionManager) {
			((HashSessionManager)sm).setStoreDirectory(new File(storeDir));
			return ;
		}
		
		HashSessionManager hsm = new HashSessionManager();
		hsm.setStoreDirectory(new File(storeDir));
		SessionHandler sh = new SessionHandler();
		sh.setSessionManager(hsm);
		webApp.setSessionHandler(sh);
	}
}
