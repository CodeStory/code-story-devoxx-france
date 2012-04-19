package net.codestory;

import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllBadgesTest {

	@Test
	public void should_get_all_badges() throws Exception {
		List<Badge> allBadges = new AllBadges(new AllCommits()).list();
		assertThat(allBadges).hasSize(2);
		assertThat(allBadges).onProperty("label").containsSequence("Top Committer", "Fatty Committer");
	}
}
