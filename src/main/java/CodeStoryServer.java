import com.sun.jersey.api.container.httpserver.*;
import com.sun.net.httpserver.*;

import javax.ws.rs.*;
import java.io.*;

@Path("/")
public class CodeStoryServer {
	@GET
	public File index() {
		return new File("index.html");
	}

	public static void main(String[] args) throws Exception {
		HttpServer httpServer = HttpServerFactory.create("http://localhost:8080/");
		httpServer.start();
	}
}
