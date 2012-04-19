package net.codestory;

import com.sun.jersey.api.*;
import com.google.inject.*;
import org.lesscss.*;

import javax.activation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.*;
import java.util.*;

@Path("/")
public class CodeStoryResource {
	private static final String ROOT_WEB_URL = "src/web";

	@Inject
	private AllCommits allCommits;

	@Inject
	private AllBadges allBadges;

	@GET
	public Response index() {
		return Response.temporaryRedirect(URI.create("index.html")).build();
	}

	@GET
	@Path("commits")
	@Produces("application/json;charset=UTF-8")
	public List<Commit> commits() {
		return allCommits.list();
	}

	@GET
	@Path("badges")
	@Produces("application/json;charset=UTF-8")
	public List<Badge> badges() {
		return allBadges.list();
	}

	@GET
	@Path("{path : .*\\.less}")
	public String style(@PathParam("path") String path) throws IOException, LessException {
		return new LessCompiler().compile(new File(ROOT_WEB_URL, path));
	}

	@GET
	@Path("{path : .*}")
	public Response staticResource(@PathParam("path") String path) {
		File file = new File(ROOT_WEB_URL, path);
		if (!file.exists()) {
			throw new NotFoundException();
		}
		String mimeType = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mimeType).build();
	}
}
