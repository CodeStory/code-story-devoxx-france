package net.codestory;

import com.google.inject.*;
import groovy.lang.Binding;
import groovy.lang.*;

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
		Binding bindings = new Binding();
		bindings.setVariable("allCommits", allCommits.list());

		String script = "allCommits.groupBy { it.author.avatarUrl }.max { it.value.size }.key";

		GroovyShell groovy = new GroovyShell(bindings);
		String avatarUrl = (String) groovy.evaluate(script);

		return new Badge("Top Committer", "/badges/topCommiter.png", avatarUrl);
	}
}
