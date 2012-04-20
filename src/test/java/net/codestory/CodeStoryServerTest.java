package net.codestory;

import com.google.inject.*;
import com.jayway.restassured.specification.*;
import net.gageot.test.rules.*;
import net.gageot.test.utils.*;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.*;
import org.junit.*;

import java.io.*;
import java.util.*;

import static com.jayway.restassured.RestAssured.*;
import static groovyx.net.http.ContentType.*;
import static java.util.Arrays.*;
import static net.gageot.test.rules.ServiceRule.*;
import static org.fest.assertions.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class CodeStoryServerTest {
	static AllCommits mockAllCommits = mock(AllCommits.class);

	@ClassRule
	public static ServiceRule<CodeStoryServer> codeStoryServer = startWithRandomPort(CodeStoryServer.class, new AbstractModule() {
		@Override
		protected void configure() {
			bind(AllCommits.class).toInstance(mockAllCommits);
		}
	});

	@Test
	public void should_list_commits_as_json() {
		when(mockAllCommits.list()).thenReturn(asList( //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("jlm")) //
						.setCommit(new Commit().setMessage("")) //
						.setAuthor(new User().setAvatarUrl("")), //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("dgageot")) //
						.setCommit(new Commit().setMessage("")).setAuthor(new User().setAvatarUrl(""))));

		expect().body("author", hasItems("jlm", "dgageot")).contentType(JSON) //
				.when().get("/commits");
	}

	@Test
	public void can_show_home_page() {
		when(mockAllCommits.list()).thenReturn(asList( //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("").setAvatarUrl("url1")) //
						.setCommit(new Commit().setMessage("message1") //
								.setAuthor(new CommitUser().setDate(new Date()))) //
						.setAuthor(new User().setAvatarUrl("url1")), //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("").setAvatarUrl("url2")) //
						.setCommit(new Commit().setMessage("message2")) //
						.setAuthor(new User().setAvatarUrl("url2")))); //

		int exitCode = new Shell().execute("./mocha.sh testHomePage.js " + port());

		assertThat(exitCode).isZero();
	}

	@Test
	public void should_list_badges_as_json() {
		expect().body("label", hasItems("Top Committer", "Fatty Committer")) //
				.when().get("/badges");
	}

	@Test
	public void should_serve_style_as_less() throws IOException {
		expect().content(containsString("body")) //
				.when().get("/style.less");
	}

	@Test
	public void should_return_a_404() {
		expect().statusCode(404) //
				.when().get("/foobar");
	}

	@Test
	public void should_serve_favicon() {
		expect().statusCode(200) //
				.when().get("/fusee-16x16.png");
	}

	static int port() {
		return codeStoryServer.service().getPort();
	}

	static ResponseSpecification expect() {
		return given().port(port()).expect();
	}
}
