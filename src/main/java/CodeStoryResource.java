import javax.ws.rs.*;
import java.io.*;

@Path("/")
public class CodeStoryResource {
	@GET
	public File index() {
		return new File("index.html");
	}

	@GET
	@Path("mustache.js")
	@Produces("application/javascript;charset=UTF-8")
	public File mustache() {
		return new File("mustache.js");
	}

	@GET
	@Path("jquery.js")
	@Produces("application/javascript;charset=UTF-8")
	public File jquery() {
		return new File("jquery.js");
	}
}
