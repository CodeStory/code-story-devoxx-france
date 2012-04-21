package net.codestory.github;

import org.eclipse.egit.github.core.*;
import org.junit.*;

import java.util.*;

import static net.codestory.misc.DateFormat.*;
import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {
	AllCommits allCommits = new AllCommits("dgageot", "NodeGravatar");

	@Test
	public void should_list_commits() {
		List<RepositoryCommit> commits = allCommits.list();

		assertThat(commits).hasSize(10);
	}

	@Test
	public void should_get_first_commit() {
		RepositoryCommit first = allCommits.list().get(0);

		assertThat(first.getSha()).isEqualTo("710ff33fed6d4b295f9e792bcf722c622a51d2f0");
		assertThat(first.getAuthor().getLogin()).isEqualTo("jeanlaurent");
		assertThat(first.getAuthor().getAvatarUrl()).contains("https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e");
		assertThat(first.getCommit().getMessage()).isEqualTo("removing file extensiosn");
		assertThat(format(first.getCommit().getAuthor().getDate())).isEqualTo("29/03/2012");
	}
}
