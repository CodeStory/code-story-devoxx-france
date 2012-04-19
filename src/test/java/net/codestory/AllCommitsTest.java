package net.codestory;

import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {
	List<Commit> commits = new AllCommits().list();

	@Test
	public void should_display_project_commits() throws Exception {

		assertThat(commits).hasSize(10);
		assertThat(commits).onProperty("author").containsSequence("jlm", "jlm", "dgageot");
		assertThat(commits).onProperty("message").containsSequence("removing file extensiosn", "Adding the right pictures", "Unused file");
	}
}
