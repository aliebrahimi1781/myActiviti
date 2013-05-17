package my;


import my.server.IServer;
import my.server.ServerFactory;

public class Runner {
	
	private static IServer server;

	
	public static void main(String[] args) {
		
		if (args == null || args.length == 0) {
			server = ServerFactory.getServer();
			server.start();
		}
		else {
			int port = Integer.parseInt(args[0]);
			String context = args[1];
			int scanIntervalSeconds = Integer.parseInt(args[2]);
			server = ServerFactory.getServer(port, context, scanIntervalSeconds);
			server.start();
		}
	}


}
