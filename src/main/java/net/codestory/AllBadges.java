package net.codestory;

import java.util.*;

import static java.util.Arrays.*;

public class AllBadges {
	private static List<Badge> badges = asList( //
			new Badge("Top Committer", "TopCommitter.png"), //
			new Badge("Fatty Committer", "FattyCommitter.png"));

	public AllBadges(AllCommits allCommits) {
	}

	public List<Badge> list() {
		return badges;
	}
}
