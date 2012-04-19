package net.gageot;

import org.junit.*;

import static org.fest.assertions.Assertions.*;

public class AllCommitsTest {

	@Test
	public void should_display_project_commits() throws Exception {
		AllCommits allCommits = new AllCommits();
		assertThat(allCommits.list("codestory")).hasSize(3);
	}
}
