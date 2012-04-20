package net.codestory;

import net.codestory.jenkins.*;
import org.junit.*;

import java.io.*;
import java.util.*;

import static org.fest.assertions.Assertions.*;

public class AllBuildsTest {
	@Test
	public void should_list_builds() throws IOException {
		List<Build> builds = new AllBuilds().list();

		assertThat(builds).onProperty("result").contains("UNSTABLE");
		Build firstBuild = builds.get(0);
		ChangesSet changeset = firstBuild.getChangeSet();
		List<Item> items = changeset.getItems();
		Item item = items.get(0);
		assertThat(item.getCommitId()).hasSize(40);
	}
}
