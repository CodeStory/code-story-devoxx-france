package net.codestory.jenkins;

import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllBuildsTest {
	AllBuilds allBuilds = new AllBuilds();

	@Test
	public void should_list_builds() {
		List<Build> builds = allBuilds.list();

		assertThat(builds).onProperty("result").contains("UNSTABLE");
		Item item = builds.get(0).getChangeSet().getItems().get(0);
		assertThat(item.getCommitId()).hasSize(40);
	}
}
