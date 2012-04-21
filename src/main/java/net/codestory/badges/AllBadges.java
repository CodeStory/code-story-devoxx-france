package net.codestory.badges;

import com.google.inject.*;
import groovy.lang.Binding;
import groovy.lang.*;
import net.codestory.github.*;
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

		return badge(user, "Top Committer", "/badges/topCommiter.png");
	}

	Badge fattyCommitter() {
		User user = (User) groovy("commits.findAll { it.stats != null }.max { it.stats.additions - it.stats.deletions }?.author ");

		return badge(user, "Fatty Committer", "/badges/fatty.png");
	}

	Object groovy(String script) {
		Binding bindings = new Binding(of("commits", allCommits.list()));

		return new GroovyShell(bindings).evaluate(script);
	}

	Badge badge(User user, String label, String image) {
		return user == null ? null : new Badge(label, image, user.getAvatarUrl());
	}
}
