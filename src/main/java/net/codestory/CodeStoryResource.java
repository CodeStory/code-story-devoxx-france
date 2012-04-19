package net.codestory;

import org.lesscss.*;

import javax.ws.rs.*;
import java.io.*;
import java.util.*;

@Path("/")
public class CodeStoryResource {
	private static final String ROOT_WEB_URL = "src/web";

	@GET
	public File index() {
		return new File(ROOT_WEB_URL + "index.html");
	}

	@GET
	@Path("commits")
	@Produces("application/json;charset=UTF-8")
	public List<Commit> commits() {
		return new AllCommits().list();
	}

	@GET
	@Path("{path: .*\\.js }")
	@Produces("application/javascript;charset=UTF-8")
	public File script(@PathParam("path") String path) {
		return new File(ROOT_WEB_URL, path);
	}

	@GET
	@Path("style.less")
	public String style() throws IOException, LessException {
		return new LessCompiler().compile(new File(ROOT_WEB_URL, "style.less"));
	}

	@GET
	@Path("{path: .*\\.png }")
	@Produces("image/png")
	public File images(@PathParam("path") String path) {
		return new File(ROOT_WEB_URL, path);
	}
}
