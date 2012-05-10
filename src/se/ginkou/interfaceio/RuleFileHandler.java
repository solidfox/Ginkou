package se.ginkou.interfaceio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import se.ginkou.Transaction;
import se.ginkou.banking.XmlParser;
/**
 * Handles calls to /loginmodules.
 * Responds with JSON containing of the following form
 * {
 * "[moduleName]" :
 * 		{
 * 		"module": "[moduleName]",
 * 		"bankName": "[bankName]",
 * 		"key_0": "[keyName]",		E g Username
 * 		"key_1": "[keyName]", 		E g Password
 * 		"key_n": "[keyName]"
 * 		},
 * "[moduleName2]" : {...},
 * "[moduleName3]" : {...}
 * }
 * @author spike
 *
 */
public class RuleFileHandler extends HttpRequestHandler{
	private final static String NAME = RuleFileHandler.class.getName();
	public void handleInternal(
			HttpRequest request, 
			HttpResponse response,
			HttpContext context) throws HttpException, IOException {

		if (
				!(this.getMethod(request).equals("GET"))
		) {
			response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
			return;
		}

		JsonObject returnJson = new JsonObject();
		String[] sfiles = (new File("rules")).list();
		for(String s: sfiles){
			if(!s.matches(".*\\.xml"))
				continue;
			BufferedReader file = null;
			try {
				// FileReader uses "the default character encoding".
				file = new BufferedReader(new FileReader("rules/"+s));
				// To specify an encoding, use this code instead:
				// file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

				String line;
				ArrayList<String> a = new ArrayList<String>();
				for (boolean reqording = false; (line = file.readLine()) != null && !line.contains("-->");) {
					if(line.equals("<!--")){
						reqording = true;
						continue;
					}
					if(reqording)
						a.add(line);
				}
				if(a.size()>0){
					JsonObject fileInfo = new JsonObject();
					fileInfo.addProperty("module", s);
					fileInfo.addProperty("bankName", a.get(0));
					for(int i = 1; i<a.size(); ++i){
						fileInfo.addProperty("key_"+(i-1), a.get(i));
					}
					returnJson.add(s, fileInfo);
				}
			} catch (IOException e) {
				System.err.printf("%s: %s%n", NAME, e);
			} finally {
				try {
					if (file != null) {
						file.close();
					}
				} catch (IOException e) {
					System.err.printf("%s: %s%n", NAME, e);
				}
			}
		}
		response.setStatusCode(HttpStatus.SC_OK);
		NStringEntity body = new NStringEntity(returnJson.toString(), "UTF-8");
		body.setContentType("text/json; charset=UTF-8");
		response.setEntity(body);
	}
}
