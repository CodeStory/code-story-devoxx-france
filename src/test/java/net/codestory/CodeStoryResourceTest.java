package net.codestory;

import net.gageot.test.rules.*;
import org.junit.*;

import static com.jayway.restassured.RestAssured.*;
import static net.gageot.test.rules.ServiceRule.*;
import static org.hamcrest.Matchers.*;

public class CodeStoryResourceTest {
	@ClassRule
	public static ServiceRule<CodeStoryServer> codeStoryServer = startWithRandomPort(CodeStoryServer.class);

	private static int port() {
		return codeStoryServer.service().getPort();
	}

	@Test
	public void should_return_commits_json() {
		given().port(port()).expect().body("author", hasItems("jlm", "dgageot")).when().get("/commits");
	}

}
