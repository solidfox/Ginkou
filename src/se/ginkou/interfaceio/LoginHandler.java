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

public class LoginHandler extends HttpRequestHandler{
	public void handleInternal(
			HttpRequest request, 
			HttpResponse response,
			HttpContext context) throws HttpException, IOException {
		if (
				!(request instanceof HttpEntityEnclosingRequest) ||
				!(this.getMethod(request).equals("POST"))
		) {
			response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
			return;
		}
		
		String reqbody = getBody(request);
		int firstBreak = reqbody.indexOf("\n\r");
		String fileName = reqbody.substring(0,firstBreak);
		String args = reqbody.substring(firstBreak+2);
		
		XmlParser parser = new XmlParser("rules/"+fileName, args);
		List<Transaction> trans = parser.run();
		if(trans==null){
			NStringEntity body = new NStringEntity("{module: \"" + fileName + "\", accessGranted: false}", "UTF-8");
			body.setContentType("text/json; charset=UTF-8");
			response.setEntity(body);	
		} else {
			for(Transaction t: trans){

			}
		}
		response.setStatusCode(HttpStatus.SC_OK);
	}
}