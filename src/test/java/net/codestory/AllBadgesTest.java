package net.codestory;

import org.eclipse.egit.github.core.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import static java.util.Arrays.*;
import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AllBadgesTest {
	@Mock AllCommits allCommits;
	@InjectMocks AllBadges allBadges;

	@Test
	public void should_show_top_committer() {
		when(allCommits.list()).thenReturn(asList(commit("dgageot", "url1"), commit("dgageot", "url1"), commit("jlm", "url2")));

		Badge topCommitter = allBadges.list().get(0);

		assertThat(topCommitter.getGravatarUrl()).isEqualTo("url1");
	}

	@Test
	public void should_show_fatty_committer() {
		when(allCommits.list()).thenReturn(asList(commit("jlm", "url2", 100, 0), commit("dgageot", "url1", 0, 100)));

		Badge fattyCommitter = allBadges.list().get(1);

		assertThat(fattyCommitter.getGravatarUrl()).isEqualTo("url2");
	}

	static RepositoryCommit commit(String login, String avatarUrl) {
		return commit(login, avatarUrl, 0, 0);
	}

	static RepositoryCommit commit(String login, String avatarUrl, int additions, int deletions) {
		return new RepositoryCommit() //
				.setAuthor(new User().setLogin(login).setAvatarUrl(avatarUrl)) //
				.setStats(new CommitStats().setAdditions(additions).setDeletions(deletions));
	}
}
