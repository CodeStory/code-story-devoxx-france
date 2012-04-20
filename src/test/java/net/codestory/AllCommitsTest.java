package net.codestory;

import org.eclipse.egit.github.core.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {
	AllCommits allCommits = new AllCommits("jlm", "NodeGravatar");

	@Test
	public void should_display_first_commit() {
		List<RepositoryCommit> commits = allCommits.list();

		assertThat(commits).hasSize(11);
		RepositoryCommit firstCommit = commits.get(0);

		assertThat(firstCommit.getSha()).isEqualTo("fb26b2a6957f46bd00c9b4159622610a8aca57bd");
		assertThat(firstCommit.getAuthor().getLogin()).isEqualTo("jlm");
		assertThat(firstCommit.getAuthor().getAvatarUrl()).contains("https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e");
		assertThat(firstCommit.getCommit().getMessage()).isEqualTo("Update README.md");
		assertThat(DateFormat.format(firstCommit.getCommit().getAuthor().getDate())).isEqualTo("19/04/2012");
	}
}
