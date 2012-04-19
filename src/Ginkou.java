import java.io.*;
import java.net.*;

class Ginkou {
	public static void main(String args[]) {
		// Info från http://stackoverflow.com/questions/6298479/java-socket-programming-listen-to-port
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader in = null;
		try {
			serverSocket = new ServerSocket(4081);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true)
		{
			try {
				socket = serverSocket.accept();
				System.out.println(socket.getKeepAlive());
				in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    String cominginText = "";
		    while (true) {
			    try
			    {
			        cominginText = in.readLine();
				    System.out.println (cominginText);
			    }
			    catch (IOException e)
			    {
			        //error ("System: " + "Connection to server lost!");
			        System.exit (1);
			        break;
			    }
			}
		}
	}
}