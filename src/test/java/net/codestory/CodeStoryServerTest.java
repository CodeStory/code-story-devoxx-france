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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import static com.jayway.restassured.RestAssured.*;
import static groovyx.net.http.ContentType.*;
import static java.lang.String.*;
import static java.util.Arrays.*;
import static net.gageot.test.rules.ServiceRule.*;
import static org.fest.assertions.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;


import com.google.common.io.Files;
import com.google.inject.AbstractModule;
import com.jayway.restassured.specification.ResponseSpecification;
import net.codestory.jenkins.AllBuilds;
import net.codestory.jenkins.Build;
import net.codestory.jenkins.ChangesSet;
import net.codestory.jenkins.Item;
import net.gageot.test.rules.ServiceRule;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.*;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static net.gageot.test.rules.ServiceRule.startWithRandomPort;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodeStoryServerTest {
	static AllCommits mockAllCommits = mock(AllCommits.class);
	static AllBuilds mockAllBuilds = mock(AllBuilds.class);
	static final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);

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

//	@Test
//	public void should_show_home_page() {
//		assertThat(jsTest("testHomePage.js")).isTrue();
//	}

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



	@Test
	public void html_good_title() throws IOException {
		final HtmlPage startPage = webClient.getPage("http://localhost:" + port() + "/");
		assertEquals("CodeStory - HomePage", startPage.getTitleText());
	}

	@Test
	public void html_commits() throws IOException, InterruptedException {
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		final HtmlPage startPage = webClient.getPage("http://localhost:" + port() + "/");
		webClient.waitForBackgroundJavaScript(5000);


		startPage.executeJavaScript(Files.toString(new File("expect.js"), Charset.forName("UTF-8")));

		startPage.executeJavaScript(
				"expect($(\"#commits .commit:nth-child(1) .description:contains('message1')\")).to.not.be.empty();" +
						"expect($(\"#commits .commit:nth-child(2) .description:contains('message2')\")).to.not.be.empty();" +
						"expect($(\"#commits .commit:nth-child(1) img[src='url1']\")).to.not.be.empty();" +
						"expect($(\"#commits .commit:nth-child(2) img[src='url2']\")).to.not.be.empty();" +
						"expect($(\"#commits .commit:nth-child(1) img[class='portrait SUCCESS']\")).to.not.be.empty();" +
						"expect($(\"#commits .commit:nth-child(2) img[class='portrait FAILURE']\")).to.not.be.empty();"
		);
	}

	@Test
	public void html_project_name() throws IOException, InterruptedException {
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		final HtmlPage startPage = webClient.getPage("http://localhost:" + port() + "/");
		webClient.waitForBackgroundJavaScript(5000);

		startPage.executeJavaScript(Files.toString(new File("expect.js"), Charset.forName("UTF-8")));

		startPage.executeJavaScript(
				"expect($(\"h2:contains('CodeStory')\")).to.not.be.empty();"
		);
	}

	@Test
	public void html_badges() throws IOException, InterruptedException {
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		final HtmlPage startPage = webClient.getPage("http://localhost:" + port() + "/");
		webClient.waitForBackgroundJavaScript(5000);

		startPage.executeJavaScript(Files.toString(new File("expect.js"), Charset.forName("UTF-8")));

		startPage.executeJavaScript(
				"expect($(\"#badges .badge:nth-child(1) p:contains('Top Committer')\")).to.not.be.empty();" +
						"expect($(\"#badges .badge:nth-child(2) p:contains('Fatty Committer')\")).to.not.be.empty();" +
						"expect($(\"#badges .badge:nth-child(1) img[src='/badges/topCommiter.png']\")).to.not.be.empty();" +
						"expect($(\"#badges .badge:nth-child(2) img[src='/badges/fatty.png']\")).to.not.be.empty();"
		);
	}

//    static boolean jsTest(String jsTest) {
//            return 0 == new Shell().execute(format("./mocha.sh src/test/resources/%s %d", jsTest, port()));
//	}

	static Build build(String result, String sha1) {
		return new Build(result, new ChangesSet(asList(new Item(sha1))));
	}

	static RepositoryCommit commit(String sha1, String login, String avatarUrl, String message) {
		return new RepositoryCommit() //
				.setSha(sha1) //
				.setStats(new CommitStats()) //
				.setAuthor(new User().setLogin(login).setAvatarUrl(avatarUrl)) //
				.setCommit(new Commit().setMessage(message).setAuthor(new CommitUser().setDate(new Date())));
	}

	static int port() {
		return codeStoryServer.service().getPort();
	}

	static ResponseSpecification expect() {
		return given().port(port()).expect();
	}
}
