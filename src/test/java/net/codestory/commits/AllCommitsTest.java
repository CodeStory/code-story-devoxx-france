package net.codestory.commits;

import org.eclipse.egit.github.core.*;
import org.junit.*;

import java.util.*;

import static net.codestory.misc.DateFormat.*;
import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {
	static List<RepositoryCommit> commits;

	@BeforeClass
	public static void getAllCommits() {
		commits = new AllCommits("dgageot", "NodeGravatar").list();
	}

	@Test
	public void should_list_commits() {
		assertThat(commits).hasSize(10);
	}

	@Test
	public void should_get_first_commit() {
		RepositoryCommit first = commits.get(0);

		assertThat(first.getSha()).isEqualTo("710ff33fed6d4b295f9e792bcf722c622a51d2f0");
		assertThat(first.getAuthor().getLogin()).isEqualTo("jeanlaurent");
		assertThat(first.getAuthor().getAvatarUrl()).startsWith("https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e");
		assertThat(first.getCommit().getMessage()).isEqualTo("removing file extensiosn");
		assertThat(format(first.getCommit().getAuthor().getDate())).isEqualTo("29/03/2012");
	}
}
