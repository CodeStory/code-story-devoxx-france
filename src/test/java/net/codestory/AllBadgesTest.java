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
		assertThat(badges).onProperty("image").containsSequence("/badges/topCommiter.png", "/badges/fatty.png");
		assertThat(badges).onProperty("gravatarUrl").containsSequence("https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e", "https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e");
	}
}
