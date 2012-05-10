package se.ginkou.interfaceio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.protocol.HttpContext;

import com.google.gson.JsonObject;

import se.ginkou.Debug;
import se.ginkou.Transaction;
import se.ginkou.banking.XmlParser;

public class LoginHandler extends HttpRequestHandler{
	
	private Map<String,String> parseForm(final String formString) {
		String decodedString;
		try {
			decodedString = URLDecoder.decode(formString, "utf-8");
		} catch (UnsupportedEncodingException e) {throw new IllegalStateException("The URLDecoder could not handle utf-8");}
		String[] rawCommands = decodedString.split("&");
		TreeMap<String,String> commands = new TreeMap<String,String>();
		for (String aCommand : rawCommands) {
			String[] commandParts = aCommand.split("=");
			assert(commandParts.length == 2);
			commands.put(commandParts[0], (commandParts.length > 1 ? commandParts[1] : null));
		}
		return commands;
	}
	
	public void handleInternal(
			HttpRequest request, 
			HttpResponse response,
			HttpContext context) throws HttpException, IOException {
		
		if (
				!(request instanceof HttpEntityEnclosingRequest) ||
				!(this.getMethod(request).equals("POST"))
		) {
			Debug.out("LoginHandler recieved a non-POST request");
			response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
			return;
		}
		
		
		Map<String,String> commands = parseForm(getBody(request));
		ArrayList<String> keys = new ArrayList<String>();
		for (int i = 0; i < commands.size(); i++) {
			String soughtKey = "key_" + i;
			if (commands.containsKey(soughtKey)) {
				keys.add(commands.get(soughtKey));
				continue;
			}
			break;
		}
		String[] args = new String[keys.size()];
		keys.toArray(args);
		
		XmlParser parser = new XmlParser("rules/"+commands.get("module"), args);
		List<Transaction> trans = parser.run();
		Debug.out("LoginHandler finished parsing bank");
		response.setStatusCode(HttpStatus.SC_OK);
		if(trans==null){
			NStringEntity body = new NStringEntity("{module: \"" + fileName + "\", accessGranted: false}", "UTF-8");
			body.setContentType("text/json; charset=UTF-8");
			response.setEntity(body);	
		} else {
			for(Transaction t: trans){

			}
		}
		Debug.out("LoginHandler finished");
	}
}