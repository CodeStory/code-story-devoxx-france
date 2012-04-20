package net.codestory;

import com.google.common.util.concurrent.*;
import com.google.inject.*;
import com.google.inject.name.*;
import com.google.inject.util.*;
import com.sun.jersey.api.container.httpserver.*;
import com.sun.jersey.api.core.*;
import com.sun.jersey.guice.spi.container.*;
import com.sun.net.httpserver.*;
import org.codehaus.jackson.jaxrs.*;

public class CodeStoryServer extends AbstractIdleService {
	private final int port;
	private final Module[] modules;
	private HttpServer httpServer;

	public CodeStoryServer(int port, Module... modules) {
		this.port = port;
		this.modules = modules;
	}

	public int getPort() {
		return port;
	}

	@Override
	protected void startUp() throws Exception {
		ResourceConfig config = new DefaultResourceConfig(CodeStoryResource.class, JacksonJsonProvider.class);
		Modules.OverriddenModuleBuilder codeStoryModule = Modules.override(new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("user")).toInstance("dgageot");
				bind(String.class).annotatedWith(Names.named("project")).toInstance("sonar");
			}
		});
		Injector injector = Guice.createInjector(codeStoryModule.with(modules));
		GuiceComponentProviderFactory ioc = new GuiceComponentProviderFactory(config, injector);

		httpServer = HttpServerFactory.create("http://localhost:" + port + "/", config, ioc);
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
