package net.codestory;

import org.lesscss.*;

import javax.activation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.*;

@Path("/")
public class CodeStoryResource {
	private static final String ROOT_WEB_URL = "src/web";

	@GET
	public File index() {
		return new File(ROOT_WEB_URL, "index.html");
	}

	@GET
	@Path("commits")
	@Produces("application/json;charset=UTF-8")
	public List<Commit> commits() {
		return new AllCommits().list();
	}

	@GET
	@Path("style.less")
	public String style() throws IOException, LessException {
		return new LessCompiler().compile(new File(ROOT_WEB_URL, "style.less"));
	}

	@GET
	@Path("{path : .*}")
	public Response staticResource(@PathParam("path") String path) {
		File file = new File(ROOT_WEB_URL, path);
		if (!file.exists()) {
			throw new WebApplicationException(404);
		}
		String mimeType = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mimeType).build();
	}
}
