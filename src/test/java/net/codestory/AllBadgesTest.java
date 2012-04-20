package net.codestory;

import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllBadgesTest {
	@Test
	public void should_get_all_badges() throws Exception {
		AllBadges allBadges = new AllBadges();
		allBadges.allCommits = new AllCommits("", "");

		List<Badge> list = allBadges.list();

		assertThat(list).onProperty("label").containsExactly("Top Committer", "Fatty Committer");
		assertThat(list).onProperty("image").containsExactly("TopCommitter.png", "FattyCommitter.png");
	}
}
