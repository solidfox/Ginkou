import java.io.*;
import java.net.*;

class Ginkou {
	public static void main(String args[]) {
		// Info från http://stackoverflow.com/questions/6298479/java-socket-programming-listen-to-port
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader in = null;
		try {
			serverSocket = new ServerSocket(4080);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket = serverSocket.accept();
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true)
		{
		    String cominginText = "";
		    try
		    {
		        cominginText = in.readLine ();
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