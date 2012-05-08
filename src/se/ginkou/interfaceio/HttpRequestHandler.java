package se.ginkou.interfaceio;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public abstract class HttpRequestHandler implements HttpAsyncRequestHandler<HttpRequest> {

	String getMethod(HttpRequest request) {
		return request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
	}
	
	String getBody(HttpRequest request) throws IOException {
		if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            byte[] entityContent;
			entityContent = EntityUtils.toByteArray(entity);
			return new String(entityContent, "utf-8");
        }
		return "";
	}
	
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(
            final HttpRequest request,
            final HttpContext context) {
        // Buffer request content in memory for simplicity
        return new BasicAsyncRequestConsumer();
    }

    public void handle(
            final HttpRequest request,
            final HttpAsyncExchange httpexchange,
            final HttpContext context) throws HttpException, IOException {
        HttpResponse response = httpexchange.getResponse();
        handleInternal(request, response, context);
        httpexchange.submitResponse(new BasicAsyncResponseProducer(response));
    }
	
	public abstract void handleInternal(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException ;

}
