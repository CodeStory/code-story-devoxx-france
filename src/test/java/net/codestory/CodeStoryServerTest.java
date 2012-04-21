package net.codestory;

import com.google.inject.*;
import com.jayway.restassured.specification.*;
import net.codestory.github.*;
import net.codestory.jenkins.*;
import net.gageot.test.rules.*;
import net.gageot.test.utils.*;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.*;
import org.junit.*;

import java.util.*;

import static com.jayway.restassured.RestAssured.*;
import static groovyx.net.http.ContentType.*;
import static java.lang.String.format;
import static java.util.Arrays.*;
import static net.gageot.test.rules.ServiceRule.*;
import static org.fest.assertions.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class CodeStoryServerTest {
	static AllCommits mockAllCommits = mock(AllCommits.class);
	static AllBuilds mockAllBuilds = mock(AllBuilds.class);

	@ClassRule
	public static ServiceRule<CodeStoryServer> codeStoryServer = startWithRandomPort(CodeStoryServer.class, new AbstractModule() {
		@Override
		protected void configure() {
			bind(AllCommits.class).toInstance(mockAllCommits);
			bind(AllBuilds.class).toInstance(mockAllBuilds);
		}
	});

	@Before
	public void setupMocks() {
		when(mockAllCommits.list()).thenReturn(asList( //
				commit("sha1", "author1", "url1", "message1"), //
				commit("", "author2", "url2", "message2")));

		when(mockAllBuilds.list()).thenReturn(asList( //
				build("SUCCESS", "sha1")));
	}

	@Test
	public void should_show_home_page() {
		assertThat(jsTest("testHomePage.js")).isTrue();
	}

	@Test
	public void should_list_commits_as_json() {
		expect().contentType(JSON).body("author", hasItems("author1", "author2")) //
				.when().get("/commits");
	}

	@Test
	public void should_list_badges_as_json() {
		expect().contentType(JSON).body("label", hasItems("Top Committer", "Fatty Committer")) //
				.when().get("/badges");
	}

	@Test
	public void should_serve_style_as_less() {
		expect().contentType(TEXT).content(containsString("body")) //
				.when().get("/style.less");
	}

	@Test
	public void should_serve_favicon() {
		expect().contentType(BINARY).statusCode(200) //
				.when().get("/fusee-16x16.png");
	}

	@Test
	public void should_return_a_404() {
		expect().statusCode(404) //
				.when().get("/not_found");
	}

	static boolean jsTest(String jsTest) {
		return 0 == new Shell().execute(format("./mocha.sh %s %d", jsTest, port()));
	}

	static Build build(String result, String sha1) {
		return new Build(result, new ChangesSet(asList(new Item(sha1))));
	}

	static RepositoryCommit commit(String sha1, String login, String avatarUrl, String message) {
		return new RepositoryCommit() //
				.setStats(new CommitStats().setAdditions(0).setDeletions(0)) //
				.setAuthor(new User().setLogin(login).setAvatarUrl(avatarUrl)) //
				.setCommitter(new User().setLogin(login).setAvatarUrl(avatarUrl)) //
				.setCommit(new Commit().setSha(sha1).setMessage(message).setAuthor(new CommitUser().setDate(new Date())));
	}

	static int port() {
		return codeStoryServer.service().getPort();
	}

	static ResponseSpecification expect() {
		return given().port(port()).expect();
	}
}
