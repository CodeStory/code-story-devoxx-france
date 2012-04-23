package net.codestory;

import com.google.common.util.concurrent.*;
import com.google.inject.*;
import com.sun.jersey.api.container.httpserver.*;
import com.sun.jersey.api.core.*;
import com.sun.jersey.core.spi.component.ioc.*;
import com.sun.jersey.guice.spi.container.*;
import com.sun.net.httpserver.*;
import net.codestory.cache.*;
import net.codestory.commits.*;
import org.codehaus.jackson.jaxrs.*;

import static com.google.common.base.Objects.*;
import static com.google.inject.Guice.*;
import static com.google.inject.matcher.Matchers.*;
import static com.google.inject.util.Modules.*;

public class CodeStoryServer extends AbstractIdleService {
	private static final String DEFAULT_PORT = "8085";

	private final int port;
	private final Module[] modules;
	private HttpServer httpServer;

	public CodeStoryServer(int port, Module... modules) {
		this.port = port;
		this.modules = modules;
	}

	int getPort() {
		return port;
	}

	@Override
	protected void startUp() throws Exception {
		ResourceConfig config = new DefaultResourceConfig(CodeStoryResource.class, JacksonJsonProvider.class);

		Module module = override(new CodeStoryModule()).with(modules);
		Injector injector = createInjector(module);

		IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(config, injector);

		httpServer = HttpServerFactory.create("http://localhost:" + port + "/", config, ioc);
		httpServer.start();
	}

	@Override
	protected void shutDown() {
		httpServer.stop(1);
	}

	public static void main(String[] args) {
		int port = Integer.valueOf(firstNonNull(System.getenv("PORT"), DEFAULT_PORT));

		new CodeStoryServer(port).startAndWait();
	}

	static class CodeStoryModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(AllCommits.class).toInstance(new AllCommits("dgageot", "CodeStoryDevoxx"));
			bindInterceptor(any(), annotatedWith(Cached.class), new CacheInterceptor());
		}
	}
}
