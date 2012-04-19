import javax.ws.rs.*;
import java.io.*;

@Path("/")
public class CodeStoryResource {
	@GET
	public File index() {
		return new File("index.html");
	}

	@GET
	@Path("{path: .* }")
	@Produces("application/javascript;charset=UTF-8")
	public File script(@PathParam("path") String path) {
		return new File(path);
	}
}
