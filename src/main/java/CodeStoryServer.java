import com.google.common.util.concurrent.*;
import com.sun.jersey.api.container.httpserver.*;
import com.sun.net.httpserver.*;

public class CodeStoryServer extends AbstractIdleService {
	private final int port;
	private HttpServer httpServer;

	public CodeStoryServer(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	@Override
	protected void startUp() throws Exception {
		httpServer = HttpServerFactory.create("http://localhost:" + port + "/");
		httpServer.start();
	}

	@Override
	protected void shutDown() {
		httpServer.stop(1);
	}

	public static void main(String[] args) {
		new CodeStoryServer(8080).startAndWait();
	}
}
