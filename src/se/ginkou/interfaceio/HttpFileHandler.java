package se.ginkou.interfaceio;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.entity.NFileEntity;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

class HttpFileHandler extends HttpRequestHandler {

    private final File docRoot;

    public HttpFileHandler(final File docRoot) {
        super();
        this.docRoot = docRoot;
    }
    
    public void handleInternal(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {

        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }

        String target = request.getRequestLine().getUri();
        File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
        if (file.isDirectory()) {file = new File(file, "index.html");}
        if (!file.exists()) {

            response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            NStringEntity entity = new NStringEntity(
                    "<html><body><h1>File" + file.getPath() +
                    " not found</h1></body></html>",
                    ContentType.create("text/html", "UTF-8"));
            response.setEntity(entity);
            System.out.println("File " + file.getPath() + " not found");

        } else if (!file.canRead() || file.isDirectory()) {

            response.setStatusCode(HttpStatus.SC_FORBIDDEN);
            NStringEntity entity = new NStringEntity(
                    "<html><body><h1>Access denied</h1></body></html>",
                    ContentType.create("text/html", "UTF-8"));
            response.setEntity(entity);
            System.out.println("Cannot read file " + file.getPath());

        } else {
            NHttpConnection conn = (NHttpConnection) context.getAttribute(
                    ExecutionContext.HTTP_CONNECTION);
            response.setStatusCode(HttpStatus.SC_OK);
            NFileEntity body = new NFileEntity(file, contentTypeOf(file));
            response.setEntity(body);
            System.out.println(conn + ": serving file " + file.getPath());
        }
    }

	private ContentType contentTypeOf(File file) {
		String[] splitFile = file.getName().split("\\.");
		String fileEnding = splitFile[splitFile.length-1];
		if (fileEnding.equals("css")) {
			return ContentType.create("text/css"); 
		} else if (fileEnding.equals("js")) {
			return ContentType.create("application/javascript");
		} else {
			return ContentType.create("text/html");
		} 
	}
}