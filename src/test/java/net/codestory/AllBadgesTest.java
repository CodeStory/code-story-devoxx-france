package net.codestory;

import org.eclipse.egit.github.core.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import java.util.*;

import static java.util.Arrays.*;
import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AllBadgesTest {
	@Mock AllCommits allCommits;
	@InjectMocks AllBadges allBadges;

	@Test
	public void should_list_badges() {
		when(allCommits.list()).thenReturn(asList(commit("dgageot", "url1")));

		List<Badge> badges = allBadges.list();

		assertThat(badges).onProperty("label").containsExactly("Top Committer", "Fatty Committer");
	}

	@Test
	public void should_show_top_committer() {
		when(allCommits.list()).thenReturn(asList(commit("dgageot", "url1"), commit("dgageot", "url1"), commit("jlm", "url2")));

		Badge topCommitter = allBadges.topCommitter();

		assertThat(topCommitter.getGravatarUrl()).isEqualTo("url1");
	}

	@Test
	public void should_show_top_committer_with_partial_commit() {
		when(allCommits.list()).thenReturn(asList(commit(), commit(), commit("jlm", "url2")));

		Badge topCommitter = allBadges.topCommitter();

		assertThat(topCommitter.getGravatarUrl()).isEqualTo("url2");
	}

	@Test
	public void should_show_top_committer_with_no_commit() {
		when(allCommits.list()).thenReturn(Collections.<RepositoryCommit>emptyList());

		Badge topCommitter = allBadges.topCommitter();

		assertThat(topCommitter).isNull();
	}

	@Test
	public void should_show_fatty_committer() {
		when(allCommits.list()).thenReturn(asList(commit("jlm", "url2", 100, 0), commit("dgageot", "url1", 0, 100)));

		Badge fattyCommitter = allBadges.fattyCommitter();

		assertThat(fattyCommitter.getGravatarUrl()).isEqualTo("url2");
	}

	@Test
	public void should_show_fatty_committer_with_partial_commit() {
		when(allCommits.list()).thenReturn(asList(commit(), commit("dgageot", "url1", 0, 100)));

		Badge fattyCommitter = allBadges.fattyCommitter();

		assertThat(fattyCommitter.getGravatarUrl()).isEqualTo("url1");
	}

	@Test
	public void should_show_fatty_committer_with_no_commit() {
		when(allCommits.list()).thenReturn(Collections.<RepositoryCommit>emptyList());

		Badge fattyCommitter = allBadges.fattyCommitter();

		assertThat(fattyCommitter).isNull();
	}

	@Test
	public void shouldnt_list_null_badges() {
		when(allCommits.list()).thenReturn(Collections.<RepositoryCommit>emptyList());

		List<Badge> badges = allBadges.list();

		assertThat(badges).isEmpty();
	}

	static RepositoryCommit commit(String login, String avatarUrl) {
		return commit(login, avatarUrl, 0, 0);
	}

	static RepositoryCommit commit(String login, String avatarUrl, int additions, int deletions) {
		return commit() //
				.setAuthor(new User().setLogin(login).setAvatarUrl(avatarUrl)) //
				.setStats(new CommitStats().setAdditions(additions).setDeletions(deletions));
	}

	static RepositoryCommit commit() {
		return new RepositoryCommit();
	}
}
