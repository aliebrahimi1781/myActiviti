package common.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.tools.Server;

import common.AppConfig;

public class AppInitServlet extends HttpServlet {
	
	private static Log log = LogFactory.getLog(AppInitServlet.class);

	private static Server h2Server = null;
	
	@Override
	public void destroy() {
		super.destroy();
		log.info("h2 server stop...");
		h2Server.stop();
		log.info("h2 server stoped");
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		initAppConfig(config);
		startH2Server();
		
	}
	
	private void startH2Server() {
		try {
			log.info("h2 server start...");
			h2Server = Server.createWebServer("-web").start();
			log.info("h2 server start complete");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("h2 server start fail");
		}
	}

	private void initAppConfig(ServletConfig config) {
		AppConfig.context = config.getServletContext();

		String webroot = AppInitServlet.class.getResource("/").getFile();
		try {
			webroot = new File(webroot).getParentFile().getParentFile()
					.getCanonicalPath();
			webroot += "/";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		AppConfig.webroot = webroot;
		AppConfig.upload = webroot + "WEB-INF/upload/";
	}

}
