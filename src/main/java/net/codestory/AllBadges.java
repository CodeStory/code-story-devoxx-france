package net.codestory;

import com.google.inject.*;

import java.util.*;

import static java.util.Arrays.*;

public class AllBadges {
	@Inject AllCommits allCommits;

	public List<Badge> list() {
		return asList( //
				new Badge("Top Committer", "TopCommitter.png"), //
				new Badge("Fatty Committer", "FattyCommitter.png"));
	}
}
