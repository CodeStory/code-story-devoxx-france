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

	@Before
	public void setupMocks() {
		when(mockAllCommits.list()).thenReturn(asList(commit("author1", "url1", "message1"), commit("author2", "url2", "message2")));
	}

	@Test
	public void should_list_commits_as_json() {
		expect().body("author", hasItems("author1", "author2")).contentType(JSON) //
				.when().get("/commits");
	}

	@Test
	public void should_show_home_page() {
		assertThat(jsTest("testHomePage.js")).isTrue();
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

	static boolean jsTest(String jsTest) {
		return 0 == new Shell().execute(String.format("./mocha.sh %s %d", jsTest, port()));
	}

	static RepositoryCommit commit(String login, String avatarUrl, String message) {
		return new RepositoryCommit() //
				.setAuthor(new User().setLogin(login).setAvatarUrl(avatarUrl)) //
				.setCommitter(new User().setLogin(login).setAvatarUrl(avatarUrl)) //
				.setCommit(new Commit().setMessage(message).setAuthor(new CommitUser().setDate(new Date())));
	}

	static int port() {
		return codeStoryServer.service().getPort();
	}

	static ResponseSpecification expect() {
		return given().port(port()).expect();
	}
}
