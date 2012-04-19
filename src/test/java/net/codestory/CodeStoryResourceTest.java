package net.codestory;

import com.jayway.restassured.*;
import net.gageot.test.rules.*;
import org.junit.*;

import java.util.*;

import static net.gageot.test.rules.ServiceRule.*;
import static org.fest.assertions.Assertions.*;

public class CodeStoryResourceTest {
	@ClassRule
	public static ServiceRule<CodeStoryServer> codeStoryServer = startWithRandomPort(CodeStoryServer.class);

	private static int port() {
		return codeStoryServer.service().getPort();
	}

	@Test
	public void should_return_all_commits() {
		System.out.print(RestAssured.get("http://localhost:" + port() + "/commits").getBody().asString());
		List<Commit> commits = new CodeStoryResource().commits();
		assertThat(commits.size()).isEqualTo(11);
	}

}
