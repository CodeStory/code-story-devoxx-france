package net.codestory;

import org.eclipse.egit.github.core.*;
import org.joda.time.format.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {
	private static String JL_GRAVATAR = "https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e";

	List<RepositoryCommit> commits = new AllCommits("jlm", "NodeGravatar").list();

	@Test
	public void should_display_project_commits() throws Exception {
		assertThat(commits).hasSize(11);

		RepositoryCommit firstCommit = commits.get(0);
		assertThat(firstCommit.getAuthor().getLogin()).isEqualTo("jlm");
		assertThat(firstCommit.getAuthor().getAvatarUrl()).contains(JL_GRAVATAR);
		assertThat(firstCommit.getCommit().getMessage()).isEqualTo("Update README.md");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		assertThat(formatter.print(firstCommit.getCommit().getAuthor().getDate().getTime())).isEqualTo("19/04/2012");
	}
}
