package net.codestory;

import com.google.inject.*;
import groovy.lang.Binding;
import groovy.lang.*;
import org.eclipse.egit.github.core.*;

import java.util.*;

import static com.google.common.collect.ImmutableMap.*;
import static java.util.Arrays.*;

public class AllBadges {
	@Inject AllCommits allCommits;

	public List<Badge> list() {
		return asList(topCommitter(), fattyCommitter());
	}

	private Badge topCommitter() {
		User topCommitter = (User) groovy("commits.findAll { it.author.login != null }.groupBy { it.author.login }.max { it.value.size }.value[0].author");

		return new Badge("Top Committer", "/badges/topCommiter.png", topCommitter.getAvatarUrl());
	}

	private Badge fattyCommitter() {
		User fattyCommitter = (User) groovy("commits.findAll { it.stats != null }.max { it.stats.additions - it.stats.deletions }.author ");

		return new Badge("Fatty Committer", "/badges/fatty.png", fattyCommitter.getAvatarUrl());
	}

	private Object groovy(String script) {
		Binding bindings = new Binding(of("commits", allCommits.list()));

		return new GroovyShell(bindings).evaluate(script);
	}
}
