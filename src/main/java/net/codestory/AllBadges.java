package net.codestory;

import com.google.common.util.concurrent.*;
import com.google.inject.*;
import groovy.lang.*;
import org.eclipse.egit.github.core.*;

import java.util.*;

import static java.util.Arrays.*;

public class AllBadges {
	@Inject AllCommits allCommits;

	public List<Badge> list() {
		return asList( //
				topComitter(), //
				new Badge("Fatty Committer", "/badges/fatty.png", "https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
	}

	private Badge topComitter() {
		List<RepositoryCommit> list = allCommits.list();
		// map < gravatarurl, int>
		AtomicLongMap<String> commitByAuthor = AtomicLongMap.create();
		for (RepositoryCommit repositoryCommit : list) {
			commitByAuthor.incrementAndGet(repositoryCommit.getAuthor().getAvatarUrl());
		}
		// max ^^
		long max = 0;
		String maxAvatarUrl = null;
		Map<String, Long> commitByAuthorMap = commitByAuthor.asMap();
		for (String avatarUrl : commitByAuthorMap.keySet()) {
			if (commitByAuthorMap.get(avatarUrl).longValue() > max) {
				max = commitByAuthorMap.get(avatarUrl).longValue();
				maxAvatarUrl = avatarUrl;
			}
		}

		return new Badge("Top Committer", "/badges/topCommiter.png", maxAvatarUrl);
	}
}
