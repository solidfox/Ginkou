package se.ginkou.interfaceio;

import java.io.IOException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.protocol.HttpContext;

public class DataTablesHandler extends HttpRequestHandler {
	
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
		
		response.setStatusCode(HttpStatus.SC_OK);
		//response.addHeader("Access-Control-Allow-Origin", "*");
		DataTablesInterface DTInterface = new DataTablesInterface(getBody(request));
	    NStringEntity body;
		body = new NStringEntity(DTInterface.getResponse(), "UTF-8");
		body.setContentType("text/json; charset=UTF-8");
	    response.setEntity(body);
	}
    
}
