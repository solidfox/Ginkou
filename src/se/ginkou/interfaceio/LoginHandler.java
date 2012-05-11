package se.ginkou.interfaceio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.gson.JsonObject;

import se.ginkou.Account;
import se.ginkou.Debug;
import se.ginkou.Transaction;
import se.ginkou.banking.XmlParser;
import se.ginkou.database.Database;
import se.ginkou.database.SQLiteDB;

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
		
		String fileName = commands.get("module");
		
		XmlParser parser = new XmlParser("rules/"+fileName, args);
		List<Transaction> trans = parser.run();
		Debug.out("LoginHandler finished parsing bank");
		response.setStatusCode(HttpStatus.SC_OK);
		if(trans==null){
			NStringEntity body = new NStringEntity("{module: \"" + fileName + "\", accessGranted: false}", "UTF-8");
			body.setContentType("text/json; charset=UTF-8");
			response.setEntity(body);	
		} else {
			Database db = SQLiteDB.getDB();
			HashSet<Account> sa = new HashSet<Account>();
			for(Transaction t: trans){
				sa.add(t.getAccount());
			}
			HashMap<Account, DateTime> accountStatus = new HashMap<Account, DateTime>();
			DateTime defClearDate = (new DateTime()).minusDays(14);
			for(Account saInst: sa){
				if(db.getAccounts().contains(saInst)){
					DateTime latestUpdate = db.getTransactions("SELECT * FROM transactions WHERE accountID IS "+saInst+" ORDER BY date desc LIMIT 1").get(0).getDate();
					db.clearAllTransactionsFrom(latestUpdate.minusDays(14), saInst);
					accountStatus.put(saInst, latestUpdate.minusDays(14));
				}
				else
					accountStatus.put(saInst, null);
			}
			List<Transaction> toDB = new ArrayList<Transaction>();
			for(Transaction t: trans){
				DateTime addAfter = accountStatus.get(t.getAccount());
				if(addAfter==null || t.getDate().isAfter(addAfter))
					toDB.add(t);
			}
			db.addTransactions(toDB);
		}
		Debug.out("LoginHandler finished");
	}
}