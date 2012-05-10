package se.ginkou.interfaceio;

import java.io.IOException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.protocol.HttpContext;

public class TestHandler extends HttpRequestHandler {

	@Override
	public void handleInternal(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException {
		response.setStatusCode(HttpStatus.SC_OK);
		NStringEntity body;
		body = new NStringEntity("Ping successful, recieved body: " + getBody(request), "UTF-8");
		body.setContentType("text/plain; charset=UTF-8");
	    response.setEntity(body);
	}

}
