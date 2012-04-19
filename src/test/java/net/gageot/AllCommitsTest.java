package net.gageot;

import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {

	@Test
	public void should_display_project_commits() throws Exception {
		AllCommits allCommits = new AllCommits();
		List<Commit> commits = allCommits.list();
		assertThat(commits).hasSize(3);
		assertThat(commits).onProperty("author").containsExactly("dgageot", "jlmorlhon", "seblm");
	}
}
