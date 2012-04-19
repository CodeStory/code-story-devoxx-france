import javax.ws.rs.*;
import java.io.*;

@Path("/")
public class CodeStoryResource {
	@GET
	public File index() {
		return new File("index.html");
	}
}
