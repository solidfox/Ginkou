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
		
		String body = getBody(request);
		int firstBreak = body.indexOf("\n\r");
		String fileName = body.substring(0,firstBreak);
		String args = body.substring(firstBreak+2);
		XmlParser parser = new XmlParser("rules/"+fileName, args);
		
		response.setStatusCode(HttpStatus.SC_OK);
		response.addHeader("Access-Control-Allow-Origin", "*"); //TODO remove this line?
	}
}