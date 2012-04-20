package net.codestory;

import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllBadgesTest {
	private AllBadges allBadges = new AllBadges();

	@Test
	public void should_list_all_badges() throws Exception {
		List<Badge> badges = allBadges.list();

		assertThat(badges).onProperty("label").containsSequence("Top Committer", "Fatty Committer");
		assertThat(badges).onProperty("image").containsSequence("TopCommitter.png", "FattyCommitter.png");
	}
}
