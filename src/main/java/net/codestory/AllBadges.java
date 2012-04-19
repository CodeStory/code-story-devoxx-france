package net.codestory;

import java.util.*;

import static java.util.Arrays.*;

public class AllBadges {
	private static List<Badge> badges = asList(new Badge("Top Committer"), new Badge("Fatty Committer"));

	public List<Badge> list() {
		return badges;
	}
}
