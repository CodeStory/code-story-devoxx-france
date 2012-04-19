package net.codestory;

import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {
	List<Commit> commits = new AllCommits().list();

	@Test
	public void should_display_project_commits() throws Exception {

		assertThat(commits).hasSize(11);
		assertThat(commits).onProperty("author").containsSequence("jlm", "jlm", "jlm", "dgageot");
		assertThat(commits).onProperty("message").containsSequence("Update README.md", "removing file extensiosn", "Adding the right pictures", "Unused file");
		assertThat(commits).onProperty("date").containsSequence("19/04/2012", "29/03/2012", "29/03/2012", "29/03/2012");
	}
}
