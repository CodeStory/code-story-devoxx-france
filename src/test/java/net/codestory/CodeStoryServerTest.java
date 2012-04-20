package net.codestory;

import com.google.common.io.*;
import com.google.inject.*;
import net.gageot.test.rules.*;
import net.gageot.test.utils.*;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.*;
import org.junit.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static com.google.common.base.Charsets.*;
import static com.jayway.restassured.RestAssured.*;
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

	static int port() {
		return codeStoryServer.service().getPort();
	}

	@Test
	public void should_return_commits_json() {
		when(mockAllCommits.list()).thenReturn(asList( //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("jlm")) //
						.setCommit(new Commit().setMessage("")) //
						.setAuthor(new User().setAvatarUrl("")), //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("dgageot")) //
						.setCommit(new Commit().setMessage("")).setAuthor(new User().setAvatarUrl(""))));

		given().port(port()).expect().body("author", hasItems("jlm", "dgageot")).when().get("/commits");
	}

	@Test
	public void should_return_badges_json() {
		given().port(port()).expect().body("label", hasItems("Top Committer", "Fatty Committer")).when().get("/badges");
	}

	@Test
	public void should_serve_style_as_less() throws IOException {
		String css = Resources.toString(new URL("http://localhost:" + port() + "/style.less"), UTF_8);

		assertThat(css).contains("color: #ffffff;");
	}

	@Test
	public void should_return_a_404() {
		given().port(port()).response().statusCode(404).when().get("/foobar");
	}

	@Test
	public void should_serve_favicon() {
		given().port(port()).response().statusCode(200).when().get("/fusee-16x16.png");
	}

	@Test
	public void can_show_commit_list() {
		when(mockAllCommits.list()).thenReturn(asList( //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("author1").setAvatarUrl("url1")) //
						.setCommit(new Commit().setMessage("message1") //
								.setAuthor(new CommitUser().setDate(new Date()))) //
						.setAuthor(new User().setAvatarUrl("url1")), //
				new RepositoryCommit() //
						.setCommitter(new User().setLogin("author2").setAvatarUrl("url2")) //
						.setCommit(new Commit().setMessage("message2")) //
						.setAuthor(new User().setAvatarUrl("url2")))); //

		int exitCode = new Shell().execute("./mocha.sh testListeCommits.js " + port());

		assertThat(exitCode).isZero();
	}

	@Test
	public void should_work_with_a_commit_without_commiter() {
		RepositoryCommit repositoryCommit = new RepositoryCommit();

		net.codestory.Commit commit = CodeStoryResource.TO_COMMIT.apply(repositoryCommit);

		assertThat(commit.getAuthor()).isEqualTo("");
	}
}
