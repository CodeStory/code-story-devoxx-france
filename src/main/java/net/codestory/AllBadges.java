package net.codestory;

import com.google.inject.*;
import groovy.lang.Binding;
import groovy.lang.*;
import org.eclipse.egit.github.core.*;

import java.util.*;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.ImmutableMap.*;
import static net.gageot.listmaker.ListMaker.*;

public class AllBadges {
	@Inject AllCommits allCommits;

	public List<Badge> list() {
		return with(topCommitter(), fattyCommitter()).exclude(isNull()).toList();
	}

	Badge topCommitter() {
		User user = (User) groovy("(commits.groupBy { it?.author?.login }.findAll { it.key != null }.max { it.value.size }?.value ?: [])[0]?.author");
		if (user == null) {
			return null;
		}

		return new Badge("Top Committer", "/badges/topCommiter.png", user.getAvatarUrl());
	}

	Badge fattyCommitter() {
		User user = (User) groovy("commits.findAll { it.stats != null }.max { it.stats.additions - it.stats.deletions }?.author ");
		if (user == null) {
			return null;
		}

		return new Badge("Fatty Committer", "/badges/fatty.png", user.getAvatarUrl());
	}

	private Object groovy(String script) {
		Binding bindings = new Binding(of("commits", allCommits.list()));

		return new GroovyShell(bindings).evaluate(script);
	}
}
