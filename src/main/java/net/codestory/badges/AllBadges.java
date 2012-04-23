package net.codestory.badges;

import com.google.inject.*;
import groovy.lang.*;
import net.codestory.github.*;
import org.eclipse.egit.github.core.*;

import java.util.*;

import static com.google.common.base.Predicates.*;
import static java.util.Arrays.asList;
import static net.gageot.listmaker.ListMaker.*;

public class AllBadges {
	private static final String TOP_COMMITTER = "(commits.groupBy { it.author?.login }.findAll { it.key != null }.max { it.value.size }?.value ?: [])[0]?.author";
	private static final String FATTY_COMMITTER = "commits.findAll { it.stats != null }.max { it.stats.additions - it.stats.deletions }?.author ";
	private static final String VERBOSE_COMMITTER = "commits.max { it.commit?.message ?: '' }?.author";

	@Inject AllCommits allCommits;

	public List<Badge> list() {
		return excludeNullBadges(asList( //
				badge(topCommitter(), "Top Committer", "top.png"), //
				badge(fattyCommitter(), "Fatty Committer", "fatty.png"), //
				badge(verboseCommitter(), "Verbose Committer", "verbose.png")));
	}

	User topCommitter() {
		return (User) groovy(TOP_COMMITTER);
	}

	User fattyCommitter() {
		return (User) groovy(FATTY_COMMITTER);
	}

	User verboseCommitter() {
		return (User) groovy(VERBOSE_COMMITTER);
	}

	Object groovy(String script) {
		GroovyShell groovy = new GroovyShell();
		groovy.setVariable("commits", allCommits.list());

		return groovy.evaluate(script);
	}

	static Badge badge(User user, String label, String image) {
		return user == null ? null : new Badge(label, "/badges/" + image, user.getAvatarUrl());
	}

	static List<Badge> excludeNullBadges(List<Badge> badges) {
		return with(badges).exclude(isNull()).toList();
	}
}
