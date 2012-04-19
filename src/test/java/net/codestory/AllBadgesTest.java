package net.codestory;

import com.google.inject.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllBadgesTest {

	@Test
	public void should_get_all_badges() throws Exception {
		Injector injector = Guice.createInjector();
		List<Badge> allBadges = injector.getInstance(AllBadges.class).list();
		assertThat(allBadges).hasSize(2);
		assertThat(allBadges).onProperty("label").containsSequence("Top Committer", "Fatty Committer");
		assertThat(allBadges).onProperty("image").containsSequence("TopCommitter.png", "FattyCommitter.png");
	}
}
