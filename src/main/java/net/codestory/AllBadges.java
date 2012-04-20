package net.codestory;

import com.google.inject.*;

import java.util.*;

import static java.util.Arrays.*;

public class AllBadges {
	@Inject AllCommits allCommits;

	public List<Badge> list() {
		return asList( //
				new Badge("Top Committer", "/badges/topCommiter.png", "https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"), //
				new Badge("Fatty Committer", "/badges/fatty.png", "https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
	}
}
