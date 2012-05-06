import java.awt.Desktop;
import java.net.URI;

import se.ginkou.interfaceio.InterfaceServer;

class Ginkou {
	public static void main(String args[]) throws Exception {
		Desktop.getDesktop().browse(new URI("http://ginkou.se"));
		InterfaceServer.startServer();
	}
}