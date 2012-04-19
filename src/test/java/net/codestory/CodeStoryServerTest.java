package net.codestory;

import com.google.common.io.*;
import com.google.inject.*;
import net.gageot.test.rules.*;
import net.gageot.test.utils.*;
import org.junit.*;

import java.io.*;
import java.net.*;

import static com.google.common.base.Charsets.*;
import static com.jayway.restassured.RestAssured.*;
import static java.util.Arrays.*;
import static net.gageot.test.rules.ServiceRule.*;
import static org.fest.assertions.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class CodeStoryServerTest {
	private final static AllCommits mockAllCommits = mock(AllCommits.class);

	@ClassRule
	public static ServiceRule<CodeStoryServer> codeStoryServer = startWithRandomPort(CodeStoryServer.class, new AbstractModule() {
		@Override
		protected void configure() {
			bind(AllCommits.class).toInstance(mockAllCommits);
		}
	});

	private static int port() {
		return codeStoryServer.service().getPort();
	}

	@Test
	public void should_return_commits_json() {
		when(mockAllCommits.list()).thenReturn(asList( //
				new Commit("jlm", "", "", ""), //
				new Commit("dgageot", "", "", "")));

		given().port(port()).expect().body("author", hasItems("jlm", "dgageot")).when().get("/commits");
	}

	@Test
	public void should_serve_style_as_less() throws IOException {
		String css = Resources.toString(new URL("http://localhost:" + port() + "/style.less"), UTF_8);

		assertThat(css).contains("color: #ffffff;");
	}

	@Test
	public void should_serve_favicon() {
		given().port(port()).response().statusCode(200).when().get("/fusee-16x16.png");
	}

	@Test
	public void can_show_commit_list() {
		when(mockAllCommits.list()).thenReturn(asList( //
				new Commit("author1", "url1", "message1", "date1"), //
				new Commit("author2", "url2", "message2", "date2")));

		int exitCode = new Shell().execute("./mocha.sh testListeCommits.js " + port());

		assertThat(exitCode).isZero();
	}
}
