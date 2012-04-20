package net.codestory;

import org.eclipse.egit.github.core.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {
	private static String JL_GRAVATAR = "https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e";
	private static String DAVID_GRAVATAR = "https://secure.gravatar.com/avatar/f0887bf6175ba40dca795eb37883a8cf";

	List<Commit> commits = new AllCommits("jlm", "NodeGravatar").list();

	@Test
	public void should_display_project_commits() throws Exception {
		assertThat(commits).hasSize(11);
		assertThat(commits).onProperty("author").containsSequence("jlm", "jlm", "jlm", "dgageot");
		assertThat(commits).onProperty("gravatarUrl").containsSequence(JL_GRAVATAR, JL_GRAVATAR, JL_GRAVATAR, DAVID_GRAVATAR);
		assertThat(commits).onProperty("message").containsSequence("Update README.md", "removing file extensiosn", "Adding the right pictures", "Unused file");
		assertThat(commits).onProperty("date").containsSequence("19/04/2012", "29/03/2012", "29/03/2012", "29/03/2012");
	}

	@Test
	public void should_work_with_a_commit_without_commiter() {
		RepositoryCommit repositoryCommit = new RepositoryCommit();

		Commit commit = AllCommits.TO_COMMIT.apply(repositoryCommit);

		assertThat(commit.getAuthor()).isEqualTo("");
	}
}
