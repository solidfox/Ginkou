package se.ginkou;
import java.awt.Desktop;
import java.net.URI;

import se.ginkou.interfaceio.InterfaceServer;

/**
 * Use this class to run the ginkou interface server and load the interface
 * in the default browser.
 * @author Daniel Schlaug
 *
 */
class Ginkou {
	public static void main(String args[]) throws Exception {
		Desktop.getDesktop().browse(new URI("http://127.0.0.1:38602/"));
		InterfaceServer.startServer();
	}
}