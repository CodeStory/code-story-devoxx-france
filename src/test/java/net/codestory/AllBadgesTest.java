package net.codestory;

import com.google.inject.*;
import com.google.inject.name.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllBadgesTest {

	@Test
	public void should_get_all_badges() throws Exception {

		Module codeStoryModule = new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("user")).toInstance("jlm");
				bind(String.class).annotatedWith(Names.named("project")).toInstance("NodeGravatar");
			}
		};


		Injector injector = Guice.createInjector(codeStoryModule);
		List<Badge> allBadges = injector.getInstance(AllBadges.class).list();
		assertThat(allBadges).hasSize(2);
		assertThat(allBadges).onProperty("label").containsSequence("Top Committer", "Fatty Committer");
		assertThat(allBadges).onProperty("image").containsSequence("TopCommitter.png", "FattyCommitter.png");
	}
}
