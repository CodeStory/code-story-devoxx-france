package net.codestory.badges;

import com.google.inject.*;
import groovy.lang.Binding;
import groovy.lang.*;
import net.codestory.github.*;
import org.eclipse.egit.github.core.*;

import java.util.*;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.ImmutableMap.*;
import static java.util.Arrays.asList;
import static net.gageot.listmaker.ListMaker.*;

public class AllBadges {
	@Inject AllCommits allCommits;

	public List<Badge> list() {
		return excludeNullBadges(asList( //
				badge(topCommitter(), "Top Committer", "top.png"), //
				badge(fattyCommitter(), "Fatty Committer", "fatty.png"), //
				badge(verboseCommitter(), "Verbose Committer", "verbose.png")));
	}

	User topCommitter() {
		return (User) groovy("(commits.groupBy { it.author?.login }.findAll { it.key != null }.max { it.value.size }?.value ?: [])[0]?.author");
	}

	User fattyCommitter() {
		return (User) groovy("commits.findAll { it.stats != null }.max { it.stats.additions - it.stats.deletions }?.author ");
	}

	User verboseCommitter() {
		return (User) groovy("commits.max { it.commit?.message ?: '' }?.author");
	}

	Object groovy(String script) {
		Binding bindings = new Binding(of("commits", allCommits.list()));

		return new GroovyShell(bindings).evaluate(script);
	}

	static Badge badge(User user, String label, String image) {
		return user == null ? null : new Badge(label, "/badges/" + image, user.getAvatarUrl());
	}

	static List<Badge> excludeNullBadges(List<Badge> badges) {
		return with(badges).exclude(isNull()).toList();
	}
}
