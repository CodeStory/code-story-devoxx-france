package net.codestory.badges;

import net.codestory.commits.*;
import org.eclipse.egit.github.core.Commit;
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

		assertThat(badges).onProperty("label").containsExactly("Top Committer", "Fatty Committer", "Verbose Committer");
		assertThat(badges).onProperty("image").containsExactly("/badges/top.png", "/badges/fatty.png", "/badges/verbose.png");
	}

	@Test
	public void should_show_top_committer() {
		when(allCommits.list()).thenReturn(asList(commit("dgageot", "url1"), commit("dgageot", "url1"), commit("jlm", "url2")));

		User topCommitter = allBadges.topCommitter();

		assertThat(topCommitter.getAvatarUrl()).isEqualTo("url1");
	}

	@Test
	public void should_show_top_committer_with_empty_commit() {
		when(allCommits.list()).thenReturn(asList(commit(), commit(), commit("jlm", "url2")));

		User topCommitter = allBadges.topCommitter();

		assertThat(topCommitter.getAvatarUrl()).isEqualTo("url2");
	}

	@Test
	public void should_show_top_committer_with_no_commit() {
		when(allCommits.list()).thenReturn(Collections.<RepositoryCommit>emptyList());

		User topCommitter = allBadges.topCommitter();

		assertThat(topCommitter).isNull();
	}

	@Test
	public void should_show_fatty_committer() {
		when(allCommits.list()).thenReturn(asList(commit("jlm", "url2", 100, 0), commit("dgageot", "url1", 0, 100)));

		User fattyCommitter = allBadges.fattyCommitter();

		assertThat(fattyCommitter.getAvatarUrl()).isEqualTo("url2");
	}

	@Test
	public void should_show_fatty_committer_with_empty_commit() {
		when(allCommits.list()).thenReturn(asList(commit(), commit("dgageot", "url1", 0, 100)));

		User fattyCommitter = allBadges.fattyCommitter();

		assertThat(fattyCommitter.getAvatarUrl()).isEqualTo("url1");
	}

	@Test
	public void should_show_fatty_committer_with_no_commit() {
		when(allCommits.list()).thenReturn(Collections.<RepositoryCommit>emptyList());

		User fattyCommitter = allBadges.fattyCommitter();

		assertThat(fattyCommitter).isNull();
	}

	@Test
	public void should_show_verbose_committer() {
		when(allCommits.list()).thenReturn(asList(commit("jlm", "url2", "LONG MESSAGE"), commit("dgageot", "url1", "Z")));

		User verboseCommitter = allBadges.verboseCommitter();

		assertThat(verboseCommitter.getAvatarUrl()).isEqualTo("url2");
	}

	@Test
	public void should_show_verbose_committer_with_empty_commit() {
		when(allCommits.list()).thenReturn(asList(commit(), commit("dgageot", "url1", "")));

		User verboseCommitter = allBadges.verboseCommitter();

		assertThat(verboseCommitter).isNull();
	}

	@Test
	public void should_show_verbose_committer_with_with_no_commit() {
		when(allCommits.list()).thenReturn(Collections.<RepositoryCommit>emptyList());

		User verboseCommitter = allBadges.verboseCommitter();

		assertThat(verboseCommitter).isNull();
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

	static RepositoryCommit commit(String login, String avatarUrl, String message) {
		return commit(login, avatarUrl, 0, 0).setCommit(new Commit().setMessage(message));
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
