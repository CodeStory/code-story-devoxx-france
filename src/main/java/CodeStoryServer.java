import com.google.common.util.concurrent.*;
import com.sun.jersey.api.container.httpserver.*;
import com.sun.net.httpserver.*;

public class CodeStoryServer extends AbstractIdleService {
	private HttpServer httpServer;
	private final int port;

	public CodeStoryServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		new CodeStoryServer(8080).startAndWait();
	}

	@Override
	protected void startUp() throws Exception {
		httpServer = HttpServerFactory.create("http://localhost:" + port + "/");
		httpServer.start();
	}

	@Override
	protected void shutDown() throws Exception {
		httpServer.stop(1);
	}

	public int getPort() {
		return port;
	}
}
