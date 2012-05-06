package se.ginkou.interfaceio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public abstract class HttpRequestHandler implements org.apache.http.protocol.HttpRequestHandler {

	String getMethod(HttpRequest request) {
		return request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
	}
	
	String getBody(HttpRequest request) {
		if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            byte[] entityContent;
			try {
				entityContent = EntityUtils.toByteArray(entity);
				return new String(entityContent, "utf-8");
			} catch (UnsupportedEncodingException e) {} catch (IOException e1) {} // TODO Handle exceptions
        }
		return "";
	}
	
	@Override
	public abstract void handle(HttpRequest request, HttpResponse response,
			HttpContext context);

}
